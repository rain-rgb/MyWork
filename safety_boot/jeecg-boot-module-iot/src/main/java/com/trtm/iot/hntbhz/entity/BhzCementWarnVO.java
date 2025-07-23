package com.trtm.iot.hntbhz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BhzCementWarnVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警总数")
    private Integer warnCount;
    @ApiModelProperty(value = "初级预警数")
    private Integer primaryCount;
    @ApiModelProperty(value = "中级预警数")
    private Integer middleCount;
    @ApiModelProperty(value = "高级预警数")
    private Integer advancedCount;
    @ApiModelProperty(value = "拌合时间超标数")
    private Integer timeCount;
    @ApiModelProperty(value = "拌合时间超标闭合数")
    private Integer timeBiheCount;
    @ApiModelProperty(value = "初级预警闭合数")
    private Integer primaryBiheCount;
    @ApiModelProperty(value = "中级预警闭合数")
    private Integer middleBiheCount;
    @ApiModelProperty(value = "高级预警闭合数")
    private Integer advancedBiheCount;
    @ApiModelProperty(value = "入仓温度预警数")
    private Integer ruCangWarnCount;
    @ApiModelProperty(value = "入仓温度预警闭合数")
    private Integer ruCangBiheCount;

    private Integer allDish;//总盘数
    private Integer allWarnDish;//总预警数
    private String sysOrgCode;//组织编码
    private String shebeiNo;//设备编号
    private String shebeiName;//设备名称
    private String projectName;//项目名称
    private String companyName;//公司名称
    private String sectionName;//标段名称
    private Double advancedWarnLv;//高级预警率
    private Double allWarnLv;//总体预警率
    private Integer biheCount;//总体闭合数
    private Double biheLv;//总体闭合率




}
