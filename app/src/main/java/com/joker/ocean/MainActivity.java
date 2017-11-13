package com.joker.ocean;


import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.selfview.OceanDraweeView;
import com.joker.ocean.temp.LoadApkUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class MainActivity extends BaseActivity implements View.OnClickListener{
//    private OceanDraweeView m_tvTestView;
    private static final String TAG = "MainActivity";
    private ListView mListView;
    private List<ApplicationInfo> mInfoList;
    private ApkAdapter mApkAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_apks);
        mInfoList = new ArrayList<ApplicationInfo>();
        mApkAdapter = new ApkAdapter();
//        mListView.setAdapter(mApkAdapter);
//        m_tvTestView = (OceanDraweeView)findViewById(R.id.tv_customview);
//        m_tvTestView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        String url = "http://upcdn.images.live.51vv.com/moments/image/ad7f1cd51e4328c327386114471ff63e_b.webp";
//        m_tvTestView.setImageURI(Uri.parse(url));
    }

    private class ApkAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this, R.layout.item_apk_detail, null);
                viewHolder = new ViewHolder();
                viewHolder.icon = (OceanDraweeView) convertView.findViewById(R.id.ocean_icon);
                viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_app_name);
                viewHolder.tvPkgName = (TextView)convertView.findViewById(R.id.tv_app_pkg_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            mLogger.info("packagename: " + mInfoList.get(position).packageName);
            mLogger.info("datadir: " + mInfoList.get(position).dataDir);
            mLogger.info("appName: " + mInfoList.get(position).loadLabel(MainActivity.this.getPackageManager()));
            mLogger.info("processName: " + mInfoList.get(position).processName);

            return convertView;
        }

        class ViewHolder{
            OceanDraweeView icon;
            TextView tvName;
            TextView tvPkgName;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadApkUtils.getInstalledApks(this)
                .subscribe(new Observer<ApplicationInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ApplicationInfo applicationInfo) {
                        mInfoList.add(applicationInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mListView.setAdapter(mApkAdapter);
                    }
                });
    }
}
