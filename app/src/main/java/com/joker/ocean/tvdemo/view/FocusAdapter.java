package com.joker.ocean.tvdemo.view;

/**
 * Created by joker on 17-3-26.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joker.ocean.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joker.ocean.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joker.ocean.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.joker.ocean.tvdemo.model.DemoModle;
import java.util.List;

public class FocusAdapter extends BaseAdapter {

    private List<DemoModle> mLists;

    private Context mContext;

    public FocusAdapter(List<DemoModle> mLists, Context mContext) {
        super();
        this.mLists = mLists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mLists.size();
    }

    @Override
    public DemoModle getItem(int position) {
        // TODO Auto-generated method stub
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return -1;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View view  = mInflater.inflate(
				R.layout.layout_focus_item,parent);
		ImageView mImageView = (ImageView) view.findViewById(R.id.photo);
		TextView mTextView = (TextView) view.findViewById(R.id.title);
		mImageView.setBackgroundResource(mLists.get(position).getImage());
		mTextView.setText(mLists.get(position).getName());
        return view;
    }

}
