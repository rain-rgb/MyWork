package com.trtm.iot.hntbhz.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BhzBiaoduanVO implements Serializable {
    private static final long serialVersionUID = 1L;


    private String name;
    private Integer count;
    private Float chaobiaoLv;
    private List<BhzCementWarnVO> records;
}
