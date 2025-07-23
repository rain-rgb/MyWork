package com.trtm.iot.bhzSwRecipe.entity;

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
 * @Description: 水稳理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bhz_sw_recipe")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_sw_recipe对象", description="水稳理论配合比主表")
public class BhzSwRecipe implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type =IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**llysb*/
	@Excel(name = "llysb", width = 15)
    @ApiModelProperty(value = "llysb")
    private java.lang.String llysb;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**配比名称*/
	@Excel(name = "配比名称", width = 15)
    @ApiModelProperty(value = "配比名称")
    @Dict(dicCode = "swlilunname")
    private java.lang.String lilunname;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    @Dict(dicCode = "swllbuwei")
    private java.lang.String llbuwei;
	/**录入时间*/
	@Excel(name = "录入时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "录入时间")
    private java.util.Date llshijian;
	/**默认*/
	@Excel(name = "默认", width = 15)
    @ApiModelProperty(value = "默认")
    private java.lang.Integer llmoren;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String llbeizhu;
	/**llkd*/
	@Excel(name = "llkd", width = 15)
    @ApiModelProperty(value = "llkd")
    private java.lang.String llkd;
	/**llhd*/
	@Excel(name = "llhd", width = 15)
    @ApiModelProperty(value = "llhd")
    private java.lang.String llhd;
	/**llmd*/
	@Excel(name = "llmd", width = 15)
    @ApiModelProperty(value = "llmd")
    private java.lang.String llmd;
	/**混合料类型*/
	@Excel(name = "混合料类型", width = 15)
    @ApiModelProperty(value = "混合料类型")
    @Dict(dicCode = "swhhl")
    private java.lang.String hhllx;
	/**生产配合比审批状态*/
	@Excel(name = "生产配合比审批状态", width = 15)
    @ApiModelProperty(value = "生产配合比审批状态")
    private java.lang.String phbsp;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String gcmc;
	/**配合比编号*/
	@Excel(name = "配合比编号", width = 15)
    @ApiModelProperty(value = "配合比编号")
    private java.lang.String phbid;
	/**组织机构id*/
	@Excel(name = "组织机构id", width = 15)
    @ApiModelProperty(value = "组织机构id")
    private java.lang.String departid;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private java.lang.String zhuziid;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.String ts;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectid;
}
