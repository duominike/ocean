package com.joker.ocean.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.joker.ocean.R;

/**
 * Created by joker on 18-1-14.
 */

public class MomentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    public MomentsAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            return new ViewHolder(View.inflate(mContext, R.layout.rainbow_header_layout, null));
        }else{
            return new ViewHolder(View.inflate(mContext,
                    R.layout.rainbow_header_layout, null));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bindData();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return 100;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }

        void bindData(){

        }
    }
}
