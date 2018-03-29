package com.joker.ocean.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joker.ocean.R;
import com.joker.ocean.databinding.ItemApkDetailBinding;
import com.joker.ocean.testview.testmodel.App;

/**
 * Created by joker on 2018/3/29.
 */

public class OceanDataBindingAdapter extends RecyclerView.Adapter<OceanDataBindingViewHolder> {
    private Activity mactivity;
    private ObservableArrayList<App> items = new ObservableArrayList<>();
    private ListChangedCallback itemsChangeCallback;

    public OceanDataBindingAdapter(Activity activity) {
        this.mactivity = activity;
        this.itemsChangeCallback = new ListChangedCallback();
    }

    public ObservableArrayList<App> getItems() {
        return items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.items.addOnListChangedCallback(itemsChangeCallback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.items.removeOnListChangedCallback(itemsChangeCallback);
    }

    //region 处理数据集变化
    protected void onChanged(ObservableArrayList<App> newItems) {
        resetItems(newItems);
        notifyDataSetChanged();
    }


    protected void onItemRangeChanged(ObservableArrayList<App> newItems, int positionStart, int itemCount) {
        resetItems(newItems);
        notifyItemRangeChanged(positionStart, itemCount);
    }

    protected void onItemRangeInserted(ObservableArrayList<App> newItems, int positionStart, int itemCount) {
        resetItems(newItems);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    protected void onItemRangeMoved(ObservableArrayList<App> newItems) {
        resetItems(newItems);
        notifyDataSetChanged();
    }

    protected void onItemRangeRemoved(ObservableArrayList<App> newItems, int positionStart, int itemCount) {
        resetItems(newItems);
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    protected void resetItems(ObservableArrayList<App> newItems) {
        this.items = newItems;
    }


    @Override
    public OceanDataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemApkDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mactivity),
                R.layout.item_apk_detail, parent, false);
        return new OceanDataBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OceanDataBindingViewHolder holder, int position) {
        holder.getBinding().setApp(items.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<App>> {
        @Override
        public void onChanged(ObservableArrayList<App> newItems) {
            OceanDataBindingAdapter.this.onChanged(newItems);
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<App> newItems, int i, int i1) {
            OceanDataBindingAdapter.this.onItemRangeChanged(newItems, i, i1);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<App> newItems, int i, int i1) {
            OceanDataBindingAdapter.this.onItemRangeInserted(newItems, i, i1);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<App> newItems, int i, int i1, int i2) {
            OceanDataBindingAdapter.this.onItemRangeMoved(newItems);
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<App> sender, int positionStart, int itemCount) {
            OceanDataBindingAdapter.this.onItemRangeRemoved(sender, positionStart, itemCount);
        }
    }


}






