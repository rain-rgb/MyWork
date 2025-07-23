package com.trtm.iot.car.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Data
@TableName("scheduling_car")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "scheduling_car对象", description = "调度车辆")
public class SchedulingCar implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
    /**
     * Erp端ID
     */
    @Excel(name = "Erp端ID", width = 15)
    @ApiModelProperty(value = "Erp端ID")
    private java.lang.Integer erpid;
    /**
     * 生产线(1或2)
     */
    @Excel(name = "生产线(1或2)", width = 15)
    @ApiModelProperty(value = "生产线(1或2)")
    @Dict(dicCode = "station")
    private java.lang.Integer station;
    /**
     * 任务单编号
     */
    @Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    @Dict(dictTable = "bhzrenwudan", dicText = "ConsPos", dicCode = "code")
    private java.lang.String code;
    /**
     * 施工配合比编号
     */
    @Excel(name = "施工配合比编号", width = 15)
    @ApiModelProperty(value = "施工配合比编号")
    private java.lang.String recipe;
    /**
     * 砂浆配合比编号
     */
    @Excel(name = "砂浆配合比编号", width = 15)
    @ApiModelProperty(value = "砂浆配合比编号")
    private java.lang.String morrec;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date dattim;
    /**
     * 车辆编号
     */
    @Excel(name = "车辆编号", width = 15)
    @ApiModelProperty(value = "车辆编号")
    @Dict(dictTable = "shebei_info", dicText = "sbjno", dicCode = "sbname")
    private java.lang.String vehicle;
    /**
     * 驾驶员
     */
    @Excel(name = "驾驶员", width = 15)
    @ApiModelProperty(value = "驾驶员")
    private java.lang.String driver;
    /**
     * 生产方量(含砂浆方量)
     */
    @Excel(name = "生产方量(含砂浆方量)", width = 15)
    @ApiModelProperty(value = "生产方量(含砂浆方量)")
    private java.lang.Double prodmete;
    /**
     * 砂浆方量
     */
    @Excel(name = "砂浆方量", width = 15)
    @ApiModelProperty(value = "砂浆方量")
    private java.lang.Double mormete;
    /**
     * 累计车次
     */
    @Excel(name = "累计车次", width = 15)
    @ApiModelProperty(value = "累计车次")
    private java.lang.Integer totvehs;
    /**
     * 累计方量
     */
    @Excel(name = "累计方量", width = 15)
    @ApiModelProperty(value = "累计方量")
    private java.lang.Double totmete;
    /**
     * 质检员
     */
    @Excel(name = "质检员", width = 15)
    @ApiModelProperty(value = "质检员")
    private java.lang.String qualitor;
    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private java.lang.String flag;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;

    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    @Dict(dicCode = "car_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;

    @ApiModelProperty(value = "状态")
    private java.lang.Integer status1;

    @ApiModelProperty(value = "单号")
    private java.lang.String danhao;

    @Excel(name = "签收时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签收时间")
    private java.util.Date qianshoutime;

    @Excel(name = "预计到达时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计到达时间")
    private java.util.Date etatime;

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

    /**到工作面时间*/
    @Excel(name = "到工作面时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到工作面时间")
    private java.util.Date dgzmsj;
    /**卸料时间*/
    @Excel(name = "卸料时间", width = 15)
    @ApiModelProperty(value = "卸料时间")
    private java.lang.String xlsj;
    /**卸完时间*/
    @Excel(name = "卸完时间", width = 15)
    @ApiModelProperty(value = "卸完时间")
    private java.lang.String xwsj;
    /**账期*/
    @Excel(name = "账期", width = 15)
    @ApiModelProperty(value = "账期")
    private java.lang.String zq;

    private Integer yunju;
    private String taiz;//合同编号
    private Integer tbStatus;
    /**工程名称*/
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projname;
    /**施工部位*/
    @Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String conspos;
    /**工程级别*/
    @Excel(name = "工程级别", width = 15)
    @ApiModelProperty(value = "工程级别")
    private String projgrade;

    private Integer  overhandle;
//    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String receiver;
}
