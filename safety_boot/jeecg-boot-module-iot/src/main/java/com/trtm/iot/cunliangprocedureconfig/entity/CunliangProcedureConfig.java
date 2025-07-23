package com.trtm.iot.cunliangprocedureconfig.entity;

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
 * @Description: 梁场台座管理表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
@Data
@TableName("cunliang_procedure_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cunliang_procedure_config对象", description="梁场台座管理表信息")
public class CunliangProcedureConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**梁场设备编号*/
	@Excel(name = "梁场设备编号", width = 15)
    @ApiModelProperty(value = "梁场设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**行*/
	@Excel(name = "行", width = 15)
    @ApiModelProperty(value = "行")
    private java.lang.String lianghang;
	/**列*/
	@Excel(name = "列", width = 15)
    @ApiModelProperty(value = "列")
    private java.lang.String lianglie;
	/**总层数*/
	@Excel(name = "总层数", width = 15)
    @ApiModelProperty(value = "总层数")
    private java.lang.String cengshu;
	/**当前梁坐的名称*/
	@Excel(name = "当前梁坐的名称", width = 15)
    @ApiModelProperty(value = "当前梁坐的名称")
    private java.lang.String liangzuoname;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**组织机构编码*/
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;

    private java.lang.Integer status;
    private java.lang.Integer status1;
    private java.lang.String dangqianceng;
    private java.lang.String uuid;//唯一id(设备(继电器)id)
    private java.lang.Integer switchsta;//开关状态 0 关 1 开
    private java.util.Date switchtime;//开关时间
    private java.lang.Integer statisc;//品茗数据推送0未推送 1推送 2推送失败
}
