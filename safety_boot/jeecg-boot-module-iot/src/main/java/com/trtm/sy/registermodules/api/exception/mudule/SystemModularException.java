package com.trtm.sy.registermodules.api.exception.mudule;

import cn.hutool.core.util.StrUtil;
import com.trtm.sy.registermodules.api.exception.BaseException;
import com.trtm.sy.registermodules.common.enums.AbsBaseExceptionEnum;


/**
 * 系统管理模块的异常
 * 限制异常的抛出格式
 * @author
 */
public class SystemModularException extends BaseException {

    private final static String SYSTEM_MODULE_NAME = "SYSTEM_MODULE";

    public SystemModularException(AbsBaseExceptionEnum exception, Object... params) {
        super(SYSTEM_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getErrorMessage(), params));
    }

    public SystemModularException(AbsBaseExceptionEnum exception) {
        super(SYSTEM_MODULE_NAME, exception);
    }

}
