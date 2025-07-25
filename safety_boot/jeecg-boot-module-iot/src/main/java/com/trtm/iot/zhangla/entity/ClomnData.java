package com.trtm.iot.zhangla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ClomnData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prop;
    private String label;
}
