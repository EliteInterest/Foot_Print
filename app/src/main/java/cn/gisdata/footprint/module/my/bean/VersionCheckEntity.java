package cn.gisdata.footprint.module.my.bean;

/**
 * Create By fxs On 2017/7/11
 * 功能：登录结果返回
 */
public class VersionCheckEntity {
    private String ApkUrl;
    private int IsUpdate;
    private String NewVersionName;

    public String getApkUrl() {
        return ApkUrl == null ? "" : ApkUrl;
    }

    public void setApkUrl(String apkUrl) {
        ApkUrl = apkUrl;
    }

    public int getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        IsUpdate = isUpdate;
    }

    public String getNewVersionName() {
        return NewVersionName == null ? "" : NewVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        NewVersionName = newVersionName;
    }

    @Override
    public String toString() {
        return "ApkUrl is " + ApkUrl + ";IsUpdate is " + IsUpdate + ";NewVersionName is " + NewVersionName;
    }
}
