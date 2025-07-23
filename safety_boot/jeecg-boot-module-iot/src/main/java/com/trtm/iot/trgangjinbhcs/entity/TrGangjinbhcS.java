package com.trtm.iot.trgangjinbhcs.entity;

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
 * @Description: 钢保检测子表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Data
@TableName("tr_gangjinbhc_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_gangjinbhc_s对象", description="钢保检测子表")
public class TrGangjinbhcS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**关联主表testId*/
	@Excel(name = "关联主表testId", width = 15)
    @ApiModelProperty(value = "关联主表testId")
    private java.lang.String testid;
	/**厚度mm*/
	@Excel(name = "厚度mm", width = 15)
    @ApiModelProperty(value = "厚度mm")
    private java.lang.Integer thickness;
	/**距离mm*/
	@Excel(name = "距离mm", width = 15)
    @ApiModelProperty(value = "距离mm")
    private java.lang.Integer distance;
	/**厚度 16进制（设备上传原始数据）*/
	@Excel(name = "厚度 16进制（设备上传原始数据）", width = 15)
    @ApiModelProperty(value = "厚度 16进制（设备上传原始数据）")
    private java.lang.String strthickness;
	/**距离*/
	@Excel(name = "距离", width = 15)
    @ApiModelProperty(value = "距离")
    private java.lang.String strdistance;
	/**上一测点距离mm*/
	@Excel(name = "上一测点距离mm", width = 15)
    @ApiModelProperty(value = "上一测点距离mm")
    private java.lang.Integer beforedistance;
	/**0 不合格  1合格*/
	@Excel(name = "0 不合格  1合格", width = 15)
    @ApiModelProperty(value = "0 不合格  1合格")
    private java.lang.Integer flagpassrate;
	/**波形信号值*/
	@Excel(name = "波形信号值", width = 15)
    @ApiModelProperty(value = "波形信号值")
    private java.lang.Integer signals;
	/**设计厚度mm*/
	@Excel(name = "设计厚度mm", width = 15)
    @ApiModelProperty(value = "设计厚度mm")
    private java.lang.Integer designthickness;
	/**主筋间距mm*/
	@Excel(name = "主筋间距mm", width = 15)
    @ApiModelProperty(value = "主筋间距mm")
    private java.lang.Integer masterspacing;
	/**1 ：横向  2：纵向*/
	@Excel(name = "1 ：横向  2：纵向", width = 15)
    @ApiModelProperty(value = "1 ：横向  2：纵向")
    private java.lang.Integer direction;
}
