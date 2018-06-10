package cn.gisdata.footprint.module.map.bean;

import java.util.List;

/**
 * Created by Xiangb on 2018/5/30.
 * 功能：
 */
public class BaiduLatlngBean {

    /**
     * status : 0
     * result : [{"x":114.2307519546763,"y":29.57908428837437}]
     */

    private int status;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * x : 114.2307519546763
         * y : 29.57908428837437
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
