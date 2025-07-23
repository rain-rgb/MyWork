package com.trtm.iot.lqbhzrecipe.entity;

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
 * @Description: bhz_lq_phb_zibiao
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Data
@TableName("bhz_lq_phb_zibiao")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_lq_phb_zibiao对象", description="bhz_lq_phb_zibiao")
public class BhzLqPhbZibiao implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String cailiaono;
	/**添加剂*/
	@Excel(name = "添加剂", width = 15)
    @ApiModelProperty(value = "理论配合比(%)")
    private java.lang.String tianjiaji;
	/**理论沥青*/
	@Excel(name = "理论沥青", width = 15)
    @ApiModelProperty(value = "理论沥青")
    private java.lang.String lilunlq;
	/**主子id*/
	@Excel(name = "主子id", width = 15)
    @ApiModelProperty(value = "主子id")
    private java.lang.String zhuziid;
    private java.lang.String ggxh;
}
