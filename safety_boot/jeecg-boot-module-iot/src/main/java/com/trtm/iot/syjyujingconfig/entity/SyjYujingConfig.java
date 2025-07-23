package com.trtm.iot.syjyujingconfig.entity;

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
 * @Description: syj_yujing_config
 * @Author: jeecg-boot
 * @Date:   2023-11-23
 * @Version: V1.0
 */
@Data
@TableName("syj_yujing_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="syj_yujing_config对象", description="syj_yujing_config")
public class SyjYujingConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**试验机预警配置表*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "试验机预警配置表")
    private java.lang.Integer id;
	/**uuid*/
	@Excel(name = "uuid", width = 15)
    @ApiModelProperty(value = "uuid")
    private java.lang.String uuid;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
    /**设备类型*/
    @Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private java.lang.Integer sbType;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**预警人*/
	@Excel(name = "预警人", width = 15)
    @ApiModelProperty(value = "预警人")
    private java.lang.String names;
	/**预警号码*/
	@Excel(name = "预警号码", width = 15)
    @ApiModelProperty(value = "预警号码")
    private java.lang.String phones;
	/**是否报警 0:不报警，1:报警*/
	@Excel(name = "是否报警 0:报警，1:不报警", width = 15)
    @ApiModelProperty(value = "是否报警 0:报警，1:不报警")
    private java.lang.Integer isCall;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
}
