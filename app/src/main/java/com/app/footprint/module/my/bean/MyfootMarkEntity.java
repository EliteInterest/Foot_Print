package com.app.footprint.module.my.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：登录结果返回
 */
public class MyfootMarkEntity implements Serializable {

    public String Id;
    public String Name;
    public String StartTime;
    public String EndTime;
    public int VisitVolume;
    public float Longitude;
    public float Latitude;
    public float Altitude;
    public String DetailsUrlPath;

    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.Name == null ? "":this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    public String getStartTime() {
        return this.StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }


    public String getEndTime() {
        return this.EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public int getVisitVolume() {
        return this.VisitVolume;
    }

    public void setVisitVolume(int VisitVolume) {
        this.VisitVolume = VisitVolume;
    }

    public float getLongitude()
    {
        return this.Longitude;
    }

    public void setLongitude(float Longitude)
    {
        this.Longitude = Longitude;
    }

    public float getLatitude()
    {
        return this.Latitude;
    }

    public void setLatitude(float Latitude)
    {
        this.Latitude = Latitude;
    }

    public float getAltitude()
    {
        return this.Altitude;
    }

    public void setAltitude(float Altitude)
    {
        this.Altitude = Altitude;
    }

    public String getDetailsUrlPath()
    {
        return DetailsUrlPath;
    }

    public void setDetailsUrlPath(String detailsUrlPath) {
        DetailsUrlPath = detailsUrlPath;
    }

    @Override
    public String toString() {
        return DetailsUrlPath.toString();
    }
}
