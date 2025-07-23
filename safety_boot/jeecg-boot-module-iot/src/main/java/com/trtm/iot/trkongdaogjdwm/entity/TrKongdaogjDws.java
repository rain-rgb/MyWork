package com.trtm.iot.trkongdaogjdwm.entity;

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
 * @Description: 孔道灌浆（定位检测）子表
 * @Author: jeecg-boot
 * @Date:   2024-03-15
 * @Version: V1.0
 */
@ApiModel(value="tr_kongdaogj_dws对象", description="孔道灌浆（定位检测）子表")
@Data
@TableName("tr_kongdaogj_dws")
public class TrKongdaogjDws implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**当前测点号*/
	@Excel(name = "当前测点号", width = 15)
    @ApiModelProperty(value = "当前测点号")
    private java.lang.String curnumber;
	/**当前测点位置 */
	@Excel(name = "当前测点位置 ", width = 15)
    @ApiModelProperty(value = "当前测点位置 ")
    private java.lang.String cunpos;
	/**采样时间 */
	@Excel(name = "采样时间 ", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "采样时间 ")
    private java.util.Date sampletime;
	/**波形数值*/
	@Excel(name = "波形数值 ", width = 15)
    @ApiModelProperty(value = "波形数值 ")
    private java.lang.String wavedata;
	/**基本信息 id*/
    @ApiModelProperty(value = "基本信息 id")
    private java.lang.String basicinfoid;
	/**总测点数*/
	@Excel(name = "总测点数", width = 15)
    @ApiModelProperty(value = "总测点数")
    private java.lang.Integer totalcount;
}
