package com.frame.zxmvp.basebean;

import android.util.Log;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 */
public class BaseRespose<T> implements Serializable {
    private static final String TAG = "BaseRespose";
    public int Code;
    public String Message;
    public String Exception;

    public T Data;

//    public boolean success() {
//        return "200000".equals(Code);
//    }
    public boolean success() {
        Log.i(TAG,"code is " +Code);
        Log.i(TAG,"data is " +Data);
        return (200000 == Code);
    }

    @Override
    public String toString() {
        return "BaseRespose{" +
                "code='" + Code + '\'' +
                ", msg='" + Message == null ? Exception : Message + '\'' +
                ", data=" + Data +
                '}';
    }
}
