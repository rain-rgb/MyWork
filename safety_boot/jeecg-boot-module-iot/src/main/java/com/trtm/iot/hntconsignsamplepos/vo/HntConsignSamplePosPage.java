package com.trtm.iot.hntconsignsamplepos.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.hntconsigncode.entity.HntConsignCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 混凝土见证取样货架表信息
 * @Author:
 * @Date:   2021-12-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="hnt_consign_sample_pos对象", description="混凝土见证取样货架表信息")
public class HntConsignSamplePosPage  {
	/**货架id*/
    @ApiModelProperty(value = "货架id")
    private Integer id;
	/**外键，组织机构id,只能是试验类型的组织机构*/
	@Excel(name = "外键，组织机构id,只能是试验类型的组织机构", width = 15)
    @ApiModelProperty(value = "外键，组织机构id,只能是试验类型的组织机构")
    private String departid;
	/**外键，组织机构编码*/
	@Excel(name = "外键，组织机构编码", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码")
    private String sysOrgCode;
	/**货架名称或者编号*/
	@Excel(name = "货架名称或者编号", width = 15)
    @ApiModelProperty(value = "货架名称或者编号")
    private String huojianame;
	/**货架排行*/
	@Excel(name = "货架排行", width = 15)
    @ApiModelProperty(value = "货架排行")
    private String huojiahang;
	/**货架排列*/
	@Excel(name = "货架排列", width = 15)
    @ApiModelProperty(value = "货架排列")
    private String huojialie;
	/**货架从下到上总层数*/
	@Excel(name = "货架从下到上总层数", width = 15)
    @ApiModelProperty(value = "货架从下到上总层数")
    private String huojiacenshu;
	/**外键，标养室id*/
	@Excel(name = "外键，标养室id", width = 15)
    @ApiModelProperty(value = "外键，标养室id")
    private String temperatureid;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    private String departname;//组织机构名称
    private BigDecimal temperature;//温度
    private BigDecimal humidity;//湿度
    private List<Object> listceng;
}
