package com.trtm.iot.wzpandian.entity;

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
 * @Description: wzpandian
 * @Author: jeecg-boot
 * @Date:   2023-12-08
 * @Version: V1.0
 */
@Data
@TableName("wzpandian")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzpandian对象", description="wzpandian")
public class Wzpandian implements Serializable {
    private static final long serialVersionUID = 1L;

	/**盘点id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "盘点id")
    private Integer id;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailiaono;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaoname;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private String nodetype;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String guigexinghao;
	/**材料计量单位*/
	@Excel(name = "材料计量单位", width = 15)
    @ApiModelProperty(value = "材料计量单位")
    private String cailiaojiliangdanwei;
	/**盘点材料数量*/
	@Excel(name = "盘点材料数量", width = 15)
    @ApiModelProperty(value = "盘点材料数量")
    private Double cailiaonum;
	/**盘点时仓库数量*/
	@Excel(name = "盘点时仓库数量", width = 15)
    @ApiModelProperty(value = "盘点时仓库数量")
    private Double liaocangnum;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    private String liaocangno;
	/**批次编号*/
	@Excel(name = "批次编号", width = 15)
    @ApiModelProperty(value = "批次编号")
    private String picino;
	/**拍照照片*/
	@Excel(name = "拍照照片", width = 15)
    @ApiModelProperty(value = "拍照照片")
    private String pictures;
	/**盘点时间*/
	@Excel(name = "盘点时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "盘点时间")
    private Date pandiantime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createpersonl;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createtime;
}
