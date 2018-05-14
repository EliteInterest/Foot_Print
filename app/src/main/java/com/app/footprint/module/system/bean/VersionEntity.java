package com.app.footprint.module.system.bean;

/**
 * Created by Xiangb on 2017/7/12.
 * 功能：
 */

public class VersionEntity {

    /**
     * vid : 104
     * title : 规划定位2.1.5
     * version : 2.1.5
     * platform : android_pad
     * content : 软件测试升级0147852369
     * sourcename : 规划定位测试线_2.1.3.apk
     * forced_content :
     * url : http://192.168.110.116/zckg/version/android_pad/citymap.apk
     * islatest : 1
     * isforced : 0
     */

    private String vid;
    private String title;
    private String version;
    private String platform;
    private String content;
    private String sourcename;
    private String forced_content;
    private String url;
    private int islatest;
    private int isforced;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getForced_content() {
        return forced_content;
    }

    public void setForced_content(String forced_content) {
        this.forced_content = forced_content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIslatest() {
        return islatest;
    }

    public void setIslatest(int islatest) {
        this.islatest = islatest;
    }

    public int getIsforced() {
        return isforced;
    }

    public void setIsforced(int isforced) {
        this.isforced = isforced;
    }
}
