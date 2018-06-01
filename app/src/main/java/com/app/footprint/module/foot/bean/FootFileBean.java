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
    private List<PicBean> picPaths;
    private String vedioShootPath;
    private String vedioPath;
    private String textName;
    private String startTime;
    private String endTime;
    private String url;
    private String streetAddress;
    private String formatAddress;
    private boolean isRoute = false;

    public enum Type {
        Camera,
        Vedio,
        Text
    }

    private Type type = Type.Camera;

    public FootFileBean() {
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description,  List<PicBean> picPaths) {
        this.point = point;
        this.description = description;
        this.picPaths = picPaths;
        this.type = Type.Camera;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description, String vedioShootPath, String vedioPath) {
        this.point = point;
        this.description = description;
        this.vedioShootPath = vedioShootPath;
        this.vedioPath = vedioPath;
        this.type = Type.Vedio;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public FootFileBean(String point, String description,  String textName) {
        this.point = point;
        this.description = description;
        this.textName = textName;
        this.type = Type.Text;
        this.id = UUID.randomUUID().toString().substring(0, 10);
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getFormatAddress() {
        return formatAddress;
    }

    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
            if (pointArray.length >= 2) {
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
