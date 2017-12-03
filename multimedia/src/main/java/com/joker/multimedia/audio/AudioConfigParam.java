package com.joker.multimedia.audio;

/**
 * Created by joker on 17-12-3.
 */

public class AudioConfigParam {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 采样率
     */
    private int sampleRate;
    /**
     * 音频码流
     */
    private int audioBitrate;
    /**
     * 声道数
     */
    private int audioChannelCount;

    /**
     * 编码方式,是否是硬编
     */
    private boolean isHardEncode = false;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public int getAudioChannelCount() {
        return audioChannelCount;
    }

    public void setAudioChannelCount(int audioChannelCount) {
        this.audioChannelCount = audioChannelCount;
    }

    public boolean isHardEncode() {
        return isHardEncode;
    }

    public void setHardEncode(boolean hardEncode) {
        isHardEncode = hardEncode;
    }
}
