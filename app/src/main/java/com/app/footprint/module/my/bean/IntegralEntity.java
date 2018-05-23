package com.app.footprint.module.my.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create By Xiangb On 2017/7/11
 * 功能：登录结果返回
 */
public class IntegralEntity {

    public RowsBean Route;
    public RowsBean Footmark;
    public RowsBean Visit;
    public RowsBean Login;
    public List<RowsBean1> DetailsInfo;

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

        }

    public static class RowsBean1 implements Serializable {
        private String RecodeTime;
        private String RecodeName;
        private int Integral;

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

    }

}
