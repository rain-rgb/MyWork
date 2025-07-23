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
 * @Description: sy_dps_sy_sample_wt
 * @Author: jeecg-boot
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_sample_wt")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_sample_wt对象", description="sy_dps_sy_sample_wt")
public class SyDpsSySampleWt implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键，")
    private String id;
	/**外键，样品表id*/
	@Excel(name = "外键，样品表id", width = 15)
    @ApiModelProperty(value = "外键，样品表id")
    private String sampleid;
	/**外键，样品编号*/
	@Excel(name = "外键，样品编号", width = 15)
    @ApiModelProperty(value = "外键，样品编号")
    private String sampleno;
	/**合格状态,默认0（0：未检 1：合格 2：不合格）*/
	@Excel(name = "合格状态,默认0（0：未检 1：合格 2：不合格）", width = 15)
    @ApiModelProperty(value = "合格状态,默认0（0：未检 1：合格 2：不合格）")
    private Integer qualifiedstate;
}
