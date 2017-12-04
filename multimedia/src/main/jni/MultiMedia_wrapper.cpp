//
// Created by joker on 17-12-3.
//
#include "MultiMedia_wrapper.h"
#include <android/log.h>
#include "audio/AudioRecord.h"
#include "audio/CAudioConfigParam.h"
/*
 * Class:     com_joker_multimedia_JniHelper
 * Method:    nativeStartRecord
 * Signature: ()V
 */

namespace multimedia {
    AudioRecord *gp_record = NULL;
}

JNIEXPORT void JNICALL Java_com_joker_multimedia_JniHelper_nativeInitRecord
        (JNIEnv *env, jclass, jobject instance){
    using namespace multimedia;
    gp_record = new AudioRecord(new CAudioConfigParam(env, instance));
}

JNIEXPORT void JNICALL Java_com_joker_multimedia_JniHelper_nativeStartRecord
        (JNIEnv *, jclass) {
    using namespace multimedia;
    if (NULL != gp_record) {
        bool initSuccess = gp_record->init();
        if (!initSuccess) {
            __android_log_print(ANDROID_LOG_INFO, TAG, "init Error");
        }else{
            gp_record->start();
        }
    }
}

/*
 * Class:     com_joker_multimedia_JniHelper
 * Method:    nativeStopRecord
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_joker_multimedia_JniHelper_nativeStopRecord
        (JNIEnv *, jclass) {
    using namespace multimedia;
    if (NULL != gp_record) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "stop record");
        gp_record->stop();
    }
}

/*
 * Class:     com_joker_multimedia_JniHelper
 * Method:    nativePauseRecord
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_joker_multimedia_JniHelper_nativePauseRecord
        (JNIEnv *, jclass, jboolean pause) {
    using namespace multimedia;
    if (NULL != gp_record) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "pause record");
        gp_record->pause(pause);
    }
}

/*
 * Class:     com_joker_multimedia_JniHelper
 * Method:    nativeSetRecordVolume
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_com_joker_multimedia_JniHelper_nativeSetRecordVolume
        (JNIEnv *, jclass, jfloat volume) {

}
