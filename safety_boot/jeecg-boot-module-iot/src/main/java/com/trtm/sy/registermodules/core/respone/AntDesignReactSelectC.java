package com.trtm.sy.registermodules.core.respone;

import lombok.Data;

@Data
public class AntDesignReactSelectC {

    public AntDesignReactSelectC(String label, String value) {
        this.label = label;
        this.value = value;
    }

    private Long id = System.currentTimeMillis();

    private String label;

    private String value;


}
