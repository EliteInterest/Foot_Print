package com.app.footprint.module.foot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xiangb on 2018/5/22.
 * 功能：
 */

public class FootFileBean implements Serializable {

        private String point;
        private String description;
        private List<PicBean> picPaths;
        private String vedioShootPath;
        private String vedioPath;
        private String textName;

        public enum Type {
            Camera,
            Vedio,
            Text
        }

        private Type type = Type.Camera;

        public FootFileBean(String point, String description, List<PicBean> picPaths) {
            this.point = point;
            this.description = description;
            this.picPaths = picPaths;
            this.type = Type.Camera;
        }

        public FootFileBean(String point, String description, String vedioShootPath, String vedioPath) {
            this.point = point;
            this.description = description;
            this.vedioShootPath = vedioShootPath;
            this.vedioPath = vedioPath;
            this.type = Type.Vedio;
        }

        public FootFileBean(String point, String description, String textName) {
            this.point = point;
            this.description = description;
            this.textName = textName;
            this.type = Type.Text;
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
