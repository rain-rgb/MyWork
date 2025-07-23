package com.trtm.iot.wztaizhangstatic.entity;

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
 * @Description: wztaizhang_static
 * @Author: jeecg-boot
 * @Date:   2024-05-16
 * @Version: V1.0
 */
@Data
@TableName("wztaizhang_static")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wztaizhang_static对象", description="wztaizhang_static")
public class WztaizhangStatic implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;//wztaizhang表的id
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "ycl_cailiaodict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
	/**供应商单位编码*/
	@Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    @Dict(dictTable = "ycl_gongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;

    private java.lang.String danwei;

	/**jingzhongt*/
	@Excel(name = "jingzhongt", width = 15)
    @ApiModelProperty(value = "jingzhongt")
    private java.lang.Double jingzhongt;
	/**picicount*/
	@Excel(name = "picicount", width = 15)
    @ApiModelProperty(value = "picicount")
    private java.lang.Integer picicount;
	/**jianyanstate*/
	@Excel(name = "jianyanstate", width = 15)
    @ApiModelProperty(value = "jianyanstate")
    private java.lang.Integer jianyanstate;
	/**choujianstate*/
	@Excel(name = "choujianstate", width = 15)
    @ApiModelProperty(value = "choujianstate")
    private java.lang.Integer choujianstate;
}
