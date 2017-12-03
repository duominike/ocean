package com.joker.ocean.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.joker.pacific.log.Logger;

/**
 * Created by joker on 17-12-3.
 */

public class SubProcessService extends Service {
    private Logger log = Logger.getLogger(SubProcessService.class);
    private BnServiceBinder mbinder = new BnServiceBinder();
    private IMsgCallback msgCallback = null;
    private class BnServiceBinder extends IPCTest.Stub{
        @Override
        public boolean inSendMsgToServer(MidData msg) throws RemoteException {
            log.info("inSendMsgToServer: " + msg.getName() + ": " + msg.getContent());

            if(msgCallback != null){
                MidData midData = new MidData();
                midData.setId(1);
                midData.setName("Joker Server");
                midData.setContent("hi client: " + msg.getName());
                msgCallback.onReplyMsg(midData);
            }
            return true;
        }

        @Override
        public void registerMsgCallback(IMsgCallback callback) throws RemoteException {
            log.info("registerMsgCallback");
            msgCallback = callback;
        }

        @Override
        public void unregisterMsgCallback(IMsgCallback callback) throws RemoteException {
            log.info("unregisterMsgCallback");
            msgCallback = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log.info("onBind");
        return mbinder;
    }

}
