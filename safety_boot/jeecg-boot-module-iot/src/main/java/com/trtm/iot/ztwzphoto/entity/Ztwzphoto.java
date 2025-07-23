package com.trtm.iot.ztwzphoto.entity;

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
 * @Description: 中铁图片
 * @Author: jeecg-boot
 * @Date:   2024-04-03
 * @Version: V1.0
 */
@Data
@TableName("ztwzphoto")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ztwzphoto对象", description="中铁图片")
public class Ztwzphoto implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgid;
	/**主表主键*/
	@Excel(name = "主表主键", width = 15)
    @ApiModelProperty(value = "主表主键")
    private java.lang.String orderid;
	/**相机位置*/
	@Excel(name = "相机位置", width = 15)
    @ApiModelProperty(value = "相机位置")
    private java.lang.String cameraposition;
	/**相机编码*/
	@Excel(name = "相机编码", width = 15)
    @ApiModelProperty(value = "相机编码")
    private java.lang.String cameracode;
	/**照片类型（入场、出场）*/
	@Excel(name = "照片类型（入场、出场）", width = 15)
    @ApiModelProperty(value = "照片类型（入场、出场）")
    private java.lang.String phototype;
	/**照片链接Key*/
	@Excel(name = "照片链接Key", width = 15)
    @ApiModelProperty(value = "照片链接Key")
    private java.lang.String photourl;
	/**上传之前端上的主键*/
	@Excel(name = "上传之前端上的主键", width = 15)
    @ApiModelProperty(value = "上传之前端上的主键")
    private java.lang.String oriitemid;
	/**上传之前端上的主表主键*/
	@Excel(name = "上传之前端上的主表主键", width = 15)
    @ApiModelProperty(value = "上传之前端上的主表主键")
    private java.lang.String oriorderid;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    private java.lang.String isremoved;
	/**上传状态*/
	@Excel(name = "上传状态", width = 15)
    @ApiModelProperty(value = "上传状态")
    private java.lang.String externaluploadstate;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
