package com.trtm.iot.devicemixpileonecfg.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 水泥搅拌桩设备配置
 * @Author: jeecg-boot
 * @Date:   2021-12-01
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_one_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_one_cfg对象", description="水泥搅拌桩设备配置")
public class DeviceMixpileOneCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**主键uuid*/
	@Excel(name = "主键uuid", width = 15)
    @ApiModelProperty(value = "主键uuid")
    private java.lang.String uid;
	/**设备编号*/
	@ApiModelProperty(value = "设备编号")
	@Excel(name="设备名称",dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
	@Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**设计桩长*/
	@Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private java.lang.String designdep;
	/**提钻水泥浆量下限*/
	@Excel(name = "提钻水泥浆量下限", width = 15)
    @ApiModelProperty(value = "提钻水泥浆量下限")
    private java.lang.String upbetonx;
	/**提钻水泥浆量上限*/
	@Excel(name = "提钻水泥浆量上限", width = 15)
    @ApiModelProperty(value = "提钻水泥浆量上限")
    private java.lang.String upbetons;
	/**提钻速度*/
	@Excel(name = "提钻速度下限", width = 15)
    @ApiModelProperty(value = "提钻速度下限")
    private java.lang.String uspeedx;
	/**下钻水泥浆量下限*/
	@Excel(name = "下钻水泥浆量下限", width = 15)
    @ApiModelProperty(value = "下钻水泥浆量下限")
    private java.lang.String downbetonx;
	/**下钻水泥浆量上限*/
	@Excel(name = "下钻水泥浆量上限", width = 15)
    @ApiModelProperty(value = "下钻水泥浆量上限")
    private java.lang.String downbetons;
	/**下钻速度*/
	@Excel(name = "下钻速度下限", width = 15)
    @ApiModelProperty(value = "下钻速度下限")
    private java.lang.String dspeedx;
	/**水泥浆用量*/
	@Excel(name = "水泥浆用量", width = 15)
    @ApiModelProperty(value = "水泥浆用量")
    private java.lang.String cement;
	/**倾角下限*/
	@Excel(name = "倾角下限", width = 15)
    @ApiModelProperty(value = "倾角下限")
    private java.lang.String xx;
	/**倾角上限*/
	@Excel(name = "倾角上限", width = 15)
    @ApiModelProperty(value = "倾角上限")
    private java.lang.String xs;
    /**平均密度*/
    @Excel(name = "平均密度", width = 15)
    @ApiModelProperty(value = "平均密度")
    private java.lang.String density;
	/**水泥搅拌桩预警号码*/
	@Excel(name = "水泥搅拌桩预警号码", width = 15)
    @ApiModelProperty(value = "水泥搅拌桩预警号码")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String jbzphones;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
	@Dict(dicCode = "is_call")
    private java.lang.Integer isCall;
	/**权限配置*/
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "权限配置")
    private java.lang.String sysOrgCode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**是否删除（0：未删除，1：已删除）*/
	@Excel(name = "是否删除（0：未删除，1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private java.lang.Integer isdel;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;

    private java.lang.String dpressx;// 下钻平均压力下限
    private java.lang.String dpress;// 下钻平均压力上限
    private java.lang.String dspeeds;// 下钻平均速度上限
    private java.lang.String upressx;// 提钻平均压力下限
    private java.lang.String upress;// 提钻平均压力上限
    private java.lang.String uspeeds;// 提钻平均速度上限
    private String pileMinelec;//最小电流(每米水泥用量)
    private java.lang.String pileDensity;// 水泥浆比重
    @Dict(dicCode = "sggy")
    private java.lang.Integer sggy;// 施工工艺
}
