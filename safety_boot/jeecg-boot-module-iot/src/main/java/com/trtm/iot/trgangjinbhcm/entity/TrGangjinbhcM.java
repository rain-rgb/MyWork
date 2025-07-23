package com.trtm.iot.trgangjinbhcm.entity;

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
 * @Description: 钢保数据检测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Data
@TableName("tr_gangjinbhc_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_gangjinbhc_m对象", description="钢保数据检测主表")
public class TrGangjinbhcM implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**构件序号*/
	@Excel(name = "构件序号", width = 15)
    @ApiModelProperty(value = "构件序号")
    private java.lang.String point;
	/**1:厚度、2:波形、3:剖面、4:网格、5:规范*/
	@Excel(name = "1:厚度、2:波形、3:剖面、4:网格、5:规范", width = 15)
    @ApiModelProperty(value = "1:厚度、2:波形、3:剖面、4:网格、5:规范")
    private java.lang.Integer type;
	/**检测唯一ID*/
	@Excel(name = "检测唯一ID", width = 15)
    @ApiModelProperty(value = "检测唯一ID")
    private java.lang.String testid;
	/**现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他*/
	@Excel(name = "现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他", width = 15)
    @ApiModelProperty(value = "现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他")
    private java.lang.String targettype;
	/**测点总数*/
	@Excel(name = "测点总数", width = 15)
    @ApiModelProperty(value = "测点总数")
    private java.lang.Integer zonecount;
	/**合格率*/
	@Excel(name = "合格率", width = 15)
    @ApiModelProperty(value = "合格率")
    private java.lang.Double passrate;
	/**检测位置*/
	@Excel(name = "检测位置", width = 15)
    @ApiModelProperty(value = "检测位置")
    private java.lang.String checklocation;
	/**检测人*/
	@Excel(name = "检测人", width = 15)
    @ApiModelProperty(value = "检测人")
    private java.lang.String testerid;
	/**检测时间*/
	@Excel(name = "检测时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检测时间")
    private java.util.Date checktime;
	/**主筋直径mm*/
	@Excel(name = "主筋直径mm", width = 15)
    @ApiModelProperty(value = "主筋直径mm")
    private java.lang.Integer masterdiameter;
	/**主筋间距mm*/
	@Excel(name = "主筋间距mm", width = 15)
    @ApiModelProperty(value = "主筋间距mm")
    private java.lang.Integer masterspacing;
	/**设计厚度mm*/
	@Excel(name = "设计厚度mm", width = 15)
    @ApiModelProperty(value = "设计厚度mm")
    private java.lang.Integer designthickness;
	/**纵向设计厚度*/
	@Excel(name = "纵向设计厚度", width = 15)
    @ApiModelProperty(value = "纵向设计厚度")
    private java.lang.Integer zdesignthickness;
	/**箍筋直径mm*/
	@Excel(name = "箍筋直径mm", width = 15)
    @ApiModelProperty(value = "箍筋直径mm")
    private java.lang.Integer subdiameter;
	/**箍筋间距mm*/
	@Excel(name = "箍筋间距mm", width = 15)
    @ApiModelProperty(value = "箍筋间距mm")
    private java.lang.Integer subspacing;
	/**曲面直径*/
	@Excel(name = "曲面直径", width = 15)
    @ApiModelProperty(value = "曲面直径")
    private java.lang.Integer curveddiameter;
	/**任务单NO*/
	@Excel(name = "任务单NO", width = 15)
    @ApiModelProperty(value = "任务单NO")
    private java.lang.String code;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;

	private java.lang.String projectname;

    private java.lang.String sgbw;

    private java.lang.Integer istuisong;

    private java.lang.String file;//附件
    private java.lang.String hege;
    private java.lang.String xiuzheng;
    private java.lang.String piancha;
    private java.lang.Integer isbaogao;

}
