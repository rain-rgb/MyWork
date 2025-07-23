package com.trtm.iot.yajiangrenwudan.entity;

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
 * @Description: 压浆任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-08
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang_renwudan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_yajiang_renwudan对象", description="压浆任务单")
public class TrYajiangRenwudan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**任务单号*/
	@Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private java.lang.String uuid;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectname;
	/**梁场*/
	@Excel(name = "梁场", width = 15)
    @ApiModelProperty(value = "梁场")
    private java.lang.String girderplace;
	/**构件名称（梁名称）*/
	@Excel(name = "构件名称（梁名称）", width = 15)
    @ApiModelProperty(value = "构件名称（梁名称）")
    private java.lang.String component;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbwuuid;
	/**施工部位名称*/
	@Excel(name = "施工部位名称", width = 15)
    @ApiModelProperty(value = "施工部位名称")
    private java.lang.String sgbwname;
	/**台座*/
	@Excel(name = "台座", width = 15)
    @ApiModelProperty(value = "台座")
    private java.lang.String pedestal;
	/**压浆任务状态码：0：未使用  1：已使用*/
	@Excel(name = "压浆任务状态码：0：未使用  1：已使用", width = 15)
    @ApiModelProperty(value = "压浆任务状态码：0：未使用  1：已使用")
    private java.lang.Integer status;
	/**压浆日期*/
	@Excel(name = "压浆日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "压浆日期")
    private java.util.Date yjdate;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String departid;
	/**orgcode*/
	@Excel(name = "orgcode", width = 15)
    @ApiModelProperty(value = "orgcode")
    private java.lang.String orgcode;
	/**departname*/
	@Excel(name = "departname", width = 15)
    @ApiModelProperty(value = "departname")
    private java.lang.String departname;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**所属组织机构*/
    @ApiModelProperty(value = "所属组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
    private java.lang.String treeid;//施工部位节点id
    private java.lang.Integer isruicang;//瑞苍内网数据是否推送 0 未推送 1 已推送
    /**制梁任务单编号*/
    @Excel(name = "制梁任务单编号", width = 15)
    @ApiModelProperty(value = "制梁任务单编号")
    private java.lang.String code;
    private java.lang.String lljl;
    private java.lang.String sjmd;
    private java.lang.String sjyl;
    /**梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)*/
    @Excel(name = "梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)", width = 15)
    @ApiModelProperty(value = "梁型(0：箱型梁；1：T型梁；2：槽形梁；3：空心板梁 ；10：其他)")
    @Dict(dicCode = "lx")
    private java.lang.Integer lx;
    /**孔道数*/
    @Excel(name = "孔道数", width = 15)
    @ApiModelProperty(value = "孔道数")
    private java.lang.String kds;
    /**孔道类型(1:曲线或横向孔道;2:竖向孔道;3:超长孔道)*/
    @Excel(name = "孔道类型(1:曲线或横向孔道;2:竖向孔道;3:超长孔道)", width = 15)
    @ApiModelProperty(value = "孔道类型(1:曲线或横向孔道;2:竖向孔道;3:超长孔道)")
    private java.lang.Integer kdlx;

    private String gxwbs;

}
