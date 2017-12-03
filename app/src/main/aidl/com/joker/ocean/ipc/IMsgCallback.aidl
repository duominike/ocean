// IMsgCallback.aidl
package com.joker.ocean.ipc;
import com.joker.ocean.ipc.MidData;

interface IMsgCallback {
    void onReplyMsg(out MidData data);
}
