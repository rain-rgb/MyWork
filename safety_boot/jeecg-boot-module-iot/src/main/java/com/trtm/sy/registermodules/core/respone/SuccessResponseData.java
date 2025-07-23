package com.trtm.sy.registermodules.core.respone;

/**
 * 成功结果返回
 *
 * @author accums@pm.me
 */
public class SuccessResponseData<T> extends ResponseData<T> {

    public SuccessResponseData() {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public SuccessResponseData(T object) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, object);
    }

    public SuccessResponseData(String code, String message, T object) {
        super(true, code, message, object);
    }
}
