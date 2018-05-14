package com.app.footprint.api;

import com.app.footprint.app.ConstStrings;
import com.zx.zxutils.util.ZXMD5Util;
import com.zx.zxutils.util.ZXUniqueIdUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Create By Xiangb On 2017/7/11
 * 功能：网络请求参数配置
 */

public class ApiParamUtil {

    //登录
    public static Map<String, String> getLoginDataInfo(String userName, String userPwd) {
        Map<String, String> map = new HashMap<>();
        map.put("method", "login");
        map.put("os", ConstStrings.DEVICE_TYPE);
        map.put("code", ZXMD5Util.getMD5(ZXUniqueIdUtil.getUniqueId()));
        map.put("e", ZXMD5Util.getMD5(UUID.randomUUID().toString()));
        map.put("name", userName);
        map.put("password", ZXMD5Util.getMD5(userPwd));
        return map;
    }

    //版本更新
    public static Map<String, String> getVersionUpdateDataInfo(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("method", "versionUpdate");
        map.put("os", ConstStrings.DEVICE_TYPE);
        map.put("code", ConstStrings.code);
        map.put("e", ConstStrings.e);
        map.put("version", version);
        return map;
    }

}
