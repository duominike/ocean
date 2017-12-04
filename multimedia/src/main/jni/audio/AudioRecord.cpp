//
// Created by joker on 17-12-3.
//

#include <pthread.h>
#include <unistd.h>
#include <string.h>
#include <android/log.h>
#include "AudioRecord.h"
#include <stdio.h>

#define TAG "AudioRecord"
namespace multimedia {
    AudioRecord::AudioRecord() : isInit(false), isPause(false), isStopped(false),
                                 mCallback(NULL), m_ThreadId(0){
    }

    AudioRecord::~AudioRecord() {
    }


    bool AudioRecord::init() {
        isStopped = false;
        int res = pthread_mutex_init(&tasks_mutex, NULL);
        if (res != 0) {
            isInit = false;
            return false;
        }
        res = pthread_cond_init(&tasks_cond, NULL);
        if (res != 0) {
            isInit = false;
            return false;
        }
        isInit = true;
        __android_log_print(ANDROID_LOG_INFO, TAG, "init success");
        return true;

    }

    void AudioRecord::start() {
        // 参数依次是：创建的线程id，线程参数，调用的函数，传入的函数参数
        if(!isInit){
            return;
        }
        int res = pthread_create(&m_ThreadId, NULL, (void *(*)(void *)) &AudioRecord::run, this);
        if (0 != res) {
            __android_log_print(ANDROID_LOG_INFO, TAG, "pthread create error");
        } else {
            __android_log_print(ANDROID_LOG_INFO, TAG, "pthread create success");
        }
    }

    void AudioRecord::run() {
        __android_log_print(ANDROID_LOG_INFO, TAG, "run");
        if(!isInit){
            return;
        }
        while (!isStopped) {
            pthread_mutex_lock(&tasks_mutex);
            while (isPause) {
                pthread_cond_wait(&tasks_cond, &tasks_mutex);
            }
            isPause = false;
            pthread_mutex_unlock(&tasks_mutex);
            __android_log_print(ANDROID_LOG_INFO, TAG, "pthread run once");
            sleep(10);
        }
        __android_log_print(ANDROID_LOG_INFO, TAG, "pthread finish");
//        pthread_exit((void *)"pthread exit");
    }

    void AudioRecord::stop() {
        if(!isInit){
            return;
        }
        __android_log_print(ANDROID_LOG_INFO, TAG, "pthread stop");
        isStopped = true;
        // 等待线程结束，阻塞操作
        void *status;
        pthread_join(m_ThreadId, &status);
//        printf("thread exit return:%s\n", (char*)&status);
    }

    void AudioRecord::pause(bool pause) {
        if(!isInit){
            return;
        }
        pthread_mutex_lock(&tasks_mutex);
        if (!pause) {
            isPause = false;
            __android_log_print(ANDROID_LOG_INFO, TAG, "resume thread run");
            pthread_cond_signal(&tasks_cond);
        } else {
            isPause = true;
            __android_log_print(ANDROID_LOG_INFO, TAG, "pause thread");
        }
        pthread_mutex_unlock(&tasks_mutex);
    }

    int AudioRecord::getState() {
        if(!isInit){
            return 0;
        }
        return 0;
    }

    void AudioRecord::release() {

    }
}
