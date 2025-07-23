package com.trtm.iot.ljyadata.entity;

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
 * @Description: ljyadata
 * @Author: jeecg-boot
 * @Date:   2022-12-12
 * @Version: V1.0
 */
@Data
@TableName("ljyadata")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ljyadata对象", description="ljyadata")
public class Ljyadata implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备rollerId 振动碾-一标：1008
振动碾-二标：962
振动碾-三标：1001
振动碾-四标：1009*/
	@Excel(name = "设备rollerId", width = 15)
    @ApiModelProperty(value = "设备rollerId")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String rollerid;
	/**速度（km/h）*/
	@Excel(name = "速度（km/h）", width = 15)
    @ApiModelProperty(value = "速度（km/h）")
    private java.lang.String velocity;
	/**gps经度*/
	@Excel(name = "gps经度", width = 15)
    @ApiModelProperty(value = "gps经度")
    private java.lang.String lon;
	/**gps纬度*/
	@Excel(name = "gps纬度", width = 15)
    @ApiModelProperty(value = "gps纬度")
    private java.lang.String lat;
	/**施工时间*/
	@Excel(name = "施工时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "施工时间")
    private java.util.Date gpstime;
	/**标段id
一标：admin@20221011231138        二标：admin@20220728224205        三标：admin@20220922155751     四标：admin@20221026180158*/
	@Excel(name = "标段id")
    private java.lang.String sectionid;
}
