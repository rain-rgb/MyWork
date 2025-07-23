package com.trtm.iot.wzcailiaonamedictman.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Data
@ApiModel(value="wzcailiaonamedict_man对象", description="wzcailiaonamedict_man")
public class WzcailiaonamedictManVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Integer id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaoName;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailiaoNo;

	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private String guigexinghao;
    /**材料字典的唯一id*/
    @Excel(name = "材料字典的唯一id", width = 15)
    @ApiModelProperty(value = "材料字典的唯一id")
    private java.lang.String guid;



}
