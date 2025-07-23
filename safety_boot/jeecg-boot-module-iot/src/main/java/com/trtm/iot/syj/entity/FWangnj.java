package com.trtm.iot.syj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: f_wangnj
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Data
@TableName("f_wangnj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="f_wangnj对象", description="f_wangnj")
public class FWangnj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**version*/
	@Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private java.lang.Integer version;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String fGuid;
	/**sjxh*/
	@Excel(name = "sjxh", width = 15)
    @ApiModelProperty(value = "sjxh")
    private java.lang.String sjxh;
	/**试验主表唯一码*/
	@Excel(name = "试验主表唯一码", width = 15)
    @ApiModelProperty(value = "试验主表唯一码")
    private java.lang.String syjid;
	/**原始标距*/
	@Excel(name = "原始标距", width = 15)
    @ApiModelProperty(value = "原始标距")
    private java.lang.String wsbz;
	/**断后标距*/
	@Excel(name = "断后标距", width = 15)
    @ApiModelProperty(value = "断后标距")
    private java.lang.String dhbz;
	/**最大力值*/
	@Excel(name = "最大力值", width = 15)
    @ApiModelProperty(value = "最大力值")
    private java.lang.String lz;
	/**抗拉强度*/
	@Excel(name = "抗拉强度", width = 15)
    @ApiModelProperty(value = "抗拉强度")
    private java.lang.String lzqd;
	/**位移过程值*/
	@Excel(name = "位移过程值", width = 15)
    @ApiModelProperty(value = "位移过程值")
    private java.lang.String wy;
	/**力值过程值*/
	@Excel(name = "力值过程值", width = 15)
    @ApiModelProperty(value = "力值过程值")
    private java.lang.String lzgc;
	/**时间过程值*/
	@Excel(name = "时间过程值", width = 15)
    @ApiModelProperty(value = "时间过程值")
    private java.lang.String sjgc;
	/**屈服力值*/
	@Excel(name = "屈服力值", width = 15)
    @ApiModelProperty(value = "屈服力值")
    private java.lang.String qflz;
	/**屈服强度*/
	@Excel(name = "屈服强度", width = 15)
    @ApiModelProperty(value = "屈服强度")
    private java.lang.String qfqd;
	/**伸长率*/
	@Excel(name = "伸长率", width = 15)
    @ApiModelProperty(value = "伸长率")
    private java.lang.String scl;
	/**试验时间*/
	@Excel(name = "试验时间", width = 15)
    @ApiModelProperty(value = "试验时间")
    private java.lang.String sysj;
	/**完成时间*/
	@Excel(name = "完成时间", width = 15)
    @ApiModelProperty(value = "完成时间")
    private java.lang.String wcsj;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;
	/**最大力值伸长率*/
	@Excel(name = "最大力值伸长率", width = 15)
    @ApiModelProperty(value = "最大力值伸长率")
    private java.lang.String zdlzscl;
	/**拉断处描述*/
	@Excel(name = "拉断处描述", width = 15)
    @ApiModelProperty(value = "拉断处描述")
    private java.lang.String ldcms;
	/**断口位置*/
	@Excel(name = "断口位置", width = 15)
    @ApiModelProperty(value = "断口位置")
    private java.lang.String dkwz;

    private String spic;// 开始图片
    private String pspic;// 破碎图片
    private String moduluselasticity;
    private String sqflz;
    private String sqfqd;
    private Integer issend;
    private String sbbh;

}
