package com.trtm.iot.anquanfxgk.entity;

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
 * @Description: anquan_fxaqjc_handle
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Data
@TableName("anquan_fxaqjc_handle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="anquan_fxaqjc_handle对象", description="anquan_fxaqjc_handle")
public class AnquanFxaqjcHandle implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**年度如 2024年度研判*/
	@Excel(name = "年度如 2024年度研判", width = 15)
    @ApiModelProperty(value = "年度如 2024年度研判")
    private java.lang.String niandu;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectname;
	/**所属业务部门*/
	@Excel(name = "所属业务部门", width = 15)
    @ApiModelProperty(value = "所属业务部门")
    private java.lang.String departname;
	/**guid*/
	@Excel(name = "guid", width = 15)
    @ApiModelProperty(value = "guid")
    private java.lang.String guid;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
	/**隐患名称*/
	@Excel(name = "隐患名称", width = 15)
    @ApiModelProperty(value = "隐患名称")
    private java.lang.String hiddagerName;
	/**隐患类别*/
	@Excel(name = "隐患类别", width = 15)
    @ApiModelProperty(value = "隐患类别")
    private java.lang.String hiddagerType;
	/**责任单位（部门）*/
	@Excel(name = "责任单位（部门）", width = 15)
    @ApiModelProperty(value = "责任单位（部门）")
    private java.lang.String zerenunit;
	/**牵头单位（部门）*/
	@Excel(name = "牵头单位（部门）", width = 15)
    @ApiModelProperty(value = "牵头单位（部门）")
    private java.lang.String qiantounit;
	/**配合单位（部门）*/
	@Excel(name = "配合单位（部门）", width = 15)
    @ApiModelProperty(value = "配合单位（部门）")
    private java.lang.String peiheunit;
	/**督办部门（单位）*/
	@Excel(name = "督办部门（单位）", width = 15)
    @ApiModelProperty(value = "督办部门（单位）")
    private java.lang.String dubanunit;
	/**基本概况*/
	@Excel(name = "基本概况", width = 15)
    @ApiModelProperty(value = "基本概况")
    private java.lang.String jibenInfo;
	/**整治目标*/
	@Excel(name = "整治目标", width = 15)
    @ApiModelProperty(value = "整治目标")
    private java.lang.String zzmb;
	/**整治方案*/
	@Excel(name = "整治方案", width = 15)
    @ApiModelProperty(value = "整治方案")
    private java.lang.String zzfa;
	/**整治情况*/
	@Excel(name = "整治情况", width = 15)
    @ApiModelProperty(value = "整治情况")
    private java.lang.String zzqk;
	/**安委会*/
	@Excel(name = "安委会", width = 15)
    @ApiModelProperty(value = "安委会")
    private java.lang.String querenren;
}
