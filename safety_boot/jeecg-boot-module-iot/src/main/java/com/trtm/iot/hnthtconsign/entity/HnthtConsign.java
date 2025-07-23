package com.trtm.iot.hnthtconsign.entity;

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
 * @Description: 检测试验任务单下发信息表
 * @Author: jeecg-boot
 * @Date:   2021-07-09
 * @Version: V1.0
 */
@Data
@TableName("hntht_consign")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hntht_consign对象", description="检测试验任务单下发信息表")
public class HnthtConsign implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**任务单号*/
	@Excel(name = "任务单号", width = 15)
    @ApiModelProperty(value = "任务单号")
    private java.lang.String uuid;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectname;
	/**构件名称*/
	@Excel(name = "构件名称", width = 15)
    @ApiModelProperty(value = "构件名称")
    @Dict(dicCode = "component")
    private java.lang.String component;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbwguid;
	/**施工部位名称*/
	@Excel(name = "施工部位名称", width = 15)
    @ApiModelProperty(value = "施工部位名称")
    private java.lang.String sgbwname;
	/**张拉任务状态码：0：未使用  1：已使用*/
	@Excel(name = "张拉任务状态码：0：未使用  1：已使用", width = 15)
    @ApiModelProperty(value = "张拉任务状态码：0：未使用  1：已使用")
    private java.lang.String status;
	/**试验日期*/
	@Excel(name = "试验日期", width = 15)
    @ApiModelProperty(value = "试验日期")
    private java.lang.String zldate;
	/**组织机构id*/
	@Excel(name = "组织机构id", width = 15)
    @ApiModelProperty(value = "组织机构id")
    private java.lang.String departid;
	/**sytypeid*/
	@Excel(name = "sytypeid", width = 15)
    @ApiModelProperty(value = "sytypeid")
    private java.lang.String sytypeid;
	/**departname*/
	@Excel(name = "departname", width = 15)
    @ApiModelProperty(value = "departname")
    private java.lang.String departname;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**issend*/
    @Excel(name = "issend", width = 15)
    @ApiModelProperty(value = "issend")
    private java.lang.String issend;
    /**设备厂家*/
    @Excel(name = "shebeichangjia", width = 15)
    @ApiModelProperty(value = "shebeichangjia")
    @Dict(dicCode = "shebeichangjia")
    private java.lang.String shebeichangjia;
    /**设备编号*/
    @Excel(name = "shebeibianhao", width = 15)
    @ApiModelProperty(value = "shebeibianhao")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
    private java.lang.String jcdw;
    private java.lang.String sgdw;
    private java.lang.Integer isdel;
    private java.lang.String treeid;// 分部分项id
    private String gxwbs;// 工序id
    private java.lang.Integer pushStatus;
}
