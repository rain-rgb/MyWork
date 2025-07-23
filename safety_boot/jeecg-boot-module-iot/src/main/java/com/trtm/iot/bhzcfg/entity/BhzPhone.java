package com.trtm.iot.bhzcfg.entity;

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
 * @Description: 拌合站手机号码配置
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Data
@TableName("bhz_phone")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_phone对象", description="拌合站手机号码配置")
public class BhzPhone implements Serializable {
    private static final long serialVersionUID = 1L;

	/**自增id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增id")
    private java.lang.Integer id;
	/**UUid主键*/
	@Excel(name = "UUid主键", width = 15)
    @ApiModelProperty(value = "UUid主键")
    private java.lang.String uid;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String names;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @ApiModelProperty(value = "手机号码")
    private java.lang.String phones;
	@Dict(dicCode = "phonesname")
	/**号码组:1=初级超标号码组,2=中级超标号码组,3=高级超标号码组,4=试验机不合格号码组 5=张拉压浆号码组*/
	@Excel(name = "号码组:1=初级超标号码组,2=中级超标号码组,3=高级超标号码组,4=试验机不合格号码组 5=张拉压浆号码组", width = 15)
    @ApiModelProperty(value = "号码组:1=初级超标号码组,2=中级超标号码组,3=高级超标号码组,4=试验机不合格号码组 5=张拉压浆号码组")
    private java.lang.Integer phonesname;
	/**所属机构*/
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "所属机构")
    private java.lang.String sysOrgCode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**创建人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**修改人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
}
