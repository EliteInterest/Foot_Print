package cn.gisdata.footprint.module.my.func.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gisdata.footprint.R;
import cn.gisdata.footprint.module.map.func.util.BaiduMapUtil;
import cn.gisdata.footprint.module.my.bean.MyfootMarkEntity;
import cn.gisdata.footprint.util.DateUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Xiangb on 2018/5/30.
 * 功能：
 */
public class FootListAdapter extends RecyclerView.Adapter<FootListAdapter.MyHolder> {

    private List<MyfootMarkEntity> dataList;
    private Context context;

    public FootListAdapter(List<MyfootMarkEntity> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_mark, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyfootMarkEntity entity = dataList.get(position);
        String time = entity.getName() == null ? "" : entity.getName();
        holder.myName.setText(time);
        String startTime = DateUtil.getDateFromMillis(entity.getStartTime());
        holder.myTime.setText(startTime);
        int visitcount = entity.getVisitVolume();
        holder.myVisitCount.setText(visitcount + "次访问");
        Glide.with(context)
                .load(BaiduMapUtil.getStaticBitmapPath(entity.getLongitude(), entity.getLatitude()))
                .centerCrop()
                .into(holder.myImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public TextView myName;
        public TextView myTime;
        public TextView myVisitCount;
        public ImageView myImage;

        public MyHolder(View itemView) {
            super(itemView);
            myName = itemView.findViewById(R.id.my_foot_name);
            myTime = itemView.findViewById(R.id.my_foot_time);
            myVisitCount = itemView.findViewById(R.id.my_foot_visitcount);
            myImage = itemView.findViewById(R.id.my_foot_des_img);
        }
    }

}
