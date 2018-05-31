package com.app.footprint.api;

import com.app.footprint.app.ConstStrings;

import java.util.HashMap;
import java.util.Map;


/**
 * Create By Xiangb On 2017/7/11
 * 功能：网络请求参数配置
 */

public class ApiParamUtil {

    //登录
    public static Map<String, String> getLoginDataInfo(String userName, String userPwd) {
        Map<String, String> map = new HashMap<>();
//        map.put("method", "login");
//        map.put("os", ConstStrings.DEVICE_TYPE);
//        map.put("code", ZXMD5Util.getMD5(ZXUniqueIdUtil.getUniqueId()));
//        map.put("e", ZXMD5Util.getMD5(UUID.randomUUID().toString()));
//        map.put("name", userName);
//        map.put("password", userPwd);
//        map.put("password", ZXMD5Util.getMD5(userPwd));

        map.put("name", userName);
        map.put("password", userPwd);
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

    //发送验证码
    public static Map<String, String> getPhoneDataInfo(String phNumer) {
        Map<String, String> map = new HashMap<>();
        map.put("number", phNumer);
        return map;
    }

    //用户注册
    public static Map<String, String> getRegisterDataInfo(String Name, String Password, String Nickname, String Phone, String VerificationCode) {
        Map<String, String> map = new HashMap<>();
        map.put("Name", Name);
        map.put("Password", Password);
        map.put("Nickname", Nickname);
        map.put("Phone", Phone);
        map.put("VerificationCode", VerificationCode);
        return map;
    }

    //得到用户信息
    public static Map<String, String> getUserDataInfo(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        return map;
    }

    //更新用户名称
    public static Map<String, String> getUserNameInfo(String userId, String userName) {
        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId);
        map.put("UserName", userName);
        return map;
    }

    //更新用户昵称
    public static Map<String, String> getUserNickNameIfo(String userId, String nickName) {
        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId);
        map.put("Nickname", nickName);
        return map;
    }

    //更新手机号
    public static Map<String, String> getUserPhoneInfo(String userId, String phone, String checkNum) {
        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId);
        map.put("Number", phone);
        map.put("Code", checkNum);
        return map;
    }

    //更新头像
    public static Map<String, String> getHeadPortraitsInfo(String userId, String file) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("file", file);
        return map;
    }

    //删除列表item
    public static Map<String, String> getDeleteMap(String footprintId) {
        Map<String, String> map = new HashMap<>();
        map.put("footprintId", footprintId);
        return map;
    }
}
