package cn.gisdata.footprint.module.my.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Create By fxs On 2017/7/11
 * 功能：登录结果返回
 */
public class UserInfoEntity {
    private int RouteCount;
    private int FootmarkCount;
    private int Integral;
    private int VisitVolume;

    public int getRouteCount() {
        return RouteCount;
    }

    public void setRouteCount(int RouteCount) {
        this.RouteCount = RouteCount;
    }

    public int getFootmarkCount() {
        return FootmarkCount;
    }

    public void setFootmarkCount(int FootmarkCount) {
        this.FootmarkCount = FootmarkCount;
    }

    public int getIntegral() {
        return Integral;
    }

    public void setIntegral(int Integral) {
        this.Integral = Integral;
    }

    public int getVisitVolume() {
        return VisitVolume;
    }

    public void setVisitVolume(int VisitVolume) {
        this.VisitVolume = VisitVolume;
    }

    @Override
    public String toString() {
        return "RouteCount is " + RouteCount + ";FootmarkCount is " + FootmarkCount + ";Integral is " + Integral +
                ";VisitVolume is " + VisitVolume ;
    }
}
