package com.htf.error;

/**
 * 处理错误的接口
 */
public interface CommonError {
    public int getErrorCode();
    public String getErrorMsg();
    public CommonError setErrorMsg(String errorMsg);
}
