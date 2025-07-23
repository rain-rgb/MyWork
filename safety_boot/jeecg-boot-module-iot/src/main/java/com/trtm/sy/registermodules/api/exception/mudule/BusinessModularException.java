//package com.trtm.sy.registermodules.api.exception.mudule;
//
//import cn.hutool.core.util.StrUtil;
//import com.trtm.sy.registermodules.api.exception.BaseException;
//import com.trtm.sy.registermodules.common.enums.AbsBaseExceptionEnum;
//
//
///**
// * 业务模块的异常
// *
// * @author wh
// */
//public class BusinessModularException extends BaseException {
//
//    private final static String BUSINESS_MODULE_NAME = "BUSINESS_MODULE";
//
//    public BusinessModularException(AbsBaseExceptionEnum exception, Object... params) {
//        super(BUSINESS_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getErrorMessage(), params));
//    }
//
//    public BusinessModularException(AbsBaseExceptionEnum exception) {
//        super(BUSINESS_MODULE_NAME, exception);
//    }
//
//    public BusinessModularException(String errorMessage) {
//        super(errorMessage);
//    }
//
//}
