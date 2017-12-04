package com.joker.ocean.ipc;
import com.joker.ocean.ipc.MidData;
import com.joker.ocean.ipc.IMsgCallback;

interface IPCTest
{
    boolean inSendMsgToServer(in MidData msg);
    void registerMsgCallback(in IMsgCallback callback);
    void unregisterMsgCallback(in IMsgCallback callback);
}