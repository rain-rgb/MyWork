package com.trtm.iot.renwudan.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 拌合站任务单进度
 * @Author: jeecg-boot
 * @Date:   2021-06-16
 * @Version: V1.0
 */
@Data
@TableName("renwudan_schedule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="renwudan_schedule对象", description="拌合站任务单进度")
public class RenwudanSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**生产线（0公用 1 第一生产线  2 第二生产线）*/
	@Excel(name = "生产线（0公用 1 第一生产线  2 第二生产线）", width = 15)
    @ApiModelProperty(value = "生产线（0公用 1 第一生产线  2 第二生产线）")
    @Dict(dicCode = "station")
    private java.lang.Integer station;
	/**任务单编号*/
	@Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String code;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String conspos;
	/**浇筑方式*/
	@Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    private java.lang.String pour;
	/**开始生产时间*/
	@Excel(name = "开始生产时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始生产时间")
    private java.util.Date starttim;
	/**结束生产时间*/
	@Excel(name = "结束生产时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束生产时间")
    private java.util.Date endtim;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String betlev;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    private java.lang.String lands;
	/**任务方量*/
	@Excel(name = "任务方量", width = 15)
    @ApiModelProperty(value = "任务方量")
    private java.lang.Double mete;
	/**生产方量*/
	@Excel(name = "生产方量", width = 15)
    @ApiModelProperty(value = "生产方量")
    private java.lang.Double metes;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**是否删除 0未删除 1已删除*/
	@Excel(name = "是否删除 0未删除 1已删除", width = 15)
    @ApiModelProperty(value = "是否删除 0未删除 1已删除")
    private java.lang.Integer isdel;
	/**分部分项节点*/
	@Excel(name = "分部分项节点", width = 15)
    @ApiModelProperty(value = "分部分项节点")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysDepartProject;

    @Excel(name = "百分比", width = 15)
    @ApiModelProperty(value = "百分比")
    private java.lang.Double bfb;

    @Excel(name = "盘数", width = 15)
    @ApiModelProperty(value = "盘数")
    @TableField("dish_count")
    private Integer dishCount;
}
