package com.joker.ocean.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joker.ocean.databinding.ItemApkDetailBinding;

/**
 * Created by joker on 2018/3/29.
 */

public class OceanDataBindingViewHolder extends RecyclerView.ViewHolder{
    private ItemApkDetailBinding mbinding;
    public OceanDataBindingViewHolder(ItemApkDetailBinding binding) {
        super(binding.getRoot());
        this.mbinding = binding;
    }

    public ItemApkDetailBinding getBinding(){
        return this.mbinding;
    }
}
