package com.app.footprint.module.foot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xiangb on 2018/5/22.
 * 功能：
 */

public class FootFileBean implements Serializable {

    private PictureBean pictureBean;

    private VedioBean vedioBean;

    private TextBean textBean;

    public PictureBean getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
    }

    public VedioBean getVedioBean() {
        return vedioBean;
    }

    public void setVedioBean(VedioBean vedioBean) {
        this.vedioBean = vedioBean;
    }

    public TextBean getTextBean() {
        return textBean;
    }

    public void setTextBean(TextBean textBean) {
        this.textBean = textBean;
    }

    public static class PictureBean implements Serializable {
        private String description;
        private String point;
        private List<PicChildBean> paths;

        public PictureBean(String description, String point, List<PicChildBean> paths) {
            this.description = description;
            this.point = point;
            this.paths = paths;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public List<PicChildBean> getPaths() {
            return paths;
        }

        public void setPaths(List<PicChildBean> paths) {
            this.paths = paths;
        }

        public static class PicChildBean implements Serializable {
            private String path;
            private String remark;

            public PicChildBean(String path, String remark) {
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

    public static class VedioBean implements Serializable {
        private String description;
        private String point;
        private String path;
        private String picShootPath;

        public VedioBean(String description, String point, String path, String picShootPath) {
            this.description = description;
            this.point = point;
            this.path = path;
            this.picShootPath = picShootPath;
        }

        public String getPicShootPath() {
            return picShootPath;
        }

        public void setPicShootPath(String picShootPath) {
            this.picShootPath = picShootPath;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class TextBean implements Serializable {
        private String name;
        private String description;
        private String point;

        public TextBean(String name, String description, String point) {
            this.name = name;
            this.description = description;
            this.point = point;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }
    }
}
