package cn.gisdata.footprint.module.my.func.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.module.foot.bean.DraftFootBean;

/**
 * Created by fxs on 2018/5/30.
 * 功能：
 */
public class DraftListAdapter extends RecyclerView.Adapter<DraftListAdapter.MyHolder> {

    private List<DraftFootBean> dataList;
    private Context context;
    private Activity activity;
    private OnUplaodListener uplaodListener;

    public DraftListAdapter(List<DraftFootBean> dataList, Activity activity) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_update, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        DraftFootBean entity = dataList.get(position);
        String name = entity.getName() == null ? "" : entity.getName();
        holder.tvName.setText(entity.getName());
        holder.tvTime.setText(entity.getDate());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvTime;
        public TextView tvUpload;

        public MyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_draft_name);
            tvTime = itemView.findViewById(R.id.tv_draft_date);
            tvUpload = itemView.findViewById(R.id.tv_draft_upload);
            tvUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uplaodListener != null) {
                        uplaodListener.onUpload(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setUplaodListener(OnUplaodListener uplaodListener) {
        this.uplaodListener = uplaodListener;
    }

    public interface OnUplaodListener {
        void onUpload(int position);
    }

}
