package com.app.footprint.module.my.func.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.footprint.R;
import com.app.footprint.module.map.func.util.BaiduMapUtil;
import com.app.footprint.module.my.bean.MyFootRouteEntity;
import com.app.footprint.util.DateUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Xiangb on 2018/5/30.
 * 功能：
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.MyHolder> {

    private List<MyFootRouteEntity> dataList;
    private Context context;

    public RouteListAdapter(List<MyFootRouteEntity> dataList) {
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
        MyFootRouteEntity entity = dataList.get(position);
        String time = entity.getName() == null ? "" : entity.getName();
        holder.myName.setText(time);
        String startTime = DateUtil.getDateFromMillis(entity.getStartTime());
        holder.myTime.setText(startTime);
        int visitcount = entity.getVisitVolume();
        holder.myVisitCount.setText(visitcount + "次访问");
        List<List<Double>> points = entity.getPointPosition();

        String bitmap = "";
        double startLongitude = 0;
        double startLatitude = 0;
        double endLongitude = 0;
        double endLatitude = 0;
        try {
            if (points.size() >= 2) {
                startLatitude = points.get(0).get(0);
                startLongitude = points.get(0).get(1);
                endLatitude = points.get(points.size() - 1).get(0);
                endLongitude = points.get(points.size() - 1).get(1);
                bitmap = BaiduMapUtil.getStaticBitmapPath(startLongitude, startLatitude, endLongitude, endLatitude);
            } else if (points.size() > 0) {
                if (points.get(0).size() >= 2) {
                    startLatitude = points.get(0).get(0);
                    startLongitude = points.get(0).get(1);
                    bitmap = BaiduMapUtil.getStaticBitmapPath(startLongitude, startLatitude);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load(bitmap)
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
