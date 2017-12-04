//
// Created by joker on 17-12-4.
//
#include "CAudioConfigParam.h"

#define JAUDIOCONFIGPARAM_CLASS_PATH "com.joker.multimedia.audio.AudioConfigParam"

CAudioConfigParam::CAudioConfigParam(JNIEnv *env, jobject instance) {
    m_pJAudioConfigParam = instance;
    initMethod(env);
}

CAudioConfigParam::~CAudioConfigParam() {

}

void CAudioConfigParam::setEnv(JNIEnv *env) {
    m_pEnv = env;
}

void CAudioConfigParam::initMethod(JNIEnv *env) {
    jclass cls = env->FindClass(JAUDIOCONFIGPARAM_CLASS_PATH);
    if(!cls){
        return;
    }
    jIsHardEncode =env->GetMethodID(cls, "isHardEncode", "()Z");
    jSetHardEncode =env->GetMethodID(cls, "setHardEncode", "(Z)V");
    jGetSampleRate =env->GetMethodID(cls, "getSampleRate", "()I");
    jSetSampleRate =env->GetMethodID(cls, "setSampleRate", "(I)V");
    jGetAudioBitrate =env->GetMethodID(cls, "getAudioBitrate", "()I");
    jSetAudioBitrate =env->GetMethodID(cls, "setAudioBitrate", "(I)V");
    jGetAudioChannelCount =env->GetMethodID(cls, "getAudioChannelCount", "()I");
    jSetAudioChannelCount =env->GetMethodID(cls, "setAudioChannelCount", "(I)V");
    jGetFileName =env->GetMethodID(cls, "getFileName", "()Ljava/lang/String;");
    jSetFileName =env->GetMethodID(cls, "setFileName", "(Ljava/lang/String;)V");
}

int CAudioConfigParam::getAudioBitrate() {
    return m_pEnv->CallIntMethod(m_pJAudioConfigParam, jGetAudioBitrate);
}

void CAudioConfigParam::setAudioBitrate(int audioBitrate) {
    m_pEnv->CallVoidMethod(m_pJAudioConfigParam, jSetAudioBitrate);
}

void CAudioConfigParam::setAudioChannelCount(int audioChannelCount) {
    m_pEnv->CallVoidMethod(m_pJAudioConfigParam, jSetAudioChannelCount);
}

int CAudioConfigParam::getAudioChannelCount() {
    return m_pEnv->CallIntMethod(m_pJAudioConfigParam, jGetAudioChannelCount);
}

void CAudioConfigParam::setSampleRate(int sampleRate) {
    m_pEnv->CallVoidMethod(m_pJAudioConfigParam, jSetSampleRate);
}

int CAudioConfigParam::getSampleRate() {
    return m_pEnv->CallIntMethod(m_pJAudioConfigParam, jGetSampleRate);
}

bool CAudioConfigParam::isHardEncode() {
    return m_pEnv->CallBooleanMethod(m_pJAudioConfigParam, jIsHardEncode);
}

void CAudioConfigParam::setHardEncode(bool hardEncode) {
    m_pEnv->CallVoidMethod(m_pJAudioConfigParam, jSetHardEncode);
}



