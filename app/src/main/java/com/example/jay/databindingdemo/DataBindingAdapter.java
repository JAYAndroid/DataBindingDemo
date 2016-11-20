package com.example.jay.databindingdemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Random;

/**
 * Created by JAY on 2016/11/19.
 */

public class DataBindingAdapter extends RecyclerView.Adapter<DataBindingViewHolder> {
    private List<User> dataSources;
    private LayoutInflater mLayoutInflater;

    public DataBindingAdapter(Context context, List<User> dataSources) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataSources = dataSources;

    }

    @Override
    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.recycle_item, parent, false);
        return new DataBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.item, dataSources.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataSources.size();
    }

    public void addItem(User user) {
        Random r = new Random();
        int index = r.nextInt(dataSources.size() + 1);
        dataSources.add(index, user);
        notifyItemInserted(index);
    }

    public void delItem() {
        if (dataSources.size() > 0) {
            Random r = new Random();
            int index = r.nextInt(dataSources.size());
            dataSources.remove(index);
            notifyItemRemoved(index);
        }
    }
}
