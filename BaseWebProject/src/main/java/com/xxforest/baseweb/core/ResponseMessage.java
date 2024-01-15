package com.xxforest.baseweb.core;


import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一数据返回格式
 */
public class ResponseMessage extends HashMap{


    public static ResponseMessage create(){
        ResponseMessage responseMessage = new ResponseMessage();
        return responseMessage ;
    }


    public static ResponseMessage success(String key , Object val){
        ResponseMessage responseMessage = ResponseMessage.create();
        responseMessage.putData("code",200);
        responseMessage.putData(key,val);
        return responseMessage ;
    }

    public static ResponseMessage success(){
        ResponseMessage responseMessage = ResponseMessage.create();
        responseMessage.putData("code",200);
        return responseMessage ;
    }


    public ResponseMessage putData(String key, Object val) {
        this.put(key,val);
        return this;
    }

    public static ResponseMessage error(String errorMsg){
        ResponseMessage responseMessage = ResponseMessage.create();
        responseMessage.putData("code",-1);
        responseMessage.putData("error",errorMsg);
        return responseMessage ;
    }

    public ResponseMessage setCode(int code){
        this.putData("code",code);
        return this ;
    }

    public ResponseMessage setError(String errorMsg){
        this.putData("error",errorMsg);
        return this ;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
