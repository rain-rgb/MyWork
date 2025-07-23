package com.trtm.sy.registermodules.api.exception.enums;


import com.trtm.sy.registermodules.common.enums.AbsBaseExceptionEnum;

/**
 * 公共错误信息枚举
 *
 * @author accums@pm.me
 */
public enum BaseExceptionEnum implements AbsBaseExceptionEnum {

    VERSION_UPDATE_IS_EXIST("B1003", "修改数据时，入参请携带version"),
    VERSION_NOT_EXIST("B1003", "version属性不存在"),
    SERVER_ERROR("500", "服务器繁忙请重试"),
    URL_NOT_EXIST("404", "资源路径不存在，请检查请求地址"),
    UPDATE_STATUS_UPDATE_ERROR("B1002", "更新数据失败，数据已发生变更，请重新获取数据"),
    UPDATE_STATUS_ERROR("B1002", "更新状态失败，您试图更新被删除的记录"),
    CONSTANT_EMPTY("B1001", "常量获取存在空值，请检查sys_config中是否配置");

    private final String errorCode;

    private final String errorMessage;

    BaseExceptionEnum(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
