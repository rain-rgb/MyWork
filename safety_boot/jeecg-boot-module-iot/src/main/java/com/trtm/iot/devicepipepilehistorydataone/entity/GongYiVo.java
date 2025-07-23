package com.trtm.iot.devicepipepilehistorydataone.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class GongYiVo implements Serializable {
    private static final long serialVersionUID = 1L;



    private Integer allDish;//总数
    private Integer allWarnDish;//总预警数
    private String sysOrgCode;//组织编码
    private String shebeiNo;//设备编号
    private String shebeiName;//设备名称
    private String sectionName;//标段名称
    private String allWarnLv;//总体预警率
    private Integer biheCount;//总体闭合数
    private String biheLv;//总体闭合率




}