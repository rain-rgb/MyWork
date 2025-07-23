package com.trtm.iot.attachlist.entity;

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
 * @Description: 图片数据表
 * @Author: jeecg-boot
 * @Date:   2022-06-30
 * @Version: V1.0
 */
@Data
@TableName("attachlist")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="attachlist对象", description="图片数据表")
public class Attachlist implements Serializable {
    private static final long serialVersionUID = 1L;

	/**objectid*/
	@Excel(name = "objectid", width = 15)
    @ApiModelProperty(value = "objectid")
    private java.lang.String objectid;
	/**filepath*/
	@Excel(name = "filepath", width = 15)
    @ApiModelProperty(value = "filepath")
    private java.lang.String filepath;
	/**createtime*/
	@Excel(name = "createtime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createtime")
    private java.util.Date createtime;
	/**remoteip*/
	@Excel(name = "remoteip", width = 15)
    @ApiModelProperty(value = "remoteip")
    private java.lang.String remoteip;
	/**filename*/
	@Excel(name = "filename", width = 15)
    @ApiModelProperty(value = "filename")
    private java.lang.String filename;
	/**sign*/
	@Excel(name = "sign", width = 15)
    @ApiModelProperty(value = "sign")
    private java.lang.String sign;
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**fileformat*/
	@Excel(name = "fileformat", width = 15)
    @ApiModelProperty(value = "fileformat")
    private java.lang.String fileformat;
	/**userid*/
	@Excel(name = "userid", width = 15)
    @ApiModelProperty(value = "userid")
    private java.lang.String userid;
	/**http://web.traiot.cn/docs/   路径前加地址*/
	@Excel(name = "http://web.traiot.cn/docs/   路径前加地址", width = 15)
    @ApiModelProperty(value = "http://web.traiot.cn/docs/   路径前加地址")
    private java.lang.String relativepath;
}
