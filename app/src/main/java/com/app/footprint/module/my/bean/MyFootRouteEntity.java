package com.app.footprint.module.my.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：登录结果返回
 */
public class MyFootRouteEntity {

    public RowsBean Route;
    public RowsBean Footmark;
    public RowsBean Visit;
    public RowsBean Login;
    public List<DetailsBean> DetailsInfo;

    public RowsBean getRoute() {
        return this.Route;
    }

    public void setRoute(RowsBean route) {
        this.Route = Route;
    }

    public RowsBean getFootmark() {
        return this.Footmark;
    }

    public void setFootmark(RowsBean Footmark) {
        this.Footmark = Footmark;
    }


    public RowsBean getVisit() {
        return this.Visit;
    }

    public void setVisit(RowsBean Visit) {
        this.Visit = Visit;
    }


    public RowsBean getLogin() {
        return this.Login;
    }

    public void setLogin(RowsBean Login) {
        this.Login = Login;
    }

    public List<DetailsBean> getDetailsInfo() {
        return DetailsInfo;
    }

    public void setDetailsInfo(List<DetailsBean> DetailsInfo) {
        this.DetailsInfo = DetailsInfo;
    }

    @Override
    public String toString() {
        return Route.toString() + Footmark.toString() + Visit.toString() + Login.toString();
    }

    public static class RowsBean implements Serializable {
        private String Name;
        private int Count;
        private int Sum;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getCount() {
            return Count;
        }

        public void setCount(int Count) {
            this.Count = Count;
        }

        public int getSum() {
            return Sum;
        }

        public void setSum(int Sum) {
            this.Sum = Sum;
        }

        @Override
        public String toString() {
            return "Name is " + Name + ";Count is " + Count + ";Sum is " + Sum;
        }
    }

    public static class DetailsBean implements Serializable {
        private String RecodeTime;
        private String RecodeName;
        private int Integral;
        private int IntegralType;

        public String getRecodeTime() {
            return RecodeTime;
        }

        public void setRecodeTime(String RecodeTime) {
            this.RecodeTime = RecodeTime;
        }

        public String getRecodeName() {
            return RecodeName;
        }

        public void setRecodeName(String RecodeName) {
            this.RecodeName = RecodeName;
        }

        public int getIntegral() {
            return Integral;
        }

        public void setIntegral(int Integral) {
            this.Integral = Integral;
        }

        public int getIntegralType() {
            return IntegralType;
        }

        public void setIntegralType(int IntegralType) {
            this.IntegralType = IntegralType;
        }

        @Override
        public String toString() {
            return "RecodeTime is " + RecodeTime + ";RecodeName is " + RecodeName + ";Integral is " + Integral + ";IntegralType is " + IntegralType;
        }

    }

}
