//
// Created by joker on 17-12-3.
//


#ifndef OCEAN_DEV_AUDIORECORD_H_H
#define OCEAN_DEV_AUDIORECORD_H_H

#include <pthread.h>
#include <stdint.h>
#include "CAudioConfigParam.h"
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

#define BUFFERCOUNT 2
#define RECORDBUFFERSIZZE 2048
static int bufferInddex = 0;
static uint8_t s_recorderBuffer[BUFFERCOUNT][RECORDBUFFERSIZZE];

namespace multimedia {


    class IRecord_Callback {
    public:
        IRecord_Callback() {
        }

        virtual ~ IRecord_Callback() {
        }

        virtual void recordCallback(uint8_t *buffer, int size) = 0;
    };

    static void RecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context);

    class AudioRecord {
    public:
        friend void RecorderCallback(SLAndroidSimpleBufferQueueItf bq,
                                     void *context);

        AudioRecord(CAudioConfigParam *param);

        ~AudioRecord();

        bool init();

        void prepare();

        void release();

        void start();

        void stop();

        void pause(bool pause);

        int getState();

        void setCallback(IRecord_Callback *callback) {
            mCallback = callback;
        }

    private:
        IRecord_Callback *mCallback;
        CAudioConfigParam *audioConfigParam;
        volatile bool isInit;
        volatile bool isPause;
        volatile bool isStopped;
        // engine interfaces
        SLObjectItf openSLEngine;
        SLEngineItf openSLEngineItf;

        // recorder interfaces
        SLObjectItf recorderObject;
        SLRecordItf recorderItf;
        SLAndroidSimpleBufferQueueItf recorderBufferQueue;
    };
}
#endif //OCEAN_DEV_AUDIORECORD_H_H