package com.trtm.iot.trliangchanginfo.entity;

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
 * @Description: tr_liangchang_info
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("tr_liangchang_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_liangchang_info对象", description="tr_liangchang_info")
public class TrLiangchangInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**标段名称*/
	@Excel(name = "标段名称", width = 15)
    @ApiModelProperty(value = "标段名称")
    private java.lang.String sectionname;
	/**梁场名称*/
	@Excel(name = "梁场名称", width = 15)
    @ApiModelProperty(value = "梁场名称")
    private java.lang.String liangchangname;
	/**梁场关联设备*/
	@Excel(name = "梁场关联设备", width = 15)
    @ApiModelProperty(value = "梁场关联设备")
    private java.lang.String shebeis;
	/**code*/
	@Excel(name = "code", width = 15)
    @ApiModelProperty(value = "code")
    private java.lang.String code;
	/**treeids*/
	@Excel(name = "treeids", width = 15)
    @ApiModelProperty(value = "treeids")
    private java.lang.String treeids;
	/**note*/
	@Excel(name = "note", width = 15)
    @ApiModelProperty(value = "note")
    private java.lang.String note;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
}
