//
// Created by joker on 17-12-4.
//

#ifndef OCEAN_CAUDIOCONFIGPARMA_H
#define OCEAN_CAUDIOCONFIGPARMA_H

#include <jni.h>

class CAudioConfigParam {
public:
    CAudioConfigParam(JNIEnv *env, jobject instance);

    ~CAudioConfigParam();

    void setEnv(JNIEnv *env);

public:
    bool isHardEncode();

    void setHardEncode(bool hardEncode);

    int getSampleRate();

    void setSampleRate(int sampleRate);

    int getAudioBitrate();

    void setAudioBitrate(int audioBitrate);

    int getAudioChannelCount();

    void setAudioChannelCount(int audioChannelCount);


private:
    void initMethod(JNIEnv* env);
    JNIEnv* m_pEnv;
    jmethodID jIsHardEncode;
    jmethodID jSetHardEncode;
    jmethodID jGetSampleRate;
    jmethodID jSetSampleRate;
    jmethodID jGetAudioBitrate;
    jmethodID jSetAudioBitrate;
    jmethodID jGetAudioChannelCount;
    jmethodID jSetAudioChannelCount;
    jmethodID jGetFileName;
    jmethodID jSetFileName;

    jobject m_pJAudioConfigParam;

};

#endif //OCEAN_CAUDIOCONFIGPARMA_H
