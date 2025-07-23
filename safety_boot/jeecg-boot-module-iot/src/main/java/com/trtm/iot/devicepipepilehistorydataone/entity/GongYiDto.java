package com.trtm.iot.devicepipepilehistorydataone.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GongYiDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Integer zongshu;
    private String chaobiaolv;
    private List<GongYiVo> records;
}
