package com.joker.multimedia.audio;

import com.joker.multimedia.JniHelper;

/**
 * Created by joker on 17-12-3.
 */

public class AudioRecorder {
    private AudioConfigParam mParam;
    public AudioRecorder(AudioConfigParam param){
        this.mParam = param;
        JniHelper.nativeInitRecord(mParam);
    }

    public void start(){
        JniHelper.nativeStartRecord();
    }

    public void stop(){
        JniHelper.nativeStopRecord();
    }

    public void pause(boolean pause){
        JniHelper.nativePauseRecord(pause);
    }

    public void setVolume(float volume){
        JniHelper.nativeSetRecordVolume(volume);
    }
}
