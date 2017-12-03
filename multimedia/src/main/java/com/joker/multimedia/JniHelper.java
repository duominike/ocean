package com.joker.multimedia;

/**
 * Created by joker on 17-12-3.
 */

public class JniHelper {
    static {

    }
    //录音
    public native static void nativeStartRecord();
    public native static void nativeStopRecord();
    public native static void nativePauseRecord(boolean pause);
    public native static void nativeSetRecordVolume(float volume);

}
