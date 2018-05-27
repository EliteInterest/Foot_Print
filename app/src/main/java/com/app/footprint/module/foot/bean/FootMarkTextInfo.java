package com.app.footprint.module.foot.bean;

import com.esri.core.internal.catalog.User;

import java.io.Serializable;

/**
 * Created by Xiangb on 2018/5/22.
 * 功能：
 */

public class FootMarkTextInfo implements Serializable {

    private FootMarkTextBean Footprint;
    private FootMarkTextBean1 PointPosition;
    private FootMarkTextBean2 FileInfo;

    public FootMarkTextBean getFootprint() {
        return Footprint;
    }

    public void setFootprint(FootMarkTextBean footprint) {
        Footprint = footprint;
    }

    public FootMarkTextBean1 getPointPosition() {
        return PointPosition;
    }

    public void setPointPosition(FootMarkTextBean1 pointPosition) {
        PointPosition = pointPosition;
    }

    public FootMarkTextBean2 getFileInfo() {
        return FileInfo;
    }

    public void setFileInfo(FootMarkTextBean2 fileInfo) {
        FileInfo = fileInfo;
    }

    public static class FootMarkTextBean implements Serializable {
        public String UserId;
        public String Name;
        public String Desc;
        public String ConsumptionTime;
        public String StartTime;
        public String EndTime;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Sum) {
            this.Desc = Desc;
        }

        public String getConsumptionTime() {
            return ConsumptionTime;
        }

        public void setConsumptionTime(String ConsumptionTime) {
            this.ConsumptionTime = ConsumptionTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }
        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }
        @Override
        public String toString() {
            return "UserId=" +UserId+",Name=" +Name +",Desc=" +Desc +
                    ",ConsumptionTime=" +ConsumptionTime +",StartTime=" +StartTime +",EndTime=" +EndTime;
        }
    }

    public static class FootMarkTextBean1 implements Serializable {
        public String PointId;
        public double Longitude;
        public double Latitude;
        public double Altitude;
        public String Addr;
        public String Desc;
        public int PointType;

        public String getPointId() {
            return PointId;
        }

        public void setPointId(String PointId) {
            this.PointId = PointId;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double longitude) {
            Longitude = longitude;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double latitude) {
            Latitude = latitude;
        }

        public double getAltitude() {
            return Altitude;
        }

        public void setAltitude(double altitude) {
            Altitude = altitude;
        }

        public String getAddr() {
            return Addr;
        }

        public void setAddr(String Addr) {
            this.Addr = Addr;
        }


        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Sum) {
            this.Desc = Desc;
        }

        public int getPointType() {
            return PointType;
        }

        public void setPointType(int pointType) {
            PointType = pointType;
        }
    }

    public static class FootMarkTextBean2 implements Serializable {
        public String TotalDesc;
        public FootMarkTextBean3 TextInfo;

        public String getTotalDesc() {
            return TotalDesc;
        }

        public void setTotalDesc(String TotalDesc) {
            this.TotalDesc = TotalDesc;
        }

        public FootMarkTextBean3 getTextInfo() {
            return TextInfo;
        }

        public void setTextInfo(FootMarkTextBean3 textInfo) {
            TextInfo = textInfo;
        }
    }

    public static class FootMarkTextBean3 implements Serializable {
        public String TextName;
        public String TextDesc;

        public String getTextName() {
            return TextName;
        }

        public void setTextName(String TextName) {
            this.TextName = TextName;
        }

        public String getTextDesc() {
            return TextDesc;
        }

        public void setTextDesc(String textDesc) {
            TextDesc = textDesc;
        }
    }
}
