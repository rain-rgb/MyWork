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
 * @Description: sy_dps_yy_sypic
 * @Author: jeecg-boot
 * @Date:   2023-03-21
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_sypic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_sypic对象", description="sy_dps_yy_sypic")
public class SyDpsYySypic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号，主键，UUID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "编号，主键，UUID")
    private String id;
	/**图片1名称，图片存放SamplePic*/
	@Excel(name = "图片1名称，图片存放SamplePic", width = 15)
    @ApiModelProperty(value = "图片1名称，图片存放SamplePic")
    private String url;
	/**取样单id*/
	@Excel(name = "取样单id", width = 15)
    @ApiModelProperty(value = "取样单id")
    private Integer qydid;
}
