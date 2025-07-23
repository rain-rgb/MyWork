package com.trtm.iot.jikeng.entity;

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
 * @Description: jikeng_weiy_cfg
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Data
@TableName("jikeng_weiy_cfg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jikeng_weiy_cfg对象", description="jikeng_weiy_cfg")
public class JikengWeiyCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**测点名称*/
	@Excel(name = "测点名称", width = 15)
    @ApiModelProperty(value = "测点名称")
    private java.lang.String cedianname;
	/**监测编码*/
	@Excel(name = "监测编码", width = 15)
    @ApiModelProperty(value = "监测编码")
    private java.lang.String jiancebianma;
	/**监测数据*/
	@Excel(name = "监测数据", width = 15)
    @ApiModelProperty(value = "监测数据")
    private java.lang.String jsondata;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;
	/**组织机构code*/
    @ApiModelProperty(value = "组织机构code")
    private java.lang.String sysOrgCode;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**监测类别：SY:渗透水压力监测;GP:表面位移监测;NW:水位监测*/
	@Excel(name = "监测类别：SY:渗透水压力监测;GP:表面位移监测;NW:水位监测", width = 15)
    @ApiModelProperty(value = "监测类别：SY:渗透水压力监测;GP:表面位移监测;NW:水位监测")
    private java.lang.String jiancetype;
	/**SY:渗透水压力监测时 表示  围堰顶部高程*/
	@Excel(name = "SY:渗透水压力监测时 表示  围堰顶部高程", width = 15)
    @ApiModelProperty(value = "SY:渗透水压力监测时 表示  围堰顶部高程")
    private java.lang.String gaocheng;
	/**SY:渗透水压力监测时 表示 孔深*/
	@Excel(name = "SY:渗透水压力监测时 表示 孔深", width = 15)
    @ApiModelProperty(value = "SY:渗透水压力监测时 表示 孔深")
    private java.lang.String kongshen;
	/**SY:渗透水压力监测时 表示 见水*/
	@Excel(name = "SY:渗透水压力监测时 表示 见水", width = 15)
    @ApiModelProperty(value = "SY:渗透水压力监测时 表示 见水")
    private java.lang.String jianshui;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String jingdu;
	/**维度*/
	@Excel(name = "维度", width = 15)
    @ApiModelProperty(value = "维度")
    private java.lang.String weidu;
}
