package com.trtm.sy.sycspz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: m_param_hangongxx
 * @Author: jeecg-boot
 * @Date:   2024-01-03
 * @Version: V1.0
 */
@Data
@TableName("sy_m_param_hangongxx")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_m_param_hangongxx对象", description="sy_m_param_hangongxx")
public class MParamHangongxx implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private String id;
	/**hangxx*/
	@Excel(name = "hanGxx", width = 15)
    @ApiModelProperty(value = "hanGxx")
    @TableField(value = "hanGxx")
    private String hanGxx;
	/**departid*/
	@Excel(name = "orgCode", width = 15)
    @ApiModelProperty(value = "orgCode")
    @TableField(value = "orgCode")
    private String orgCode;
//    private String departId;
}
