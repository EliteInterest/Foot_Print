package cn.gisdata.footprint.app;


import android.os.Environment;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstStrings {
    public static boolean ISRELEASE = false;
    public static String code = "";
    public static String e = "";
    public static String usename = "";
    public static String adrApikey = "";
    public static String INI_PATH = "";
    public static String APP_NAME = "ZHSQ";
    public static String DEVICE_TYPE = "android_phone";
    private static final String APPNAME = "FootPrint";
    public static final String FootFiles = "footFiles";
    public static final String DraftFootList = "draftFootList";
    public static final String RESPONSE_SUCCESS = "1"; // 请求成功
    public static final String arcgisKey = "5SKIXc21JlankElJ";
    public static String LOCAL_PATH;
    public static String LOCAL_HEAD_PIC_PATH = "";
    public static final String ZHSQ_FILE_720MAP_DATA_SAMPLE_URL = "http://222.178.118.101:6084/arcgis/rest/services/GHDW/JZFP720/MapServer";

    public static String getDatabasePath() {
        return INI_PATH + "/" + APPNAME + "/DATABASE/";
    }

    public static String getCachePath() {
        return INI_PATH + "/" + APPNAME + "/SubmitFile/";
    }

    public static String getZipPath() {
        return INI_PATH + "/" + APPNAME + "/.zip/";
    }

    public static String getOnlinePath() {
        return INI_PATH + "/" + APPNAME + "/ONLINE/";
    }

    public static String getCrashPath() {
        return LOCAL_PATH + "/" + APPNAME + "/CRASH/";
    }

    public static String getDraftPath() {
        return INI_PATH + "/" + APP_NAME + "/DRAFT/";
    }

    public static String getApkPath() {
        return Environment.getExternalStorageDirectory() + File.separator + APPNAME + "/APK/";
//        return INI_PATH + "/" + APPNAME + "/APK/";
    }

    public static String getMainPath() {
        return INI_PATH + "/" + APPNAME;
    }

    public static String getLocalPath() {
        return LOCAL_PATH + "/" + APPNAME;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static final String RECORD_START_TIME = "record_start_time";
    public static final String RECORD_POINTS = "record_points";
    public static final String RECORD_STATUS = "record_status";
}


