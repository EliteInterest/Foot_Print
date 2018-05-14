package com.app.footprint.module.system.bean;

/**
 *
 * Create By Xiangb On 2017/7/11
 * 功能：登录结果返回
 *
 */
public class LoginEntity {


    /**
     * id : 86
     * name : xb
     * truename : admin
     * code : f1dc50b732dbc69159856b20233d022d
     * salt : 57ts1p
     * mobile :
     * email :
     * isdbsx : 1
     * iszdsj : 1
     * adrApikey : 1234567890
     * active : 1
     */

    private String id;
    private String name;
    private String truename;
    private String code;
    private String salt;
    private String mobile;
    private String email;
    private String isdbsx;
    private String iszdsj;
    private String adrApikey;
    private int active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsdbsx() {
        return isdbsx;
    }

    public void setIsdbsx(String isdbsx) {
        this.isdbsx = isdbsx;
    }

    public String getIszdsj() {
        return iszdsj;
    }

    public void setIszdsj(String iszdsj) {
        this.iszdsj = iszdsj;
    }

    public String getAdrApikey() {
        return adrApikey;
    }

    public void setAdrApikey(String adrApikey) {
        this.adrApikey = adrApikey;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
