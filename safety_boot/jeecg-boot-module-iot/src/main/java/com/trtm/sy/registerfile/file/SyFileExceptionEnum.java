package com.trtm.sy.registerfile.file;


import com.trtm.sy.registermodules.common.enums.AbsBaseExceptionEnum;

/**
 * @Description 文件相关异常枚举类
 * @Author Liupei
 * @Date 2023-06-29
 */
public enum SyFileExceptionEnum implements AbsBaseExceptionEnum {

    // 目录编码为空!
    ML_CODE_IS_EMPTY("BUSFILE1001", "目录编码为空!"),
    // 文件目录不存在!
    WJML_IS_EMPTY("BUSFILE1002", "文件目录不存在!"),
    // 文件目录不能为空
    WJML_IS_NOT_EMPTY("BUSFILE1003", "文件目录不能为空!"),
    // 所选的目录中有不可删除或修改项,请检查!
    EDIT_FLAG_CHECK("BUSFILE1004", "所选的目录中有不可删除或修改项,请检查!"),
    // 所删除的目录中已有规程,请先删除规程!
    DOT_DELETE("BUSFILE1005", "所删除的目录中已有规程,请先删除规程!"),
    // 上级类目不能选择本身
    FML_IS_NOT_ITSELF("BUSFILE1006", "上级类目不能选择本身!"),
    // 不可将上级类目修改为其子级类目
    NOT_MODIFY_FML_TO_CHILD("BUSFILE1007", "不可将上级类目修改为其子级类目!");

    SyFileExceptionEnum(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private final String errorCode;

    private final String errorMessage;


    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
