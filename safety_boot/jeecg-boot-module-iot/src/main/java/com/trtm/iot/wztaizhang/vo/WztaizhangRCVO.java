package com.trtm.iot.wztaizhang.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WztaizhangRCVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private String projectName;
    private String sectionName;
    private String producerName;
    private String localName;
    private String buhegeName;
}
