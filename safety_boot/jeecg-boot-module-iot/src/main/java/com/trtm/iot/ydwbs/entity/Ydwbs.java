package com.trtm.iot.ydwbs.entity;

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
 * @Description: ydwbs
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
@Data
@TableName("ydwbs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ydwbs对象", description="ydwbs")
public class Ydwbs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**条目ID主键唯一*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "条目ID主键唯一")
    private java.lang.String id;
	/**合同id*/
	@Excel(name = "合同id", width = 15)
    @ApiModelProperty(value = "合同id")
    private java.lang.String contId;
	/**节点类型（fx分项清单）*/
	@Excel(name = "节点类型（fx分项清单）", width = 15)
    @ApiModelProperty(value = "节点类型（fx分项清单）")
    private java.lang.String nodetype;
	/**条目编码*/
	@Excel(name = "条目编码", width = 15)
    @ApiModelProperty(value = "条目编码")
    private java.lang.String code;
	/**条目名称*/
	@Excel(name = "条目名称", width = 15)
    @ApiModelProperty(value = "条目名称")
    private java.lang.String name;
	/**父节点ID*/
	@Excel(name = "父节点ID", width = 15)
    @ApiModelProperty(value = "父节点ID")
    private java.lang.String pid;
	/**是否是叶节点*/
	@Excel(name = "是否是叶节点", width = 15)
    @ApiModelProperty(value = "是否是叶节点")
    private java.lang.String isLeaf;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private java.lang.String stack;
	/**图号*/
	@Excel(name = "图号", width = 15)
    @ApiModelProperty(value = "图号")
    private java.lang.String pic;
	/**排序字段*/
	@Excel(name = "排序字段", width = 15)
    @ApiModelProperty(value = "排序字段")
    private java.lang.String sort;

	private java.lang.Integer status;
}
