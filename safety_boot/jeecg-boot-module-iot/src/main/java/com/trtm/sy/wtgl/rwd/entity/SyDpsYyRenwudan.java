package com.trtm.sy.wtgl.rwd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: sy_dps_yy_renwudan
 * @Author: jeecg-boot
 * @Date:   2023-08-31
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_yy_renwudan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_yy_renwudan对象", description="sy_dps_yy_renwudan")
public class SyDpsYyRenwudan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键，自增长*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键，自增长")
    private Integer id;
	/**外键，原材进场登记表ID*/
	@Excel(name = "外键，原材进场登记表ID", width = 15)
    @ApiModelProperty(value = "外键，原材进场登记表ID")
    private Integer yuancaijinchangdengjiid;
	/**任务单类型（0：施工单位 1：监理）*/
	@Excel(name = "任务单类型（0：施工单位 1：监理）", width = 15)
    @ApiModelProperty(value = "任务单类型（0：施工单位 1：监理）")
    private Integer renwudanleixing;
	/**任务单流水号*/
	@Excel(name = "任务单流水号", width = 15)
    @ApiModelProperty(value = "任务单流水号")
    private String renwudanliushuihao;
	/**试验完成期限（YYYY-MM-dd）*/
	@Excel(name = "试验完成期限（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "试验完成期限（YYYY-MM-dd）")
    private String shiyanwanchengqixian;
	/**检测依据*/
	@Excel(name = "检测依据", width = 15)
    @ApiModelProperty(value = "检测依据")
    private String jianceyiju;
	/**判定依据*/
	@Excel(name = "判定依据", width = 15)
    @ApiModelProperty(value = "判定依据")
    private String pandingyiju;
	/**检测项目*/
	@Excel(name = "检测项目", width = 15)
    @ApiModelProperty(value = "检测项目")
    private String jiancexiangmu;
	/**检测人员*/
	@Excel(name = "检测人员", width = 15)
    @ApiModelProperty(value = "检测人员")
    private String jiancerenyuan;
	/**记录人员*/
	@Excel(name = "记录人员", width = 15)
    @ApiModelProperty(value = "记录人员")
    private String jilurenyuan;
	/**复核人员*/
	@Excel(name = "复核人员", width = 15)
    @ApiModelProperty(value = "复核人员")
    private String fuherenyuan;
	/**签发人员*/
	@Excel(name = "签发人员", width = 15)
    @ApiModelProperty(value = "签发人员")
    private String qianfarenyuan;
	/**检测人员id*/
	@Excel(name = "检测人员id", width = 15)
    @ApiModelProperty(value = "检测人员id")
    private String userid;
	/**检测人员时间*/
	@Excel(name = "检测人员时间", width = 15)
    @ApiModelProperty(value = "检测人员时间")
    private String jiancerenyuandate;
	/**复核人员时间*/
	@Excel(name = "复核人员时间", width = 15)
    @ApiModelProperty(value = "复核人员时间")
    private String fuherenyuandate;
	/**签发人员时间*/
	@Excel(name = "签发人员时间", width = 15)
    @ApiModelProperty(value = "签发人员时间")
    private String qianfarenyuandate;
	/**复核人员id*/
	@Excel(name = "复核人员id", width = 15)
    @ApiModelProperty(value = "复核人员id")
    private String fuherenyuanid;

    /**样品编号*/
    @Excel(name = "样品编号", width = 15)
    @ApiModelProperty(value = "样品编号")
    private String sampleNo;
    /**样品编号*/
//    @Excel(name = "是否已指派试验人员（0：未指派：1已指派）", width = 15)
//    @ApiModelProperty(value = "是否已指派试验人员（0：未指派：1已指派）")
//    private Integer zpzt;

    @TableField(exist = false)
    private String erweimabianhao;
    @TableField(exist = false)
    private String gongcghengmingcheng;
    @TableField(exist = false)
    private String wtType;

    private String wtdid;
    private String xcwtdid;
    @TableField(exist = false)
    private String yangpingmingcheng;
    @TableField(exist = false)
    private String yangpingshuliang;
    @TableField(exist = false)
    private String yangpinmiaoshu;

}
