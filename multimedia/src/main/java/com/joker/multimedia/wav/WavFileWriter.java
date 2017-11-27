package com.joker.multimedia.wav;

import com.joker.pacific.log.Logger;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by joker on 17-11-27.
 * 写入音频数据到wav文件，打开文件时先写文件头
 */

public class WavFileWriter {
    private Logger mLogger = Logger.getLogger(WavFileWriter.class);
    private String mFilepath;
    private int mDataSize = 0;
    private DataOutputStream mDataOutputStream;

    public boolean openFile(String filepath, int sampleRateInHz, int channels, int bitsPerSample) throws IOException {
        if (mDataOutputStream != null) {
            closeFile();
        }
        mFilepath = filepath;
        mDataSize = 0;
        mDataOutputStream = new DataOutputStream(new FileOutputStream(filepath));
        return writeHeader(sampleRateInHz, bitsPerSample, channels);
    }

    public boolean closeFile() throws IOException {
        boolean ret = true;
        if (mDataOutputStream != null) {
            ret = writeDataSize();
            mDataOutputStream.close();
            mDataOutputStream = null;
        }
        return ret;
    }

    public boolean writeData(byte[] buffer, int offset, int count) {
        if (mDataOutputStream == null) {
            return false;
        }
        try {
            mLogger.info("writeData");
            mDataOutputStream.write(buffer, offset, count);
            mDataSize += count;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean writeHeader(int sampleRateInHz, int channels, int bitsPerSample) {
        if (mDataOutputStream == null) {
            return false;
        }

        WavFileHeader header = new WavFileHeader(sampleRateInHz, channels, bitsPerSample);

        try {
            mDataOutputStream.writeBytes(header.mChunkID);
            mDataOutputStream.write(intToByteArray(header.mChunkSize), 0, 4);
            mDataOutputStream.writeBytes(header.mFormat);
            mDataOutputStream.writeBytes(header.mSubChunk1ID);
            mDataOutputStream.write(intToByteArray(header.mSubChunk1Size), 0, 4);
            mDataOutputStream.write(shortToByteArray(header.mAudioFormat), 0, 2);
            mDataOutputStream.write(shortToByteArray(header.mNumChannel), 0, 2);
            mDataOutputStream.write(intToByteArray(header.mSampleRate), 0, 4);
            mDataOutputStream.write(intToByteArray(header.mByteRate), 0, 4);
            mDataOutputStream.write(shortToByteArray(header.mBlockAlign), 0, 2);
            mDataOutputStream.write(shortToByteArray(header.mBitsPerSample), 0, 2);
            mDataOutputStream.writeBytes(header.mSubChunk2ID);
            mDataOutputStream.write(intToByteArray(header.mSubChunk2Size), 0, 4);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        mLogger.info("writeHeader success");
        return true;
    }

    private boolean writeDataSize() {
        if (mDataOutputStream == null) {
            return false;
        }

        try {
            RandomAccessFile wavFile = new RandomAccessFile(mFilepath, "rw");
            // 该文件总大小，音频数据大小加上头部大小
            wavFile.seek(WavFileHeader.WAV_CHUNKSIZE_OFFSET);
            mLogger.info("file size is: " + intToByteArray((mDataSize + WavFileHeader.WAV_CHUNKSIZE_EXCLUDE_DATA)));
            wavFile.write(intToByteArray((mDataSize + WavFileHeader.WAV_CHUNKSIZE_EXCLUDE_DATA)), 0, 4);
            // 音频数据总大小
            mLogger.info("audio size is: " + intToByteArray((mDataSize)));
            wavFile.seek(WavFileHeader.WAV_SUB_CHUNKSIZE2_OFFSET);
            wavFile.write(intToByteArray((mDataSize)), 0, 4);
            wavFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        mLogger.info("writeDataSize success" );
        return true;
    }

    private static byte[] intToByteArray(int data) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array();
    }

    private static byte[] shortToByteArray(short data) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array();
    }
}
