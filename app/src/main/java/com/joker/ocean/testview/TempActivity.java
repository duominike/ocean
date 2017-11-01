package com.joker.ocean.testview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.joker.ocean.R;
import com.joker.pacific.component.BaseFragmentActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-10-25.
 */

public class TempActivity extends BaseFragmentActivity {
    private Disposable mDisposable;
    private byte[] ss = new byte[1024 * 1024 * 50];
    private ObservableOnSubscribe<Object> obj = new ObservableOnSubscribe<Object>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
            int num = 0;
            while (num < 50) {
                num++;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
            Log.i("TempActivity", "call once");
            e.onNext("finished");
            e.onComplete();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_customview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TempActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        test();
    }

    private void test() {
        mDisposable = Observable.create(obj).
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i("TempActivity", "accept: " + o);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mDisposable.isDisposed()) {
            mLogger.info("call dispose");
            mDisposable.dispose();
        }
    }
}
