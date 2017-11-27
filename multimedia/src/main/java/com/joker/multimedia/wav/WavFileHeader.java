package com.joker.multimedia.wav;

/**
 * Created by joker on 17-11-27.
 * wav文件头，分为三部分：
 *  顶层信息块部分：
 *      - chunkID来表示这时一个 RIFF 格式文件
 *      - Format填入WAVE来标识这是一个wav文件
 *  fmt信息块部分：
 *      - 主要记录wav音频文件的详细音频参数信息：通道数，采样率，位宽
 *
 *  data信息块：
 *      - 由Subchunk2Size这个字段来记录后面存储的二进制原始音频数据的长度
 */

public class WavFileHeader {
    public static final int WAV_FILE_HEADER_SIZE = 44;
    public static final int WAV_CHUNKSIZE_EXCLUDE_DATA = 36;

    public static final int WAV_CHUNKSIZE_OFFSET = 4;
    public static final int WAV_SUB_CHUNKSIZE1_OFFSET = 16;
    public static final int WAV_SUB_CHUNKSIZE2_OFFSET = 40;

    public String mChunkID = "RIFF";
    public int mChunkSize = 0;
    public String mFormat = "WAVE";

    public String mSubChunk1ID = "fmt ";
    public int mSubChunk1Size = 16;
    public short mAudioFormat = 1;
    public short mNumChannel = 1; //声道数
    public int mSampleRate = 8000;// 采样率
    public int mByteRate = 0; // 位宽
    public short mBlockAlign = 0; //块对齐
    public short mBitsPerSample = 8; // 位宽

    public String mSubChunk2ID = "data";
    public int mSubChunk2Size = 0;

    public WavFileHeader() {

    }

    public WavFileHeader(int sampleRateInHz, int channels, int bitsPerSample) {
        mSampleRate = sampleRateInHz;
        mBitsPerSample = (short) bitsPerSample;
        mNumChannel = (short) channels;
        mByteRate = mSampleRate * mNumChannel * mBitsPerSample / 8;
        mBlockAlign = (short) (mNumChannel * mBitsPerSample / 8);
    }

}
