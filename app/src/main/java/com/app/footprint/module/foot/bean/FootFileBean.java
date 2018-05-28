package com.app.footprint.module.foot.bean;


import com.esri.core.geometry.Point;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Xiangb on 2018/5/22.
 * 功能：
 */

public class FootFileBean implements Serializable {

    private String id;
    private String point;
    private String description;
    private String locationName;
    private List<PicBean> picPaths;
    private String vedioShootPath;
    private String vedioPath;
    private String textName;
    private String recordTime;
    private String commitTime;
    private boolean isRoute = false;

    public enum Type {
        Camera,
        Vedio,
        Text
    }

    private Type type = Type.Camera;

    public FootFileBean(){
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description, String locationName, List<PicBean> picPaths) {
        this.point = point;
        this.description = description;
        this.picPaths = picPaths;
        this.locationName = locationName;
        this.type = Type.Camera;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description, String locationName, String vedioShootPath, String vedioPath) {
        this.point = point;
        this.description = description;
        this.vedioShootPath = vedioShootPath;
        this.vedioPath = vedioPath;
        this.locationName = locationName;
        this.type = Type.Vedio;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description, String locationName, String textName) {
        this.point = point;
        this.description = description;
        this.textName = textName;
        this.locationName = locationName;
        this.type = Type.Text;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPoint() {
        return point;
    }

    public Point getMapPoint() {
        Point mapPoint = null;
        try {
            String[] pointArray = point.split(",");
            if (pointArray.length == 2) {
                mapPoint = new Point(Double.parseDouble(pointArray[0]), Double.parseDouble(pointArray[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapPoint;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getVedioShootPath() {
        return vedioShootPath;
    }

    public void setVedioShootPath(String vedioShootPath) {
        this.vedioShootPath = vedioShootPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PicBean> getPicPaths() {
        return picPaths;
    }

    public void setPicPaths(List<PicBean> picPaths) {
        this.picPaths = picPaths;
    }

    public String getVedioPath() {
        return vedioPath;
    }

    public void setVedioPath(String vedioPath) {
        this.vedioPath = vedioPath;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public static class PicBean implements Serializable {
        private String path;
        private String remark;

        public PicBean(String path, String remark) {
            this.path = path;
            this.remark = remark;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
