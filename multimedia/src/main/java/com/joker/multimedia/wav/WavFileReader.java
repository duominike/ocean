package com.joker.multimedia.wav;

import com.joker.pacific.log.Logger;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by joker on 17-11-27.
 * 读取wav文件，先读取文件头，再读取音频数据
 */

public class WavFileReader {
    private Logger mLogger = Logger.getLogger(WavFileReader.class);
    private DataInputStream mDataInputStream;
    private WavFileHeader mWavFileHeader;

    public boolean openFile(String filepath) throws IOException {
        if (mDataInputStream != null) {
            closeFile();
        }
        mDataInputStream = new DataInputStream(new FileInputStream(filepath));
        return readHeader();
    }

    public void closeFile() throws IOException {
        if (mDataInputStream != null) {
            mDataInputStream.close();
            mDataInputStream = null;
        }
    }

    public WavFileHeader getmWavFileHeader() {
        return mWavFileHeader;
    }

    public int readData(byte[] buffer, int offset, int count) {
        if (mDataInputStream == null || mWavFileHeader == null) {
            return -1;
        }

        try {
            int nbytes = mDataInputStream.read(buffer, offset, count);
            if (nbytes == -1) {
                return 0;
            }
            return nbytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private boolean readHeader() {
        if (mDataInputStream == null) {
            return false;
        }

        WavFileHeader header = new WavFileHeader();

        byte[] intValue = new byte[4];
        byte[] shortValue = new byte[2];

        try {
            header.mChunkID = "" + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte();
            mLogger.debug("Read file chunkID:" + header.mChunkID);

            mDataInputStream.read(intValue);
            header.mChunkSize = byteArrayToInt(intValue);
            mLogger.debug("Read file chunkSize:" + header.mChunkSize);

            header.mFormat = "" + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte();
            mLogger.debug("Read file format:" + header.mFormat);

            header.mSubChunk1ID = "" + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte();
           mLogger.debug("Read fmt chunkID:" + header.mSubChunk1ID);

            mDataInputStream.read(intValue);
            header.mSubChunk1Size = byteArrayToInt(intValue);
            mLogger.debug("Read fmt chunkSize:" + header.mSubChunk1Size);

            mDataInputStream.read(shortValue);
            header.mAudioFormat = byteArrayToShort(shortValue);
            mLogger.debug( "Read audioFormat:" + header.mAudioFormat);

            mDataInputStream.read(shortValue);
            header.mNumChannel = byteArrayToShort(shortValue);
            mLogger.debug("Read channel number:" + header.mNumChannel);

            mDataInputStream.read(intValue);
            header.mSampleRate = byteArrayToInt(intValue);
            mLogger.debug("Read samplerate:" + header.mSampleRate);

            mDataInputStream.read(intValue);
            header.mByteRate = byteArrayToInt(intValue);
            mLogger.debug("Read byterate:" + header.mByteRate);

            mDataInputStream.read(shortValue);
            header.mBlockAlign = byteArrayToShort(shortValue);
            mLogger.debug( "Read blockalign:" + header.mBlockAlign);

            mDataInputStream.read(shortValue);
            header.mBitsPerSample = byteArrayToShort(shortValue);
            mLogger.debug("Read bitspersample:" + header.mBitsPerSample);

            header.mSubChunk2ID = "" + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte() + (char) mDataInputStream.readByte();
            mLogger.debug("Read data chunkID:" + header.mSubChunk2ID);

            mDataInputStream.read(intValue);
            header.mSubChunk2Size = byteArrayToInt(intValue);
            mLogger.debug("Read data chunkSize:" + header.mSubChunk2Size);

            mLogger.debug("Read wav file success !");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        mWavFileHeader = header;

        return true;
    }

    private static short byteArrayToShort(byte[] b) {
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    private static int byteArrayToInt(byte[] b) {
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
