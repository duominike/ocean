//
// Created by joker on 17-12-3.
//
#include <pthread.h>

#ifndef OCEAN_DEV_AUDIORECORD_H_H
#define OCEAN_DEV_AUDIORECORD_H_H

#endif //OCEAN_DEV_AUDIORECORD_H_H

#define TAG "AudioRecord"

class IRecord_Callback{
public:
    IRecord_Callback(){
    }
    virtual ~ IRecord_Callback(){
    }
    virtual void recordCallback(uint8_t *buffer, int size) = 0;
};

class AudioRecord {
public:
    AudioRecord();
    ~AudioRecord();
    bool init();
    void release();
    void start();
    void stop();
    void pause(bool pause);
    int getState();
    void setCallback(IRecord_Callback *callback){
        mCallback = callback;
    }
private:
    IRecord_Callback * mCallback;
    pthread_t m_ThreadId;
    volatile bool isInit;
    volatile bool isPause;
    volatile bool isStopped;
    pthread_mutex_t &tasks_mutex;
    pthread_cond_t &tasks_cond;
    void run();
};