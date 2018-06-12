package cn.gisdata.footprint.module.foot.func.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.app.ConstStrings;
import cn.gisdata.footprint.module.foot.bean.FootFileBean;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by fxs on 2018/5/23.
 * 功能：
 */

public class FootPicAdapter extends RecyclerView.Adapter<FootPicAdapter.MyHolder> {

    private Context context;
    private List<FootFileBean.PicBean> picChildBeans;
    private OnPicDeleteListener onDeleteListener;

    public FootPicAdapter(Context context, List<FootFileBean.PicBean> picChildBeans) {
        this.context = context;
        this.picChildBeans = picChildBeans;
    }

    public void setOnDeleteListener(OnPicDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_pic, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (holder.getItemViewType() == 0) {//add
            holder.ivPic.setBackground(ContextCompat.getDrawable(context, R.mipmap.foot_pic_add));
            holder.ivDelete.setVisibility(View.GONE);
        } else {//pic
            FootFileBean.PicBean picChildBean = picChildBeans.get(position);
            Glide.with(context)
                    .load(new File(ConstStrings.getCachePath() + picChildBean.getPath()))
                    .centerCrop()
                    .crossFade()
                    .into(holder.ivPic);
            holder.ivDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return picChildBeans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == picChildBeans.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    public interface OnPicDeleteListener {
        void onDelete(int position);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private ImageView ivPic, ivDelete;

        public MyHolder(View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_foot_pic);
            ivDelete = itemView.findViewById(R.id.iv_foot_pic_delete);
            ivDelete.setOnClickListener(v -> {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(getAdapterPosition());
                }
            });
        }
    }
}
