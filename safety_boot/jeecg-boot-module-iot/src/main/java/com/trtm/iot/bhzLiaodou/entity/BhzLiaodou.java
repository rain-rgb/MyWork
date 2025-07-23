package com.trtm.iot.bhzLiaodou.entity;

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
 * @Description: bhz_liaodou
 * @Author: jeecg-boot
 * @Date:   2023-01-29
 * @Version: V1.0
 */
@Data
@TableName("bhz_liaodou")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_liaodou对象", description="bhz_liaodou")
public class BhzLiaodou implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**原料斗编号*/
	@Excel(name = "原料斗编号", width = 15)
    @ApiModelProperty(value = "原料斗编号")
    private java.lang.String materialNo;
	/**原料斗名称*/
	@Excel(name = "原料斗名称", width = 15)
    @ApiModelProperty(value = "原料斗名称")
    private java.lang.String materialName;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private java.lang.Integer materialType;
	/**盛材料类型*/
	@Excel(name = "盛材料类型", width = 15)
    @ApiModelProperty(value = "盛材料类型")
    private java.lang.String shengMaterialType;
	/**容量*/
	@Excel(name = "容量", width = 15)
    @ApiModelProperty(value = "容量")
    private java.lang.String volume;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
    private java.lang.String kucun;
	/**关联料仓号*/
	@Excel(name = "关联料仓号", width = 15)
    @ApiModelProperty(value = "关联料仓号")
    private java.lang.String liaocangNo;
	/**料斗状态*/
	@Excel(name = "料斗状态", width = 15)
    @ApiModelProperty(value = "料斗状态")
    private java.lang.String status;

    private java.lang.String createdBy;
    private Date createdTime;
    private java.lang.String updatedBy;
    private Date updatedTime;
}
