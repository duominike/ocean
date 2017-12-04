package com.joker.multimedia;

import com.joker.multimedia.audio.AudioConfigParam;

/**
 * Created by joker on 17-12-3.
 */

public class JniHelper {
    static {
        System.loadLibrary("multimedia");
    }
    //录音
    public native static void nativeInitRecord(AudioConfigParam param);
    public native static void nativeStartRecord();
    public native static void nativeStopRecord();
    public native static void nativePauseRecord(boolean pause);
    public native static void nativeSetRecordVolume(float volume);

}
