package com.app.footprint.module.foot.mvp.model;

import com.frame.zxmvp.base.BaseModel;

import com.app.footprint.module.foot.mvp.contract.EditInfoContract;

import java.util.Map;

import rx.Observable;

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
public class EditInfoModel extends BaseModel implements EditInfoContract.Model {


    @Override
    public Observable<String> commitFile(Map<String, String> map) {
        return null;
    }
}