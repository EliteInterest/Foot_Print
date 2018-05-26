package com.app.footprint.module.my.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：登录结果返回
 */
public class MyFootRouteEntity implements Serializable {

    public String Id;
    public String Name;
    public String StartTime;
    public String EndTime;
    public int VisitVolume;
    public float Mileage;
    public String ScreenshotPath;
    public String DetailsUrlPath;
    public List<List<Float>> Path;


    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.Name;
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

    public float getMileage()
    {
        return this.Mileage;
    }

    public void setMileage(float Mileage)
    {
        this.Mileage = Mileage;
    }

    public String getScreenshotPath()
    {
        return  this.ScreenshotPath;
    }

    public void setScreenshotPath(String ScreenshotPath)
    {
        this.ScreenshotPath = ScreenshotPath;
    }

    public String getDetailsUrlPath()
    {
        return this.DetailsUrlPath;
    }

    public void setDetailsUrlPath(String DetailsUrlPath) {
        this.DetailsUrlPath = DetailsUrlPath;
    }

    public List<List<Float>> getPath()
    {
        return this.Path;
    }

    public void setPath(List<List<Float>> Path) {
        this.Path = Path;
    }

    @Override
    public String toString() {
        return Id.toString() + Name.toString() + StartTime.toString() + EndTime.toString();
    }
}
