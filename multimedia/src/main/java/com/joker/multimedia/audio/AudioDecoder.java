package com.joker.multimedia.audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import com.joker.pacific.log.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by joker on 17-11-27.
 * Android上实际测试 解码并不成功，一般不用硬解
 */

public class AudioDecoder {
    private Logger mLogger = Logger.getLogger(AudioDecoder.class);
    private static final String DEFAULT_MIME_TYPE = "audio/mp4a-latm";
    private static final int DEFAULT_CHANNEL_NUM = 2;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_MAX_BUFFER_SIZE = 16384;

    private MediaCodec mMediaCodec;
    private OnAudioDecodedListener mAudioDecodedListener;
    private boolean mIsOpened = false;
    private boolean mIsFirstFrame = true;

    public interface OnAudioDecodedListener {
        void onFrameDecoded(byte[] decoded, long presentationTimeUs);
    }

    public boolean open() {
        if (mIsOpened) {
            return true;
        }
        return open(DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_NUM, DEFAULT_MAX_BUFFER_SIZE);
    }

    public boolean open(int samplerate, int channels, int maxBufferSize) {
        mLogger.info("open audio decoder: " + samplerate + ", " + channels + ", " + maxBufferSize);
        if (mIsOpened) {
            return true;
        }

        try {
            mMediaCodec = MediaCodec.createDecoderByType(DEFAULT_MIME_TYPE);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, DEFAULT_MIME_TYPE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channels);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, samplerate);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 64 * 1000);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize);
            format.setInteger(MediaFormat.KEY_IS_ADTS, 1);
            byte[] data = new byte[]{(byte) 0x11, (byte) 0x90};
            ByteBuffer csd_0 = ByteBuffer.wrap(data);
            format.setByteBuffer("csd-0", csd_0);
            mMediaCodec.configure(format, null, null, 0);
            mIsOpened = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(mMediaCodec == null){
            return false;
        }
        mMediaCodec.start();
        mLogger.info("open audio decoder success !");
        return true;
    }

    public synchronized void close() {
        mLogger.info("close audio decoder +");
        if (!mIsOpened) {
            return;
        }
        mMediaCodec.stop();
        mMediaCodec.release();
        mMediaCodec = null;
        mIsOpened = false;
        mLogger.info("close audio decoder -");
    }

    public boolean isOpened() {
        return mIsOpened;
    }

    public void setAudioDecodedListener(OnAudioDecodedListener listener) {
        mAudioDecodedListener = listener;
    }

    public synchronized boolean decode(byte[] input, long presentationTimeUs) {
        mLogger.debug("decode: " + presentationTimeUs);
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
//                if (mIsFirstFrame) {
//                    /**
//                     * Some formats, notably AAC audio and MPEG4, H.264 and H.265 video formats
//                     * require the actual data to be prefixed by a number of buffers containing
//                     * setup data, or codec specific data. When processing such compressed formats,
//                     * this data must be submitted to the codec after start() and before any frame data.
//                     * Such data must be marked using the flag BUFFER_FLAG_CODEC_CONFIG in a call to queueInputBuffer.
//                     */
//                    mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, MediaCodec.BUFFER_FLAG_CODEC_CONFIG);
//                    mIsFirstFrame = false;
//                } else {
//                }
                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        mLogger.debug("decode -");
        return false;
    }

    public synchronized boolean retrieve() {
        mLogger.debug("decode retrieve +");
        if (!mIsOpened) {
            return false;
        }

        try {
            ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 1000);
            while (outputBufferIndex >= 0) {
                mLogger.debug("decode retrieve frame " + bufferInfo.size);
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                if (mAudioDecodedListener != null) {
                    mAudioDecodedListener.onFrameDecoded(outData, bufferInfo.presentationTimeUs);
                }
                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                //解码未解完的数据
                outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        mLogger.debug("decode retrieve -");
        return true;
    }
}
