package com.trtm.sy.syztgl.entity;

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
 * @Description: 试验人员管理
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Data
@TableName("sy_renyuanguanli")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_renyuanguanli对象", description="试验人员管理")
public class SyRenyuanguanli implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**人员编码(工号)*/
	@Excel(name = "人员编码(工号)", width = 15)
    @ApiModelProperty(value = "人员编码(工号)")
    private java.lang.String usercode;
	/**人员名称*/
	@Excel(name = "人员名称", width = 15)
    @ApiModelProperty(value = "人员名称")
    private java.lang.String username;
	/**性别（0男；1女）*/
	@Excel(name = "性别（0男；1女）", width = 15)
    @ApiModelProperty(value = "性别（0男；1女）")
    private java.lang.Integer sex;
	/**所属单位*/
	@Excel(name = "所属单位", width = 15)
    @ApiModelProperty(value = "所属单位")
    private java.lang.String ssdw;
	/**所属组织机构*/
	@Excel(name = "所属组织机构", width = 15)
    @ApiModelProperty(value = "所属组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysorgcode;
	/**入职时间*/
	@Excel(name = "入职时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入职时间")
    private java.util.Date ruzhitime;
	/**是否在职（0在职；1离职）*/
	@Excel(name = "是否在职（0在职；1离职）", width = 15)
    @ApiModelProperty(value = "是否在职（0在职；1离职）")
    private java.lang.String iszz;
	/**负责检测参数*/
	@Excel(name = "负责检测参数", width = 15)
    @ApiModelProperty(value = "负责检测参数")
    private java.lang.String jccs;
	/**所属职位*/
	@Excel(name = "所属职位", width = 15)
    @ApiModelProperty(value = "所属职位")
    private java.lang.String post;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String phone;
	/**通讯地址*/
	@Excel(name = "通讯地址", width = 15)
    @ApiModelProperty(value = "通讯地址")
    private java.lang.String addr;
}
