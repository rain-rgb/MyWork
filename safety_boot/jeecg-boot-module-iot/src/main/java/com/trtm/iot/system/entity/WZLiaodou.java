package com.trtm.iot.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WZLiaodou implements Serializable {
    private String lcbh;//料斗名
    private String name;//料仓名
    private String pici;//检验批编号


}
