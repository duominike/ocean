package com.joker.multimedia.audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import com.joker.pacific.log.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by joker on 17-11-27.
 * 通过MediaCodec将音频数据编码
 */

public class AudioEncoder {
    private Logger mLogger = Logger.getLogger(AudioEncoder.class);
    private static final String DEFAULT_MIME_TYPE = "audio/mp4a-latm";
    private static final int DEFAULT_CHANNEL_NUM = 1;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_BITRATE = 128 * 1000; //AAC-LC, 64 *1024 for AAC-HE
    private static final int DEFAULT_PROFILE_LEVEL = MediaCodecInfo.CodecProfileLevel.AACObjectLC;
    private static final int DEFAULT_MAX_BUFFER_SIZE = 16384;

    private MediaCodec mMediaCodec;
    private OnAudioEncodedListener mAudioEncodedListener;
    private boolean mIsOpened = false;

    public interface OnAudioEncodedListener {
        void onFrameEncoded(byte[] encoded, long presentationTimeUs);
    }

    public boolean open() {
        if (mIsOpened) {
            return true;
        }
        return open(DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_NUM, DEFAULT_BITRATE, DEFAULT_MAX_BUFFER_SIZE);
    }

    public boolean open(int samplerate, int channels, int bitrate, int maxBufferSize) {
        mLogger.info("open audio encoder: " + samplerate + ", " + channels + ", " + maxBufferSize);
        if (mIsOpened) {
            return true;
        }

        try {
            mMediaCodec = MediaCodec.createEncoderByType(DEFAULT_MIME_TYPE);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, DEFAULT_MIME_TYPE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channels);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, samplerate);
            format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, DEFAULT_PROFILE_LEVEL);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize);
            mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
            mIsOpened = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        mLogger.info("open audio encoder success !");
        return true;
    }

    public synchronized void close() {
        mLogger.info("close audio encoder +");
        if (!mIsOpened) {
            return;
        }
        mMediaCodec.stop();
        mMediaCodec.release();
        mMediaCodec = null;
        mIsOpened = false;
        mLogger.info("close audio encoder -");
    }

    public boolean isOpened() {
        return mIsOpened;
    }

    public void setAudioEncodedListener(OnAudioEncodedListener listener) {
        mAudioEncodedListener = listener;
    }

    public synchronized boolean encode(byte[] input, long presentationTimeUs) {
       mLogger.info("encode: " + presentationTimeUs);
        if (!mIsOpened) {
            return false;
        }

        try {
            ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(1000);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input);
                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        mLogger.debug("encode -");
        return true;
    }

    public synchronized boolean retrieve() {
       mLogger.debug("encode retrieve +");
        if (!mIsOpened) {
            return false;
        }

        try {
            ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 1000);
            if (outputBufferIndex >= 0) {
                mLogger.debug("encode retrieve frame  " + bufferInfo.size);
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                outputBuffer.position(bufferInfo.offset);
                outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                byte[] frame = new byte[bufferInfo.size];
                outputBuffer.get(frame, 0, bufferInfo.size);
                if (mAudioEncodedListener != null) {
                    mAudioEncodedListener.onFrameEncoded(frame, bufferInfo.presentationTimeUs);
                }
                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        mLogger.debug("encode retrieve -");
        return true;
    }
}
