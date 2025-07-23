package com.trtm.iot.zlpz.entity;

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
 * @Description: zlpz_tenders
 * @Author: jeecg-boot
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Data
@TableName("zlpz_tenders")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zlpz_tenders对象", description="zlpz_tenders")
public class ZlpzTenders implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**标段id*/
	@Excel(name = "标段id", width = 15)
    @ApiModelProperty(value = "标段id")
    private java.lang.String bdid;
	/**标段名称*/
	@Excel(name = "标段名称", width = 15)
    @ApiModelProperty(value = "标段名称")
    private java.lang.String name;
	/**类型（build：施工  supervisor：监理  thirdparty：第三方检测）*/
	@Excel(name = "类型（build：施工  supervisor：监理  thirdparty：第三方检测）", width = 15)
    @ApiModelProperty(value = "类型（build：施工  supervisor：监理  thirdparty：第三方检测）")
    private java.lang.String sectiontype;
	/**对应zlpz_project项目id*/
	@Excel(name = "对应zlpz_project项目id", width = 15)
    @ApiModelProperty(value = "对应zlpz_project项目id")
    private java.lang.String xmid;
    private java.lang.String orgname;
}
