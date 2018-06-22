package cn.gisdata.footprint.module.foot.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 */
public class DraftFootBean implements Serializable {

    private int footType = 0;//0足印  1路径

    private int uploadType;

    private String markInfoJson;
    private String routeInfoJson;
    private String textInfoJson;
    private String pathInfoJson;
    private String saveInfoJson;
    private String date;
    private String name;
    private List<FilePathBean> filePathBeans;
    private List<String> filePaths;
    private String point;

    public DraftFootBean(String date, String name, String markInfoJson, List<String> filePaths, int uploadType, String point) {
        footType = 0;
        this.markInfoJson = markInfoJson;
        this.date = date;
        this.point = point;
        this.name = name;
        this.filePaths = filePaths;
        this.uploadType = uploadType;
    }

    public DraftFootBean(String date, String name, String routeInfoJson, String textInfoJson, String pathInfoJson, String saveInfoJson, List<FilePathBean> filePathBeans) {
        footType = 1;
        this.routeInfoJson = routeInfoJson;
        this.textInfoJson = textInfoJson;
        this.name = name;
        this.date = date;
        this.pathInfoJson = pathInfoJson;
        this.saveInfoJson = saveInfoJson;
        this.filePathBeans = filePathBeans;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteInfoJson() {
        return routeInfoJson;
    }

    public void setRouteInfoJson(String routeInfoJson) {
        this.routeInfoJson = routeInfoJson;
    }

    public String getTextInfoJson() {
        return textInfoJson;
    }

    public void setTextInfoJson(String textInfoJson) {
        this.textInfoJson = textInfoJson;
    }

    public String getPathInfoJson() {
        return pathInfoJson;
    }

    public void setPathInfoJson(String pathInfoJson) {
        this.pathInfoJson = pathInfoJson;
    }

    public String getSaveInfoJson() {
        return saveInfoJson;
    }

    public void setSaveInfoJson(String saveInfoJson) {
        this.saveInfoJson = saveInfoJson;
    }

    public List<FilePathBean> getFilePathBeans() {
        return filePathBeans;
    }

    public void setFilePathBeans(List<FilePathBean> filePathBeans) {
        this.filePathBeans = filePathBeans;
    }

    public int getFootType() {
        return footType;
    }

    public void setFootType(int footType) {
        this.footType = footType;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

    public String getMarkInfoJson() {
        return markInfoJson;
    }

    public void setMarkInfoJson(String markInfoJson) {
        this.markInfoJson = markInfoJson;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getFilePaths() {
        if (filePaths != null) {
            return filePaths;
        } else {
            List<String> paths = new ArrayList<>();
            if (filePathBeans != null && filePathBeans.size() > 0) {
                for (FilePathBean file : filePathBeans) {
                    paths.add(file.getPath());
                }
            }
            return paths;
        }
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public static class FilePathBean implements Serializable {
        private String path;
        private int type;

        public FilePathBean(String path, int type) {
            this.path = path;
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
