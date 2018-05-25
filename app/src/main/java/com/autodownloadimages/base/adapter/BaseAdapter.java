package com.autodownloadimages.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autodownloadimages.utilites.ItemClickSupport;


public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    public boolean isForDesign = true;
    LayoutInflater layoutInflater;

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public View inflateLayout(int layoutId) {
        return getLayoutInflater().inflate(layoutId, null);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        return getViewHolder();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return isForDesign ? 5 : getDataCount();
    }

    public abstract BaseViewHolder getViewHolder();

    public abstract int getDataCount();

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setData(int position);

        public void performItemClick(int position, View view) {
            ItemClickSupport itemClickSupport = ItemClickSupport.getFrom(itemView);
            if (itemClickSupport != null) {
                itemClickSupport.getmOnItemClickListener()
                        .onItemClicked(itemClickSupport.getmRecyclerView(), position, view);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
