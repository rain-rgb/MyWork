package com.trtm.sy.registermodules.api.exception;


import com.trtm.sy.registermodules.common.enums.AbsBaseExceptionEnum;

public class BaseException extends RuntimeException {
    private String code = "500";
    private String message;
    private String errorCode;
    private final String errorMessage;
    private String errorModuleName = "CORE";

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getErrorModuleName() {
        return this.errorModuleName;
    }

    public void setErrorModuleName(String errorModuleName) {
        this.errorModuleName = errorModuleName;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.code = "500";
        this.message = errorMessage;
    }

    public BaseException(String errorModuleName, String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorModuleName = errorModuleName;
        this.code = errorCode;
        this.message = errorMessage;
    }

    public BaseException(AbsBaseExceptionEnum exceptionEnum) {
        super(exceptionEnum.getErrorMessage());
        this.errorCode = exceptionEnum.getErrorCode();
        this.errorMessage = exceptionEnum.getErrorMessage();
        this.code = exceptionEnum.getErrorCode();
        this.message = exceptionEnum.getErrorMessage();
    }

    public BaseException(String errorModuleName, AbsBaseExceptionEnum exception) {
        super(exception.getErrorMessage());
        this.errorCode = exception.getErrorCode();
        this.errorMessage = exception.getErrorMessage();
        this.errorModuleName = errorModuleName;
        this.code = exception.getErrorCode();
        this.message = exception.getErrorMessage();
    }
}
