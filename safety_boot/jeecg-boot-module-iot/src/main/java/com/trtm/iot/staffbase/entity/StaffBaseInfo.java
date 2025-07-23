package com.trtm.iot.staffbase.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: staff_base_info
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
@Data
@TableName("staff_base_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="staff_base_info对象", description="staff_base_info")
public class StaffBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private java.lang.String userid;
	/**法人单位*/
	@Excel(name = "法人单位", width = 15)
    @ApiModelProperty(value = "法人单位")
    private java.lang.String farendanwei;
	/**建设处*/
	@Excel(name = "建设处", width = 15)
    @ApiModelProperty(value = "建设处")
    private java.lang.String jianshechu;
	/**标段*/
	@Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    private java.lang.String biaoduan;
	/**工区*/
	@Excel(name = "工区", width = 15)
    @ApiModelProperty(value = "工区")
    private java.lang.String gongqu;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String xingming;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String danwei;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String dianhua;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private java.lang.String zhiwu;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
    @ApiModelProperty(value = "证件类型")
    @Dict(dicCode = "zhengjiantype")
    private java.lang.String zhengjiantype;
	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
    private java.lang.String shenfenz;
	/**（到岗状态1必须到岗,2灵活到岗）*/
	@Excel(name = "（到岗状态1必须到岗,2灵活到岗）", width = 15)
    @ApiModelProperty(value = "（到岗状态1必须到岗,2灵活到岗）")
    @Dict(dicCode = "shifoubixudaogang")
    private java.lang.String shifoubixudaogang;
	/**计划进场时间yyy-MM-dd*/
	@Excel(name = "计划进场时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划进场时间yyy-MM-dd")
    private java.util.Date jihuajingchangtime;
	/**计划离场时间yyy-MM-dd*/
	@Excel(name = "计划离场时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划离场时间yyy-MM-dd")
    private java.util.Date jihualichangtime;
	/**进退场状态说明：1未进场2进场3退场*/
	@Excel(name = "进退场状态", width = 15)
    @ApiModelProperty(value = "进退场状态 说明：1未进场2进场3退场")
    @Dict(dicCode = "jintuichangzhuangtai")
    private java.lang.String jintuichangzhuangtai;
	/**实际进场时间yyy-MM-dd*/
	@Excel(name = "实际进场时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际进场时间yyy-MM-dd")
    private java.util.Date shijijinchangtime;
	/**实际退场时间yyy-MM-dd*/
	@Excel(name = "实际退场时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际退场时间")
    private java.util.Date shijituichangtime;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**管理员；实施员*/
	@Excel(name = "管理员；实施员", width = 15)
    @ApiModelProperty(value = "管理员；实施员")
    private java.lang.String renyuantype;
	/**工种*/
	@Excel(name = "工种", width = 15)
    @ApiModelProperty(value = "工种")
    @Dict(dicCode = "gongzhong")
    private java.lang.String gongzhong;
	/**班组*/
	@Excel(name = "班组", width = 15)
    @ApiModelProperty(value = "班组")
    private java.lang.String banzu;
    private java.lang.String chizhengqk;
    private java.lang.String zhicheng;

    private java.lang.Integer pushstatus;
}
