package com.trtm.iot.pipepileYujing.entity;

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
 * @Description: 管桩预警配置表
 * @Author: jeecg-boot
 * @Date:   2022-09-15
 * @Version: V1.0
 */
@Data
@TableName("pipepile_yujing")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pipepile_yujing对象", description="管桩预警配置表")
public class PipepileYujing implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**施工桩长 ±0.5m*/
	@Excel(name = "施工桩长 ±0.5m", width = 15)
    @ApiModelProperty(value = "施工桩长 ±0.5m")
    private Integer pileRealdep;
    /**倾角 最小垂直度 ≤1%*/
    @Excel(name = "倾角 最小垂直度 ≤1%", width = 15)
    @ApiModelProperty(value = "倾角 最小垂直度 ≤1%")
    private Integer pileYx;
	/**倾角 最大垂直度 ≤1%*/
	@Excel(name = "倾角 最大垂直度 ≤1%", width = 15)
    @ApiModelProperty(value = "倾角 最大垂直度 ≤1%")
    private Integer pileY;
	/**桩间距*/
	@Excel(name = "桩间距", width = 15)
    @ApiModelProperty(value = "桩间距")
    private String pileAway;
	/**桩尖监测 */
	@Excel(name = "桩尖监测 ", width = 15)
    @ApiModelProperty(value = "桩尖监测 ")
    private String pileJiance;
	/**压桩力*/
	@Excel(name = "压桩力", width = 15)
    @ApiModelProperty(value = "压桩力")
    private String pilePress;
	/**预警手机号*/
	@Excel(name = "预警手机号", width = 15)
    @ApiModelProperty(value = "预警手机号")
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private String phone;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeiNo;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    private String stadate;
    private Integer isCall;
    /**处置负责人*/
    @Excel(name = "处置负责人", width = 15)
    @ApiModelProperty(value = "处置负责人")
    private java.lang.String czperson;
    /**审核负责人*/
    @Excel(name = "审核负责人", width = 15)
    @ApiModelProperty(value = "审核负责人")
    private java.lang.String shperson;
    /**审批负责人*/
    @Excel(name = "审批负责人", width = 15)
    @ApiModelProperty(value = "审批负责人")
    private java.lang.String spperson;
}
