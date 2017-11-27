package com.joker.ocean.testview;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.joker.multimedia.audio.AudioCapturer;
import com.joker.multimedia.audio.AudioDecoder;
import com.joker.multimedia.audio.AudioEncoder;
import com.joker.multimedia.audio.AudioPlayer;
import com.joker.multimedia.wav.WavFileReader;
import com.joker.multimedia.wav.WavFileWriter;
import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by joker on 17-11-27.
 */

public class TestAudioRecordActivity extends BaseFragmentActivity implements View.OnClickListener{
    private TextView txtBeginRecord;
    private TextView txtStopRecord;
    private TextView txtBeginPlay;
    private TextView txtStopPlay;

    private AudioCapturer mAudioCapturer;
    private AudioEncoder mAudioEncoder;
    private AudioDecoder mAudioDecoder;
    private AudioPlayer mAudioPlayer;
    private String outPutFileEncoded = "test.aac";
    private String outPutFileUnEncoded = "test.pcm";
    private static final String OUTPUTDIR = "/ocean/record/";
    private WavFileWriter mWavFileWriter;
    private WavFileReader mWavFileReader;
    private volatile boolean isRecording;
    private volatile boolean isPlaying;
    private DataOutputStream dataAfterEncoded;
    private DataOutputStream dataBeforeEncoded;
    private AudioCapturer.OnAudioFrameCapturedListener mOnAudioFrameCapturedListener = new AudioCapturer.OnAudioFrameCapturedListener() {
        @Override
        public void onAudioFrameCaptured(byte[] audioData) {
            try{
                dataBeforeEncoded.write(audioData, 0, audioData.length);
            }catch (IOException e){
                e.printStackTrace();
            }
            mAudioEncoder.encode(audioData, System.nanoTime()/ 1000L);
        }
    };

    private byte[] adtsHeader = null;
    private int m_iAacObjectType;
    private int m_iSampleIndex;
    private int m_iChannels;

    private AudioEncoder.OnAudioEncodedListener mEncodedListener = new AudioEncoder.OnAudioEncodedListener() {
        @Override
        public void onFrameEncoded(byte[] encoded, long presentationTimeUs) {
            try{
                if(adtsHeader == null){
                    adtsHeader = new byte[7];
                    m_iAacObjectType = encoded[0]>> 3;
                    m_iSampleIndex = ((encoded[0] & 0x07) << 1) + (encoded[1] >> 7);
                    m_iChannels = (encoded[1] & 0x78) >> 3;
                    return;
                }

                int profile = m_iAacObjectType;  //AAC LC
                //39=MediaCodecInfo.CodecProfileLevel.AACObjectELD;
                int freqIdx = m_iSampleIndex;  //44.1KHz
                int chanCfg = m_iChannels;  //CPE
                int length = encoded.length;

                // fill in ADTS data
                adtsHeader[0] = (byte) 0xFF;
                adtsHeader[1] = (byte) 0xF9;
                adtsHeader[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
                adtsHeader[3] = (byte) (((chanCfg & 3) << 6) + (length >> 11));
                adtsHeader[4] = (byte) ((length & 0x7FF) >> 3);
                adtsHeader[5] = (byte) (((length & 7) << 5) + 0x1F);
                adtsHeader[6] = (byte) 0xFC;

                dataAfterEncoded.write(adtsHeader, 0, 7);
                dataAfterEncoded.write(encoded, 0, encoded.length);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    };

    private AudioDecoder.OnAudioDecodedListener mDecodedListener = new AudioDecoder.OnAudioDecodedListener() {
        @Override
        public void onFrameDecoded(byte[] decoded, long presentationTimeUs) {
            mAudioPlayer.play(decoded, 0, decoded.length);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_record_activity);
        initView();
        setUp();
        File file = new File(Environment.getExternalStorageDirectory() + OUTPUTDIR);
        if(!file.exists()){
            file.mkdirs();
        }

        try{
            dataAfterEncoded = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() +  OUTPUTDIR + outPutFileEncoded));
            dataBeforeEncoded = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + OUTPUTDIR + outPutFileUnEncoded));
        }catch (IOException e){

        }

    }

    private void initView() {
        txtBeginRecord = (TextView) findViewById(R.id.tv_begin_record);
        txtStopRecord = (TextView) findViewById(R.id.tv_stop_record);
        txtBeginPlay = (TextView) findViewById(R.id.tv_begin_play);
        txtStopPlay = (TextView) findViewById(R.id.tv_stop_play);
    }

    private void setUp() {
        txtBeginRecord.setOnClickListener(this);
        txtStopRecord.setOnClickListener(this);
        txtBeginPlay.setOnClickListener(this);
        txtStopPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_begin_record:
                beginRecord();
                break;
            case R.id.tv_stop_record:
                stopRecord();
                break;
            case R.id.tv_begin_play:
                startPlay();
                break;
            case R.id.tv_stop_play:
                stopPlay();
                break;
        }
    }

    private Runnable enCodeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording){
                mAudioEncoder.retrieve();
            }
            mAudioEncoder.close();
        }
    };

    private Runnable deCodeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isPlaying){
                mAudioDecoder.retrieve();
            }
            mAudioDecoder.close();
        }
    };
    private void beginRecord(){
        mLogger.info("beginRecord");
        if(null == mAudioCapturer){
            mAudioCapturer = new AudioCapturer();
            mAudioCapturer.setOnAudioFrameCapturedListener(mOnAudioFrameCapturedListener);
            mWavFileWriter = new WavFileWriter();
            try{
                mWavFileWriter.openFile(Environment.getExternalStorageDirectory() + OUTPUTDIR + "test.wav",
                        44100, 1, 16);
            }catch (IOException e){

            }
            mAudioEncoder = new AudioEncoder();
            mAudioEncoder.setAudioEncodedListener(mEncodedListener);
            mAudioEncoder.open();
            isRecording = true;
            mAudioCapturer.startCapture();
            new Thread(enCodeRunnable).start();
        }
    }

    private void stopRecord(){
        mLogger.info("stopRecord");
        isRecording = false;
        mAudioCapturer.stopCapture();
        mAudioEncoder.close();
        try{
            dataBeforeEncoded.close();
            dataAfterEncoded.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void startPlay(){
        mLogger.info("startPlay");
        mAudioPlayer = new AudioPlayer();
        mAudioPlayer.startPlayer();
        mAudioDecoder = new AudioDecoder();
        mAudioDecoder.open();
        mAudioDecoder.setAudioDecodedListener(mDecodedListener);
//        mWavFileReader = new WavFileReader();
//        try{
//            mWavFileReader.openFile(Environment.getExternalStorageDirectory()+OUTPUTDIR+outPutFile);
//        }catch (IOException e){
//            e.printStackTrace();
//            return;
//        }
        isPlaying = true;
        new Thread(deCodeRunnable).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte [] data = new byte[1024];
                while (mWavFileReader.readData(data, 0, 1024) > 0){
                    mAudioDecoder.decode(data, System.nanoTime() / 1000L);
                }
//                stopPlay();
            }
        }).start();
    }

    private void stopPlay(){
        mLogger.info("stopPlay");
        isPlaying = false;
        mAudioPlayer.stopPlayer();
        mAudioDecoder.close();
        try{
            mWavFileReader.closeFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
