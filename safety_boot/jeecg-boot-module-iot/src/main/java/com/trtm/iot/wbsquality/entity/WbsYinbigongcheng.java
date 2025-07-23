package com.trtm.iot.wbsquality.entity;

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
 * @Description: wbs_yinbigongcheng
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Data
@TableName("wbs_yinbigongcheng")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wbs_yinbigongcheng对象", description="wbs_yinbigongcheng")
public class WbsYinbigongcheng implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**节段*/
	@Excel(name = "节段", width = 15)
    @ApiModelProperty(value = "节段")
    private java.lang.String jieduan;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String zhuanghao;
	/**验收内容*/
	@Excel(name = "验收内容", width = 15)
    @ApiModelProperty(value = "验收内容")
    private java.lang.String yanshouneirong;
	/**报验时间*/
	@Excel(name = "报验时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报验时间")
    private java.util.Date baoyantime;
	/**验收时间*/
	@Excel(name = "验收时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "验收时间")
    private java.util.Date yanshoutime;
	/**验收单位*/
	@Excel(name = "验收单位", width = 15)
    @ApiModelProperty(value = "验收单位")
    private java.lang.String yanshouren;
	/**报验单位*/
	@Excel(name = "报验单位", width = 15)
    @ApiModelProperty(value = "报验单位")
    private java.lang.String baoyanren;
	/**图片，分隔*/
	@Excel(name = "图片，分隔", width = 15)
    @ApiModelProperty(value = "图片，分隔")
    private java.lang.String pics;
	/**视频*/
	@Excel(name = "视频", width = 15)
    @ApiModelProperty(value = "视频")
    private java.lang.String video;
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
}
