// IMsgCallback.aidl
package com.joker.ocean.ipc;
import com.joker.ocean.ipc.MidData;

interface IMsgCallback {
    void onReplyMsg(in MidData data);
}
