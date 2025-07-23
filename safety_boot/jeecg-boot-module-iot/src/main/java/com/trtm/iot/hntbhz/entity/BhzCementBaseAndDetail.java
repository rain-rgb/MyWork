package com.trtm.iot.hntbhz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@ApiModel(value = "bhz_cement_base对象", description = "拌合站主表")
@Data
@TableName("bhz_cement_base")
public class BhzCementBaseAndDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * 唯一ID
     */
    @ApiModelProperty(value = "唯一ID")
    private String batchNo;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private String shebeiNo;
    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
    private String workNo;
    /**
     * 操作者
     */
    @Excel(name = "操作者", width = 15)
    @ApiModelProperty(value = "操作者")
    private String handlers;
    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projectName;
    /**
     * 施工地点
     */
    @Excel(name = "施工地点", width = 15)
    @ApiModelProperty(value = "施工地点")
    private String jobLocation;
    /**
     * 浇筑部位
     */
    @Excel(name = "浇筑部位", width = 15)
    @ApiModelProperty(value = "浇筑部位")
    private String poureLocation;
    /**
     * 水泥品种
     */
    @Excel(name = "水泥品种", width = 15)
    @ApiModelProperty(value = "水泥品种")
    private String cementVariety;
    /**
     * 外加剂品种
     */
    @ApiModelProperty(value = "外加剂品种")
    private String additiveVariety;
    /**
     * 配方号
     */
    @Excel(name = "配方号", width = 15)
    @ApiModelProperty(value = "配方号")
    private String formulaNo;
    /**
     * 强度等级
     */
    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private String strengthRank;
    /**
     * 搅拌时间
     */
    @Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private Integer stirDatetime;
    /**
     * 保存时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "保存时间")
    private Date saveDatetime;
    /**
     * 客户端编号
     */
    @ApiModelProperty(value = "客户端编号")
    private String clientNo;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 采集时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
    private Date collectDatetime;
    /**
     * 坍落度
     */
    @Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    private String slump;
    /**
     * 方量
     */
    @Excel(name = "方量", width = 15)
    @ApiModelProperty(value = "方量")
    private Double estimateNumber;
    /**
     * 出料时间
     */
    @Excel(name = "出料时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出料时间")
    private Date productDatetime;

    /**
     * 超标等级
     */
//    @Excel(name = "超标等级", width = 15)
    @Dict(dicCode = "over_level")
    @ApiModelProperty(value = "超标等级")
    private Integer overLevel;
    /**
     * 是否超标
     */
    @ApiModelProperty(value = "是否超标")
    private Integer alertstate;
    /**
     * 配方uuid(车结束符)
     */
    @ApiModelProperty(value = "配方uuid(车结束符)")
    private String formulaId;
    /**
     * 搅拌时间超标等级
     */
    @ApiModelProperty(value = "搅拌时间超标等级")
    private Integer timeOverLevel;
    /**
     * 搅拌时间超标等级
     */
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "overproof_status")
    private Integer overproofStatus;

    @ApiModelProperty(value = "任务单计算状态")
    private Integer renwudanstatus;

    private Integer jiaozhustatus;//浇筑部位计算状态
    private String chuliaowd;//出料温度
    private String overReason;// 预警原因
    private Integer statistics;
    private Integer isfilter;
    private Integer issend;
    private Integer isdel;

    private List<BhzCementDetail> bhzCementDetailList;
}
