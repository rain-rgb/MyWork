package com.trtm.sy.enumutil;

public enum RedisCacheKeyEnum {
    ;


    private String key;

    RedisCacheKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
