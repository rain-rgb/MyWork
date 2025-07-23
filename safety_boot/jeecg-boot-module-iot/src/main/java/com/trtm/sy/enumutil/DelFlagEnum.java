package com.trtm.sy.enumutil;

public enum DelFlagEnum {
    Y("Y", "已删除"),
    N("N", "未删除");

    private final String code;
    private final String message;

    private DelFlagEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
