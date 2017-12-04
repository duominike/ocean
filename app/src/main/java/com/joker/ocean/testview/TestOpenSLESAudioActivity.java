package com.joker.ocean.testview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.joker.multimedia.audio.AudioRecorder;
import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;

/**
 * Created by joker on 17-12-4.
 */

public class TestOpenSLESAudioActivity extends BaseFragmentActivity implements View.OnClickListener{
    private TextView txtBeginRecord;
    private TextView txtPauseRecord;
    private TextView txtResumeRecord;
    private TextView txtStopRecord;
    private AudioRecorder mAudioRecorder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_layout);
        initView();
        setUp();
        mAudioRecorder = new AudioRecorder(null);
    }

    private void initView() {
        txtBeginRecord = (TextView) findViewById(R.id.tv_begin_record);
        txtPauseRecord = (TextView) findViewById(R.id.tv_pause_record);
        txtResumeRecord = (TextView) findViewById(R.id.tv_resume_record);
        txtStopRecord = (TextView) findViewById(R.id.tv_stop_record);
    }

    private void setUp() {
        txtBeginRecord.setOnClickListener(this);
        txtPauseRecord.setOnClickListener(this);
        txtResumeRecord.setOnClickListener(this);
        txtStopRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_begin_record:
                mAudioRecorder.start();
                break;
            case R.id.tv_pause_record:
                mAudioRecorder.pause(true);
                break;
            case R.id.tv_resume_record:
                mAudioRecorder.pause(false);
                break;
            case R.id.tv_stop_record:
                mAudioRecorder.stop();
                break;
        }
    }
}
