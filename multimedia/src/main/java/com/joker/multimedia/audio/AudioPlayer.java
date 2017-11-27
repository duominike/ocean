package com.joker.multimedia.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.joker.pacific.log.Logger;

/**
 * Created by joker on 17-11-27.
 */

public class AudioPlayer {
    private Logger mLogger = Logger.getLogger(AudioPlayer.class);
    private static final int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int DEFAULT_PLAY_MODE = AudioTrack.MODE_STREAM;

    private volatile boolean mIsPlayStarted = false;
    private AudioTrack mAudioTrack;

    public boolean startPlayer() {
        return startPlayer(DEFAULT_STREAM_TYPE, DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT);
    }

    public boolean startPlayer(int streamType, int sampleRateInHz, int channelConfig, int audioFormat) {
        if (mIsPlayStarted) {
            mLogger.debug("Player already started !");
            return false;
        }

        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (bufferSizeInBytes == AudioTrack.ERROR_BAD_VALUE) {
            mLogger.debug( "Invalid parameter !");
            return false;
        }
        mLogger.info("getMinBufferSize = " + bufferSizeInBytes + " bytes !");

        mAudioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, DEFAULT_PLAY_MODE);
        if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
            mLogger.error("AudioTrack initialize fail !");
            return false;
        }

        mIsPlayStarted = true;

        mLogger.info("Start audio player success !");

        return true;
    }

    public void stopPlayer() {
        if (!mIsPlayStarted) {
            return;
        }

        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.stop();
        }

        mAudioTrack.release();
        mIsPlayStarted = false;

        mLogger.info("Stop audio player success !");
    }

    public boolean play(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        if (!mIsPlayStarted) {
            mLogger.error("Player not started !");
            return false;
        }

        if (mAudioTrack.write(audioData, offsetInBytes, sizeInBytes) != sizeInBytes) {
            mLogger.error("Could not write all the samples to the audio device !");
        }

        mAudioTrack.play();

        mLogger.debug("OK, Played " + sizeInBytes + " bytes !");

        return true;
    }
}
