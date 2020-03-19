package com.htf.response;

import lombok.Data;

/**
 * 通用的返回值类型
 */
@Data
public class ResponseJSONResult {
    /*状态码*/
    private String status;//success   fail
    /*返回的数据*/
    private Object data;

    //默认，状态为成功
    public  static ResponseJSONResult create(Object data){
        return ResponseJSONResult.create(data,"success");
    }

    //设置数据和状态
    public static ResponseJSONResult create(Object data, String status){
        ResponseJSONResult commonReturnType = new ResponseJSONResult();
        commonReturnType.setData(data);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }
}
