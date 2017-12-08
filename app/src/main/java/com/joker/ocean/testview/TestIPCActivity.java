package com.joker.ocean.testview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.joker.ocean.R;
import com.joker.ocean.ipc.IMsgCallback;
import com.joker.ocean.ipc.IPCTest;
import com.joker.ocean.ipc.MidData;
import com.joker.ocean.ipc.SubProcessService;
import com.joker.ocean.selfview.OceanDraweeView;
import com.joker.pacific.component.BaseFragmentActivity;

/**
 * Created by joker on 17-12-4.
 * aidl进程间通信demo
 */

public class TestIPCActivity extends BaseFragmentActivity implements View.OnClickListener{
    private OceanDraweeView m_tvTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_webview);
        m_tvTestView = (OceanDraweeView) findViewById(R.id.tv_customview);
        m_tvTestView.setOnClickListener(this);
        startBindService();
    }

    private IMsgCallback.Stub mcallback = new IMsgCallback.Stub() {
        @Override
        public void onReplyMsg(MidData data) throws RemoteException {
            mLogger.info("onReplyMsg: " + data.getName() + ":  " + data.getContent());
        }
    };
    private ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mLogger.info("onServiceConnected");
            mbinder = IPCTest.Stub.asInterface(iBinder);
            try{
                mbinder.registerMsgCallback(mcallback);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mLogger.info("onServiceDisconnected");

        }
    };

    private void sendMessage() {
        if(mbinder == null){
            return;
        }
        MidData midata = new MidData();
        midata.setId(0);
        midata.setName("client joker");
        midata.setContent("hi server");
        try{
            mbinder.inSendMsgToServer(midata);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private IPCTest mbinder;
    @Override
    public void onClick(View v) {
        sendMessage();
    }

    private void startBindService(){
        this.bindService(new Intent(this, SubProcessService.class), mconnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mbinder != null){
            try{
                mbinder.unregisterMsgCallback(mcallback);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        unbindService(mconnection);
    }
}
