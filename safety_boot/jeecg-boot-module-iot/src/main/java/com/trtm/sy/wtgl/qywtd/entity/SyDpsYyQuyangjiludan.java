package com.trtm.sy.wtgl.qywtd.entity;

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
 * @Description: sy_dps_yy_quyangjiludan
 * @Author: jeecg-boot
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_quyangjiludan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_quyangjiludan对象", description="sy_dps_yy_quyangjiludan")
public class SyDpsYyQuyangjiludan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自增长*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键，自增长")
    private Integer id;
	/**外键，原材进场登记表ID*/
	@Excel(name = "外键，原材进场登记表ID", width = 15)
    @ApiModelProperty(value = "外键，原材进场登记表ID")
    private Integer yuancaijinchangdengjiid;
	/**取样单类型（0：施工单位 1：监理）*/
	@Excel(name = "取样单类型（0：施工单位 1：监理）", width = 15)
    @ApiModelProperty(value = "取样单类型（0：施工单位 1：监理）")
    private Integer quyangdanleixing;
	/**取样单流水号*/
	@Excel(name = "取样单流水号", width = 15)
    @ApiModelProperty(value = "取样单流水号")
    private String quyangdanliushuihao;
	/**成型日期(YYYY-MM-dd)*/
	@Excel(name = "成型日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "成型日期(YYYY-MM-dd)")
    private String chengxingriqi;
	/**取样地点*/
	@Excel(name = "取样地点", width = 15)
    @ApiModelProperty(value = "取样地点")
    private String quyangdidian;
	/**样品数量*/
	@Excel(name = "样品数量", width = 15)
    @ApiModelProperty(value = "样品数量")
    private String yangpingshuliang;
	/**样品描述*/
	@Excel(name = "样品描述", width = 15)
    @ApiModelProperty(value = "样品描述")
    private String yangpinmiaoshu;
	/**抽样人员*/
	@Excel(name = "抽样人员", width = 15)
    @ApiModelProperty(value = "抽样人员")
    private String chouyangrenyuan;
	/**外委状态*/
	@Excel(name = "外委状态", width = 15)
    @ApiModelProperty(value = "外委状态")
    private String waiweizhuangtai;
}
