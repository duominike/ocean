//
// Created by joker on 17-12-3.
//
#include "AudioRecord.h"
#include <pthread.h>
#include <unistd.h>
#include <android/log.h>

bool AudioRecord::init(){
    isStopped = false;
    pthread_mutex_init(&tasks_mutex, NULL);
    pthread_cond_init( &tasks_cond, NULL );
}

void AudioRecord::start(){
    // 参数依次是：创建的线程id，线程参数，调用的函数，传入的函数参数
    int res = pthread_create(&m_ThreadId, NULL, (void* (*)(void *))&AudioRecord::run, this);
    if(0 != res){
        __android_log_print(ANDROID_LOG_INFO, TAG, "pthread create error");
    }else{
        __android_log_print(ANDROID_LOG_INFO, TAG, "pthread create success");
    }
}

void AudioRecord::run(){
    while(!isStopped){
        pthread_mutex_lock(&tasks_mutex);
        while(!isPause){
            pthread_cond_wait(&tasks_cond, &tasks_mutex);
        }
        pthread_mutex_unlock(&tasks_mutex);
        __android_log_print(ANDROID_LOG_INFO, TAG, "pthread run once");
        sleep(1000);
    }
}

void AudioRecord::stop(){
    isStopped = true;
}

void AudioRecord::pause(bool pause){
    pthread_mutex_lock(&tasks_mutex);
    if(!pause){
        __android_log_print(ANDROID_LOG_INFO, TAG, "resume thread run");
        pthread_cond_signal(&tasks_cond);
    }else{
        isPause = true;
         __android_log_print(ANDROID_LOG_INFO, TAG, "pause thread");
    }
    pthread_mutex_unlock(&tasks_mutex);
}
