package com.trtm.iot.trzhanglaconfigure.entity;

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
 * @Description: 张拉预警配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_configure")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_configure对象", description="张拉预警配置表")
public class TrZhanglaConfigure implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**是否预警*/
	@Excel(name = "是否预警", width = 15)
    @ApiModelProperty(value = "是否报警:0=报警,1=不报警")
    @Dict(dicCode = "is_call")
    private java.lang.String alertOrNot;
	/**张拉初级预警范围1*/
	@Excel(name = "张拉初级预警范围1", width = 15)
    @ApiModelProperty(value = "张拉初级预警范围1")
    private java.lang.Double tensionChu;
	/**张拉中级预警*/
	@Excel(name = "张拉中级预警", width = 15)
    @ApiModelProperty(value = "张拉中级预警")
    private java.lang.Double tensionZhong;
	/**张拉高级预警*/
	@Excel(name = "张拉高级预警", width = 15)
    @ApiModelProperty(value = "张拉高级预警")
    private java.lang.Double tensionGao;
	/**伸长率初级*/
	@Excel(name = "伸长率初级", width = 15)
    @ApiModelProperty(value = "伸长率初级")
    private java.lang.Double elongationChu;
	/**伸长率中级*/
	@Excel(name = "伸长率中级", width = 15)
    @ApiModelProperty(value = "伸长率中级")
    private java.lang.Double elongationZhong;
	/**伸长率高级*/
	@Excel(name = "伸长率高级", width = 15)
    @ApiModelProperty(value = "伸长率高级")
    private java.lang.Double elongationGao;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String namescj;//初级

    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String nameszj;//中级

    @Dict(dictTable = "bhz_phone", dicText = "names", dicCode = "uid")
    private java.lang.String namesgj;//高级
    private java.lang.Integer yesornot;
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
    private java.lang.String names;
    private java.lang.String phones;
}
