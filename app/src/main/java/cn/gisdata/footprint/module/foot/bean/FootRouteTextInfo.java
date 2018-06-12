package cn.gisdata.footprint.module.foot.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fxs on 2018/5/22.
 * 功能：
 */

public class FootRouteTextInfo implements Serializable {

    private FootRouteTextInfoFootprint Footprint;
    private List<FootRouteTextInfoPointPositions> PointPositions;

    public FootRouteTextInfoFootprint getFootprint() {
        return Footprint;
    }

    public void setFootprint(FootRouteTextInfoFootprint footprint) {
        Footprint = footprint;
    }

    public List<FootRouteTextInfoPointPositions> getPointPositions() {
        return PointPositions;
    }

    public void setPointPositions(List<FootRouteTextInfoPointPositions> PointPositions) {
        this.PointPositions = PointPositions;
    }

    public static class FootRouteTextInfoFootprint implements Serializable {
        public String UserId;
        public String Name;
        public String Desc;
        public float Mileage;
        public int ConsumptionTime;
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

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public float getMileage()
        {
            return Mileage;
        }

        public void setMileage(float mileage) {
            Mileage = mileage;
        }

        public int getConsumptionTime() {
            return ConsumptionTime;
        }

        public void setConsumptionTime(int ConsumptionTime) {
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

    public static class FootRouteTextInfoPointPositions implements Serializable {
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

    public static class FootRouteTextInfoBean implements Serializable {
        public List<FootRouteTextInfoBeanDetail> TextInfo;

        public List<FootRouteTextInfoBeanDetail> getTextInfo() {
            return TextInfo;
        }

        public void setTextInfo(List<FootRouteTextInfoBeanDetail> TextInfo)
        {
            this.TextInfo = TextInfo;
        }
    }

    public static class FootRouteTextInfoBeanDetail implements Serializable {
        public String PointId;
        public String TextName;
        public String TextDesc;

        public String getPointId() {
            return PointId;
        }

        public void setPointId(String PointId) {
            this.PointId = PointId;
        }

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

    public static class FootRouteSaveInfo implements Serializable {
        public String TotalDesc;
        public List<FootRouteSaveInfoDetail> MediaInfo;

        public String getTotalDesc()
        {
            return TotalDesc;
        }

        public void setTotalDesc(String totalDesc) {
            TotalDesc = totalDesc;
        }

        public List<FootRouteSaveInfoDetail> getMediaInfo() {
            return MediaInfo;
        }

        public void setMediaInfo(List<FootRouteSaveInfoDetail> MediaInfo)
        {
            this.MediaInfo = MediaInfo;
        }
    }

    public static class FootRouteSaveInfoDetail implements Serializable {
        public String PointId;
        public String Desc;

        public String getPointId() {
            return PointId;
        }

        public void setPointId(String PointId) {
            this.PointId = PointId;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }
    }

    public static class FootRouteFileInfo implements Serializable {
        public int fileType;
        public File MediaFile;

        public int getFileType()
        {
            return fileType;
        }

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public File getMediaInfo()
        {
            return MediaFile;
        }

        public void setMediaFile(File MediaFile) {
            this.MediaFile = MediaFile;
        }
    }

    public static class FootRoutePoint implements Serializable {
        public double X;
        public double Y;
        public double Z;

        public double getX()
        {
            return X;
        }

        public void setX(double x) {
            X = x;
        }

        public double getY() {
            return Y;
        }

        public void setY(double y) {
            Y = y;
        }

        public double getZ() {
            return Z;
        }

        public void setZ(double z) {
            Z = z;
        }
    }
}
