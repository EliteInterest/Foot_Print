package cn.gisdata.footprint.module.system.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Create By fxs On 2017/7/11
 * 功能：登录结果返回
 */
public class LoginEntity {
    private String UserId;
    private String UserName;
    private String Nickname;
    private String Phone;
    private String HeadPortraits;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getCode() {
        return UserName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getHeadPortraits() {
        return HeadPortraits;
    }

    public void setHeadPortraits(String HeadPortraits) {
        this.HeadPortraits = HeadPortraits;
    }

    @Override
    public String toString() {
        return "UserId is " + UserId + ";UserName is " + UserName + ";NickName is " + Nickname +
                ";Phone is " + Phone + ";HeadPortraits" + HeadPortraits == null ? "" : HeadPortraits;
    }
}
