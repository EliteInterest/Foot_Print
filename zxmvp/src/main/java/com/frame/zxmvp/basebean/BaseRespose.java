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

    public T data;
    public T list;

//    public boolean success() {
//        return "200000".equals(Code);
//    }
    public boolean success() {
        Log.i(TAG,"code is " +Code);
        Log.i(TAG,"data is " +data);
        return (200000 == Code);
    }

    @Override
    public String toString() {
        Log.i(TAG,"message is " +"BaseRespose{" +
                "code='" + Code + '\'' +
                ", msg='" + Message == null ? Exception : Message + '\'' +
                ", data=" + (data == null ? list : data) +
                '}');
        return "BaseRespose{" +
                "code='" + Code + '\'' +
                ", msg='" + Message == null ? Exception : Message + '\'' +
                ", data=" + (data == null ? list : data) +
                '}';
    }
}
