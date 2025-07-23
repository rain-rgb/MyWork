package com.trtm.iot.beamorder.entity;

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
 * @Description: 粱订单信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-25
 * @Version: V1.0
 */
@Data
@TableName("beam_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="beam_order对象", description="粱订单信息表")
public class BeamOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private java.lang.String orderno;
	/**粱型*/
	@Excel(name = "粱型", width = 15)
    @ApiModelProperty(value = "粱型")
    @Dict(dicCode = "beamType")
    private java.lang.String beamType;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String constructionSite;
	/**施工部位id*/
	@Excel(name = "施工部位id", width = 15)
    @ApiModelProperty(value = "施工部位id")
    private java.lang.String constructionSiteid;
	/**交付日期*/
	@Excel(name = "交付日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "交付日期")
    private java.util.Date deliveryDate;
	/**梁数量*/
	@Excel(name = "梁数量", width = 15)
    @ApiModelProperty(value = "梁数量")
    private java.lang.Integer beamNum;
	/**梁场(设备编号)*/
	@Excel(name = "梁场(设备编号)", width = 15)
    @ApiModelProperty(value = "梁场(设备编号)")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**制梁要求*/
	@Excel(name = "制梁要求", width = 15)
    @ApiModelProperty(value = "制梁要求")
    private java.lang.String remark;
	/**订单进度*/
	@Excel(name = "订单进度", width = 15)
    @ApiModelProperty(value = "订单进度")
    private java.lang.String orderProgress;
	/**0：未开始 1：生产中 2：已完成 3：滞后*/
	@Excel(name = "0：未开始 1：生产中 2：已完成 3：滞后", width = 15)
    @ApiModelProperty(value = "0：未开始 1：生产中 2：已完成 3：滞后")
    private java.lang.Integer orderStatus;
	/**是否删除（0：否 1：是）*/
	@Excel(name = "是否删除（0：否 1：是）", width = 15)
    @ApiModelProperty(value = "是否删除（0：否 1：是）")
    private java.lang.Integer isdel;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**订单接收人*/
	@Excel(name = "订单接收人", width = 15)
    @ApiModelProperty(value = "订单接收人")
    private java.lang.String orderRecipient;
	/**顶板宽*/
	@Excel(name = "顶板宽", width = 15)
    @ApiModelProperty(value = "顶板宽")
    private java.lang.String roofwid;
	/**底板宽*/
	@Excel(name = "底板宽", width = 15)
    @ApiModelProperty(value = "底板宽")
    private java.lang.String bottomwid;
	/**梁高*/
	@Excel(name = "梁高", width = 15)
    @ApiModelProperty(value = "梁高")
    private java.lang.String beamhig;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String uuid;

	private java.lang.String treeid;//施工部位节点id

    private java.lang.Integer issend;//是否发送短信通知接收人(0：否,1：是)
}
