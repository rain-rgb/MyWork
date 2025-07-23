package com.trtm.iot.weiyan.entity;

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
 * @Description: 围岩量测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Data
@TableName("weiyan_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="weiyan_base对象", description="围岩量测主表")
public class WeiyanBase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**唯一ID*/
	@Excel(name = "唯一ID", width = 15)
    @ApiModelProperty(value = "唯一ID")
    private java.lang.String batchNo;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String jobLocation;
	/**切面名称*/
	@Excel(name = "切面名称", width = 15)
    @ApiModelProperty(value = "切面名称")
    private java.lang.String sectionName;
	/**切面里程*/
	@Excel(name = "切面里程", width = 15)
    @ApiModelProperty(value = "切面里程")
    private java.lang.String sectionLength;
	/**切面宽度*/
	@Excel(name = "切面宽度", width = 15)
    @ApiModelProperty(value = "切面宽度")
    private java.lang.String sectionWidth;
	/**开挖方式*/
	@Excel(name = "开挖方式", width = 15)
    @ApiModelProperty(value = "开挖方式")
    private java.lang.String excavationType;
	/**围岩级别*/
	@Excel(name = "围岩级别", width = 15)
    @ApiModelProperty(value = "围岩级别")
    private java.lang.String wallRockLevel;
	/**测点*/
	@Excel(name = "测点", width = 15)
    @ApiModelProperty(value = "测点")
    private java.lang.String measuringPoint;
	/**测量值（单位m）*/
	@Excel(name = "测量值（单位m）", width = 15)
    @ApiModelProperty(value = "测量值（单位m）")
    private java.lang.Double measuringNumber;
	/**单次变形量（单位mm）*/
	@Excel(name = "单次变形量（单位mm）", width = 15)
    @ApiModelProperty(value = "单次变形量（单位mm）")
    private java.lang.Double onceDeformation;
	/**间隔时间  （单位天）*/
	@Excel(name = "间隔时间  （单位天）", width = 15)
    @ApiModelProperty(value = "间隔时间  （单位天）")
    private java.lang.Double intervalTime;
	/**变形速率*/
	@Excel(name = "变形速率", width = 15)
    @ApiModelProperty(value = "变形速率")
    private java.lang.Double deformationRate;
	/**单次是否超标 (0:未超标，1:超标)*/
	@Excel(name = "单次是否超标 (0:未超标，1:超标)", width = 15)
    @ApiModelProperty(value = "单次是否超标 (0:未超标，1:超标)")
    private java.lang.Integer onceStatus;
	/**累计是否超标  (0:未超标，1:超标)*/
	@Excel(name = "累计是否超标  (0:未超标，1:超标)", width = 15)
    @ApiModelProperty(value = "累计是否超标  (0:未超标，1:超标)")
    private java.lang.Integer accumulatedStatus;
	/**累计变形量单位（mm）*/
	@Excel(name = "累计变形量单位（mm）", width = 15)
    @ApiModelProperty(value = "累计变形量单位（mm）")
    private java.lang.Double accumulatedDeformation;
	/**是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常*/
	@Excel(name = "是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常", width = 15)
    @ApiModelProperty(value = "是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常")
    private java.lang.Integer alertstate;
	/**测量时间*/
	@Excel(name = "测量时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "测量时间")
    private java.util.Date birthTime;
	/**数据上传时间*/
	@Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date uploadTime;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "is_use")
    private java.lang.Integer isUse;
	/**报警级别(0,代表合格，1代表黄色报警，21代表红色报警)*/
	@Excel(name = "报警级别(0,代表合格，1代表黄色报警，21代表红色报警)", width = 15)
    @ApiModelProperty(value = "报警级别(0,代表合格，1代表黄色报警，21代表红色报警)")
    private java.lang.Integer callLevel;
	/**检测类型（0:速率，1累计）*/
	@Excel(name = "检测类型（0:速率，1累计）", width = 15)
    @ApiModelProperty(value = "检测类型（0:速率，1累计）")
    private java.lang.Integer superviseType;
	/**callSum*/
	@Excel(name = "callSum", width = 15)
    @ApiModelProperty(value = "callSum")
    private java.lang.Integer callSum;
	/**间隔时间(距离初次测试时间)*/
	@Excel(name = "间隔时间(距离初次测试时间)", width = 15)
    @ApiModelProperty(value = "间隔时间(距离初次测试时间)")
    private java.lang.Integer intervalFirstTime;
	/**测量状态（0：正常 ,1：重新布设）*/
	@Excel(name = "测量状态（0：正常 ,1：重新布设）", width = 15)
    @ApiModelProperty(value = "测量状态（0：正常 ,1：重新布设）")
    private java.lang.String measuringStatus;
	/**超标问题中文描述*/
	@Excel(name = "超标问题中文描述", width = 15)
    @ApiModelProperty(value = "超标问题中文描述")
    private java.lang.String problemRecord;
	/**临建再统计后的测点*/
	@Excel(name = "临建再统计后的测点", width = 15)
    @ApiModelProperty(value = "临建再统计后的测点")
    private java.lang.String measuringPointLj;
}
