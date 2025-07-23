package com.trtm.iot.zhiliangtaizuocfg.entity;

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
 * @Description: 制梁台座配置表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
@Data
@TableName("zhiliang_taizuo_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zhiliang_taizuo_cfg对象", description="制梁台座配置表信息")
public class ZhiliangTaizuoCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**台座编号*/
	@Excel(name = "台座编号", width = 15)
    @ApiModelProperty(value = "台座编号")
    private java.lang.String taizuono;
	/**台座名称*/
	@Excel(name = "台座名称", width = 15)
    @ApiModelProperty(value = "台座名称")
    private java.lang.String taizuoname;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**testid*/
    @Excel(name = "testid", width = 15)
    @ApiModelProperty(value = "testid")
    private Integer testid;
    /**梁场设备编号*/
    @Excel(name = "梁场设备编号", width = 15)
    @ApiModelProperty(value = "梁场设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;

    private java.lang.String uuid;

    private java.lang.Integer status;//状态
    /**工序序号(工序字典值)*/
    @Excel(name = "工序序号(工序字典值)", width = 15)
    @ApiModelProperty(value = "工序序号(工序字典值)")
    @Dict(dicCode = "xuhao")
    private java.lang.Integer xuhao;
    private java.lang.String beamno;
    private java.lang.Integer statisc;//品茗数据推送0未推送 1推送 2推送失败
    private java.lang.Integer lzplzt;
    private java.lang.String aijk;
}
