package com.autodownloadimages.ui.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autodownloadimages.R;
import com.autodownloadimages.base.adapter.BaseAdapter;
import com.autodownloadimages.model.MenuInfoModel;
import com.autodownloadimages.model.RecipeModel;
import com.autodownloadimages.utilites.DeviceScreenModel;
import com.autodownloadimages.utilites.ImageDownloadHelper;
import com.foji.downloadimage.DownloadImageHelper;

import java.util.List;

/**
 * @author Sunil kumar Yadav
 * @Since 25/5/18
 */
public class MainAdapter extends BaseAdapter {

    Context context;
    List<RecipeModel> list;

    public MainAdapter(Context context, List<RecipeModel> list) {
        isForDesign = false;
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder getViewHolder() {
        return new MyViewHolder(inflateLayout(R.layout.item_menu));
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    private class MyViewHolder extends BaseViewHolder {
        private RelativeLayout ll_layout;
        private ImageView ivImage;
        private TextView tv_name;
        public MyViewHolder(View view) {
            super(view);
            ll_layout =  view.findViewById(R.id.ll_layout);
            ivImage =  view.findViewById(R.id.iv_image);
            tv_name =  view.findViewById(R.id.tv_name);

            int width = DeviceScreenModel.getInstance().getWidth(0.50f);
            ll_layout.setLayoutParams(
                    DeviceScreenModel.getInstance()
                            .getRelativeLayoutParams(ll_layout, width, 3,3,3,3));
        }

        @Override
        public void setData(int position) {
            ll_layout.setTag(position);
            ll_layout.setOnClickListener(this);

            RecipeModel model = list.get(position);
            tv_name.setText(model.getName());
            DownloadImageHelper.getInstance().loadImage(itemView.getContext(), ivImage,
                    model.getImageUrl());
        }

        @Override
        public void onClick(View v) {
            performItemClick((Integer) v.getTag(), v);
        }
    }
}
