//
// Created by joker on 17-12-3.
//

#include <pthread.h>
#include <unistd.h>
#include <string.h>
#include <android/log.h>
#include "AudioRecord.h"
#include <stdio.h>
#include <assert.h>

#define TAG "AudioRecord"

namespace multimedia {
    static void RecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
        assert(NULL != context);
        AudioRecord *pRecorder = (AudioRecord *) context;

        if (pRecorder->isPause) {
            memset(s_recorderBuffer[bufferInddex], 0, RECORDBUFFERSIZZE);
        }

        pRecorder->mCallback->recordCallback(s_recorderBuffer[bufferInddex], RECORDBUFFERSIZZE);
        (*bq)->Enqueue(bq, s_recorderBuffer[bufferInddex], RECORDBUFFERSIZZE);
        bufferInddex = !bufferInddex;
    }


    AudioRecord::AudioRecord(CAudioConfigParam *param) : isInit(false), isPause(false),
                                                         isStopped(false),
                                                         mCallback(NULL) {
        audioConfigParam = param;
    }

    AudioRecord::~AudioRecord() {
    }


    bool AudioRecord::init() {
        isStopped = false;
        ///////////////////////////////////////////////////////////
        SLresult result;
        // 创建Audio Engine
        SLEngineOption EngineOption[] = {(SLuint32) SL_ENGINEOPTION_THREADSAFE,
                                         (SLuint32) SL_BOOLEAN_TRUE};
        result = slCreateEngine(&openSLEngine, 1, EngineOption, 0, NULL, NULL);
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG, "slEngine create error");
            isInit = false;
            return false;
        }
        // 初始化上一步得到的openSLEngine
        result = (*openSLEngine)->Realize(openSLEngine, SL_BOOLEAN_FALSE);
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG, "slEngine realize error");
            isInit = false;
            return false;
        }
        // 获取SLEngine接口对象，后续的操作将使用这个对象
        result = (*openSLEngine)->GetInterface(openSLEngine, SL_IID_ENGINE, &openSLEngineItf);

        if (SL_RESULT_SUCCESS != result) {
            isInit = false;
            __android_log_print(ANDROID_LOG_ERROR, TAG, "get slEngineItf error");
            return false;
        }
        // 配置音频源
        SLDataLocator_IODevice deviceInputLocator = {SL_DATALOCATOR_IODEVICE,
                                                     SL_IODEVICE_AUDIOINPUT,
                                                     SL_DEFAULTDEVICEID_AUDIOINPUT, NULL};
        SLDataSource inputSource = {&deviceInputLocator, NULL};

        // 配置音频参数
        SLDataLocator_AndroidSimpleBufferQueue inputLocator = {
                SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, BUFFERCOUNT};

        SLDataFormat_PCM inputFormat = {SL_DATAFORMAT_PCM, 2,
                                        SL_SAMPLINGRATE_44_1,
                                        SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
                                        SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
                                        SL_BYTEORDER_LITTLEENDIAN};

        SLDataSink inputSink = {&inputLocator, &inputFormat};

        // 创建录音机
        const SLInterfaceID inputInterfaces[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
        const SLboolean requireds[1] = {SL_BOOLEAN_TRUE};
        result = (*openSLEngineItf)->CreateAudioRecorder(openSLEngineItf, &recorderObject,
                                                         &inputSource, &inputSink, 1,
                                                         inputInterfaces,
                                                         requireds);
        if (SL_RESULT_SUCCESS != result) {
            isInit = false;
            __android_log_print(ANDROID_LOG_ERROR, TAG, "CreateAudioRecorder error");
            return false;
        }
        // realize the audio recorder
        result = (*recorderObject)->Realize(recorderObject,
                                            SL_BOOLEAN_FALSE); //SL_BOOLEAN_TRUE  SL_BOOLEAN_FALSE
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG, "Realize Recorder error");
            isInit = false;
            return false;
        }

        // get the record interface
        result = (*recorderObject)->GetInterface(recorderObject, SL_IID_RECORD,
                                                 &recorderItf);
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG, "GetInterface Recorder error");
            isInit = false;
            return false;
        }


        // get the buffer queue interface
        result = (*recorderObject)->GetInterface(recorderObject,
                                                 SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
                                                 &recorderBufferQueue);
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG,
                                "GetInterface recorderBufferQueue error.  SLresult");
            isInit = false;
            return false;
        }

        // register callback on the buffer queue
        result = (*recorderBufferQueue)->RegisterCallback(recorderBufferQueue,
                                                          RecorderCallback, this);
        if (SL_RESULT_SUCCESS != result) {
            __android_log_print(ANDROID_LOG_ERROR, TAG,
                                "RegisterCallback Recorder error.  SLresult");
            isInit = false;
            return false;
        }

        ///////////////////////////////////////////////////////////
        isInit = true;
        __android_log_print(ANDROID_LOG_INFO, TAG, "init success");
        return true;

    }

    void AudioRecord::prepare() {
        if (!isInit) {
            return;
        }
        // 设置为录制状态
        SLresult result;
        result = (*recorderItf)->SetRecordState(recorderItf,
                                                SL_RECORDSTATE_STOPPED);
        result = (*recorderBufferQueue)->Clear(recorderBufferQueue);
        // enqueue an empty buffer to be filled by the recorder
        // (for streaming recording, we would enqueue at least 2 empty buffers to start things off)
        result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue,
                                                 s_recorderBuffer[0], RECORDBUFFERSIZZE);
        result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue,
                                                 s_recorderBuffer[1], RECORDBUFFERSIZZE);
    }

    void AudioRecord::start() {

        if (!isInit) {
            return;
        }

        SLresult result;
        result = (*recorderItf)->SetRecordState(recorderItf,
                                                SL_RECORDSTATE_RECORDING);
        assert(SL_RESULT_SUCCESS == result);
        (void) result;
    }


    void AudioRecord::stop() {
        if (!isInit) {
            return;
        }

        SLresult result;

        result = (*recorderItf)->SetRecordState(recorderItf,
                                                SL_RECORDSTATE_STOPPED);
        assert(SL_RESULT_SUCCESS == result);
        (void) result;
        __android_log_print(ANDROID_LOG_INFO, TAG, "AudioRecord Stopped");
    }

    void AudioRecord::pause(bool pause) {
        if (!isInit) {
            return;
        }
        SLresult result;

        if (pause) {
            result = (*recorderItf)->SetRecordState(recorderItf,
                                                    SL_RECORDSTATE_PAUSED);
        } else {
            result = (*recorderItf)->SetRecordState(recorderItf,
                                                    SL_RECORDSTATE_RECORDING);
        }
        assert(SL_RESULT_SUCCESS == result);
        (void) result;
        __android_log_print(ANDROID_LOG_INFO, TAG, "RoomRecord Stopped");
        isPause = pause;
    }

    int AudioRecord::getState() {
        SLuint32 state;
        SLresult result;
        if (!isInit) {
            return SL_RECORDSTATE_STOPPED;
        }
        result = (*recorderItf)->GetRecordState(recorderItf, &state);
        assert(SL_RESULT_SUCCESS == result);
        return state;
    }

    void AudioRecord::release() {
        if (getState() != SL_RECORDSTATE_STOPPED) {
//            LOGW("call release, but the state is not SL_PLAYSTATE_STOPPED");
            stop();
        }

        //destroy buffer queue audio recorder object, and invalidate all associated interfaces
        if (recorderObject != NULL) {
            (*recorderObject)->Destroy(recorderObject);
            recorderObject = NULL;
            recorderItf = NULL;
            recorderBufferQueue = NULL;
        }

        // destroy engine object, and invalidate all associated interfaces
        if (openSLEngine != NULL) {
            (*openSLEngine)->Destroy(openSLEngine);
            openSLEngine = NULL;
            openSLEngineItf = NULL;
        }
        isInit = false;
        __android_log_print(ANDROID_LOG_INFO, TAG, "RoomRecord stop release success");
    }
}
