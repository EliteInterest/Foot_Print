package com.app.footprint.app;


public class ConstStrings {
    public static boolean ISRELEASE = false;
    public static String code = "";
    public static String e = "";
    public static String usename = "";
    public static String adrApikey = "";
    public static String INI_PATH = "";
    public static String APP_NAME = "ZHSQ";
    public static String DEVICE_TYPE = "android_phone";
    private static final String APPNAME = "MaincityMap";
    public static final String RESPONSE_SUCCESS = "1"; // 请求成功
    public static final String arcgisKey = "5SKIXc21JlankElJ";
    public static String LOCAL_PATH;
    public static final String ZHSQ_FILE_720MAP_DATA_SAMPLE_URL = "http://222.178.118.101:6084/arcgis/rest/services/GHDW/JZFP720/MapServer";

    public static String getDatabasePath(){
        return INI_PATH + "/" + APPNAME + "/DATABASE/";
    }

    public static String getCachePath() {
        return INI_PATH + "/" + APPNAME + "/SubmitFile/";
    }

    public static String getZipPath() {
        return INI_PATH + "/" + APPNAME + "/.zip/";
    }

    public static String getOnlinePath(){
        return INI_PATH + "/" + APPNAME + "/ONLINE/";
    }

    public static String getCrashPath(){
        return LOCAL_PATH + "/" + APPNAME + "/CRASH/";
    }

    public static String getApkPath(){
//        return Environment.getExternalStorageState() + File.separator;
        return INI_PATH + "/" + APPNAME + "/APK/";
    }

    public static String getMainPath(){
        return INI_PATH + "/" +APPNAME;
    }

    public static String getLocalPath(){
        return LOCAL_PATH + "/" +APPNAME;
    }

}


