package com.joker.ocean.ipc;
import com.joker.ocean.ipc.MidData;
import com.joker.ocean.ipc.IMsgCallback;
interface IPCTest
{
    boolean inSendMsgToServer(in MidData msg);
    void registerMsgCallback(IMsgCallback callback);
    void unregisterMsgCallback(IMsgCallback callback);
}