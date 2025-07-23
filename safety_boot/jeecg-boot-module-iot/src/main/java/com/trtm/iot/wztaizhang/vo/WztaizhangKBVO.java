package com.trtm.iot.wztaizhang.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WztaizhangKBVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectName;
    private Integer allWeight;
    private Integer unWeight;
    private Double unWeightLv;
    private Integer allPici;
    private Integer unPici;
    private Double unPiciLv;
    private Integer hegePici;
    private Double hegelv;
    private List<WztaizhangKBVO> biaoduanList;

}
