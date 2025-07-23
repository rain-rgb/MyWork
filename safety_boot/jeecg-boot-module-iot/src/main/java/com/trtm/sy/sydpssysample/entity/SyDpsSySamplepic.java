package com.trtm.sy.sydpssysample.entity;

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
 * @Description: sy_dps_sy_samplepic
 * @Author: jeecg-boot
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_samplepic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_samplepic对象", description="sy_dps_sy_samplepic")
public class SyDpsSySamplepic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号，主键，UUID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号，主键，UUID")
    private String id;
	/**样品编号，外键dps_sy_sample样品表的sampleNo*/
	@Excel(name = "样品编号，外键dps_sy_sample样品表的sampleNo", width = 15)
    @ApiModelProperty(value = "样品编号，外键dps_sy_sample样品表的sampleNo")
    private String sampleno;
	/**图片1名称，图片存放SamplePic*/
	@Excel(name = "图片1名称，图片存放SamplePic", width = 15)
    @ApiModelProperty(value = "图片1名称，图片存放SamplePic")
    private String pic1;
	/**图片2名称，图片存放SamplePic*/
	@Excel(name = "图片2名称，图片存放SamplePic", width = 15)
    @ApiModelProperty(value = "图片2名称，图片存放SamplePic")
    private String pic2;
	/**图片3名称，图片存放SamplePic*/
	@Excel(name = "图片3名称，图片存放SamplePic", width = 15)
    @ApiModelProperty(value = "图片3名称，图片存放SamplePic")
    private String pic3;
	/**图片4名称，图片存放SamplePic*/
	@Excel(name = "图片4名称，图片存放SamplePic", width = 15)
    @ApiModelProperty(value = "图片4名称，图片存放SamplePic")
    private String pic4;
	/**委托id*/
	@Excel(name = "委托id", width = 15)
    @ApiModelProperty(value = "委托id")
    private String wtid;
}
