package com.trtm.iot.lowstrain_m.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 低应变主表
 * @Author: jeecg-boot
 * @Date:   2024-08-14
 * @Version: V1.0
 */
@ApiModel(value="lowstrain_m对象", description="低应变主表")
@Data
@TableName("lowstrain_m")
public class LowstrainM implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**报检单ID*/
	@Excel(name = "报检单ID", width = 15)
    @ApiModelProperty(value = "报检单ID")
    private java.lang.String inspectionformid;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    private java.lang.String vendorid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String machineno;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String sgbw;
	/**流水号(任务单编号)*/
	@Excel(name = "流水号(任务单编号)", width = 15)
    @ApiModelProperty(value = "流水号(任务单编号)")
    private java.lang.String serialno;
	/**桩id*/
	@Excel(name = "桩id", width = 15)
    @ApiModelProperty(value = "桩id")
    private java.lang.Integer pileid;
	/**桩编号*/
	@Excel(name = "桩编号", width = 15)
    @ApiModelProperty(value = "桩编号")
    private java.lang.String pileno;
	/**开始检测时间(yyyy-MM-dd HH:mm:ss)*/
	@Excel(name = "开始检测时间(yyyy-MM-dd HH:mm:ss)", width = 15)
    @ApiModelProperty(value = "开始检测时间(yyyy-MM-dd HH:mm:ss)")
    private java.lang.String starttime;
	/**实际桩径（mm）*/
	@Excel(name = "实际桩径（mm）", width = 15)
    @ApiModelProperty(value = "实际桩径（mm）")
    private java.lang.Integer pilediameter;
	/**实际桩长（m）*/
	@Excel(name = "实际桩长（m）", width = 15)
    @ApiModelProperty(value = "实际桩长（m）")
    private java.math.BigDecimal pilelength;
	/**预设波速(m/s)*/
	@Excel(name = "预设波速(m/s)", width = 15)
    @ApiModelProperty(value = "预设波速(m/s)")
    private java.lang.Integer velocity;
	/**检测数量*/
	@Excel(name = "检测数量", width = 15)
    @ApiModelProperty(value = "检测数量")
    private java.lang.Integer samplecount;
	/**桩完整性*/
	@Excel(name = "桩完整性", width = 15)
    @ApiModelProperty(value = "桩完整性")
    private java.lang.String intergrityevaluation;
	/**桩等级*/
	@Excel(name = "桩等级", width = 15)
    @ApiModelProperty(value = "桩等级")
    private java.lang.Integer integrityclassification;
	/**有效数据总条数*/
	@Excel(name = "有效数据总条数", width = 15)
    @ApiModelProperty(value = "有效数据总条数")
    private java.lang.Integer validdatacount;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String gpslongitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String gpslatitude;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15)
    @ApiModelProperty(value = "上传时间")
    private java.lang.String uploaded;
}
