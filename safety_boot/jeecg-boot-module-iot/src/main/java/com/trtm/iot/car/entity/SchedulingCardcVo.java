package com.trtm.iot.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SchedulingCardcVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * Erp端ID
     */
    @Excel(name = "Erp端ID", width = 15)
    @ApiModelProperty(value = "Erp端ID")
    private Integer erpid;
    /**
     * 生产线(1或2)
     */
    @Excel(name = "生产线(1或2)", width = 15)
    @ApiModelProperty(value = "生产线(1或2)")
    @Dict(dicCode = "station")
    private Integer station;
    /**
     * 任务单编号
     */
    @Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private String code;
    /**
     * 施工配合比编号
     */
    @Excel(name = "施工配合比编号", width = 15)
    @ApiModelProperty(value = "施工配合比编号")
    private String recipe;
    /**
     * 砂浆配合比编号
     */
    @Excel(name = "砂浆配合比编号", width = 15)
    @ApiModelProperty(value = "砂浆配合比编号")
    private String morrec;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date dattim;
    /**
     * 车辆编号
     */
    @Excel(name = "车辆编号", width = 15)
    @ApiModelProperty(value = "车辆编号")
    private String vehicle;
    /**
     * 驾驶员
     */
    @Excel(name = "驾驶员", width = 15)
    @ApiModelProperty(value = "驾驶员")
    private String driver;
    /**
     * 生产方量(含砂浆方量)
     */
    @Excel(name = "生产方量(含砂浆方量)", width = 15)
    @ApiModelProperty(value = "生产方量(含砂浆方量)")
    private Double prodmete;
    /**
     * 砂浆方量
     */
    @Excel(name = "砂浆方量", width = 15)
    @ApiModelProperty(value = "砂浆方量")
    private Double mormete;
    /**
     * 累计车次
     */
    @Excel(name = "累计车次", width = 15)
    @ApiModelProperty(value = "累计车次")
    private Integer totvehs;
    /**
     * 累计方量
     */
    @Excel(name = "累计方量", width = 15)
    @ApiModelProperty(value = "累计方量")
    private Double totmete;
    /**
     * 质检员
     */
    @Excel(name = "质检员", width = 15)
    @ApiModelProperty(value = "质检员")
    private String qualitor;
    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String flag;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    @Dict(dicCode = "car_status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态")
    private Integer status1;

    @ApiModelProperty(value = "单号")
    private String danhao;

    @Excel(name = "签收时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "签收时间")
    private Date qianshoutime;

    @Excel(name = "预计到达时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预计到达时间")
    private Date etatime;

    @ApiModelProperty(value = "运输时长", dataType = "String")
    @TableField(exist = false)
    private String shippingLength;

    @ApiModelProperty(value = "运输方量", dataType = "Integer")
    @TableField(exist = false)
    private Double transportVolume;

    @ApiModelProperty(value = "拌合站数据列表", dataType = "List")
    @TableField(exist = false)
    private List<BhzCementBase> bhzCementBasePOList;

    private String fragmentName;
    private String siteName;
    private String lineName;

    /**gps设备编号*/
    @Excel(name = "gps设备名称", width = 15)
    @ApiModelProperty(value = "gps设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String sbjno;

    private Double yunju;


    private Double shuini;
    private Double fmh;
    private Double sa;
    private Double sso;
    private Double sst;
    private Double ssf;
    private Double wjj;
    private Double slj;
    private Double water;

    private Double shuini1;
    private Double fmh1;
    private Double sa1;
    private Double sso1;
    private Double sst1;
    private Double ssf1;
    private Double wjj1;
    private Double slj1;
    private Double water1;

    private Integer bumate;
    private Double buyunju;
    private Integer buyunjugcl;

    private String xinzhen;
    private String gonq;
    private String kehu;
    private String shebei;

    private String fcdName;
}
