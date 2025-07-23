package com.trtm.sy.enumutil;

public enum YesOrNotEnum {
    Y("Y", "是"),
    N("N", "否");

    private final String code;
    private final String message;

    private YesOrNotEnum(String code, String message) {
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
