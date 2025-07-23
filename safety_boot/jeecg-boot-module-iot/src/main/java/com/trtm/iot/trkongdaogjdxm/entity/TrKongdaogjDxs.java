package com.trtm.iot.trkongdaogjdxm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 孔道灌浆（定性检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@ApiModel(value="tr_kongdaogj_dxs对象", description="孔道灌浆（定性检测）子表")
@Data
@TableName("tr_kongdaogj_dxs")
public class TrKongdaogjDxs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**当前锤号*/
	@Excel(name = "当前锤号", width = 15)
    @ApiModelProperty(value = "当前锤号")
    private java.lang.Integer curnumber;
	/**采样时间*/
	@Excel(name = "采样时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "采样时间")
    private java.util.Date sampletime;
	/**CH1 数据 波形数值*/
	@Excel(name = "CH1 数据 波形数值", width = 15)
    @ApiModelProperty(value = "CH1 数据 波形数值")
    private java.lang.String wavedata1;
	/**CH2 数据 波形数值*/
	@Excel(name = "CH2 数据 波形数值", width = 15)
    @ApiModelProperty(value = "CH2 数据 波形数值")
    private java.lang.String wavedata2;
	/**基本信息 id*/
    @ApiModelProperty(value = "基本信息 id")
    private java.lang.String basicinfoid;
	/**总锤数*/
	@Excel(name = "总锤数", width = 15)
    @ApiModelProperty(value = "总锤数")
    private java.lang.Integer totalcount;
}
