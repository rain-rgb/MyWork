package com.trtm.iot.zhenru.entity;

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
 * @Description: w_zhenrudu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
@Data
@TableName("w_zhenrudu_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="w_zhenrudu_m对象", description="w_zhenrudu_m")
public class WZhenruduM implements Serializable {
    private static final long serialVersionUID = 1L;
    /**设备编号*/
    @Excel(name = "试验id", width = 15)
    @ApiModelProperty(value = "试验id")
    private String syjid;
	/**isEnd*/
	@Excel(name = "isEnd", width = 15)
    @ApiModelProperty(value = "isEnd")
    private String isEnd;
	/**isTesttime*/
	@Excel(name = "isTesttime", width = 15)
    @ApiModelProperty(value = "isTesttime")
    private String isTesttime;
	/**fSbbh*/
	@Excel(name = "fSbbh", width = 15)
    @ApiModelProperty(value = "fSbbh")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String fsbbh;
	/**projectname*/
	@Excel(name = "projectname", width = 15)
    @ApiModelProperty(value = "projectname")
    private String projectname;
	/**sampleno*/
	@Excel(name = "sampleno", width = 15)
    @ApiModelProperty(value = "sampleno")
    private String sampleno;
	/**gcbuwei*/
	@Excel(name = "gcbuwei", width = 15)
    @ApiModelProperty(value = "gcbuwei")
        private String gcbuwei;
	/**samplename*/
	@Excel(name = "samplename", width = 15)
    @ApiModelProperty(value = "samplename")
    private String samplename;
	/**samplems*/
	@Excel(name = "samplems", width = 15)
    @ApiModelProperty(value = "samplems")
    private String samplems;
	/**zrd1*/
	@Excel(name = "zrd1", width = 15)
    @ApiModelProperty(value = "zrd1")
    private BigDecimal zrd1;
	/**zrd2*/
	@Excel(name = "zrd2", width = 15)
    @ApiModelProperty(value = "zrd2")
    private BigDecimal zrd2;
	/**zrd3*/
	@Excel(name = "zrd3", width = 15)
    @ApiModelProperty(value = "zrd3")
    private BigDecimal zrd3;
	/**zrdavg*/
	@Excel(name = "zrdavg", width = 15)
    @ApiModelProperty(value = "zrdavg")
    private BigDecimal zrdavg;
	/**biaozhunzhi1*/
	@Excel(name = "biaozhunzhi1", width = 15)
    @ApiModelProperty(value = "biaozhunzhi1")
    private BigDecimal biaozhunzhi1;
	/**biaozhunzhi2*/
	@Excel(name = "biaozhunzhi2", width = 15)
    @ApiModelProperty(value = "biaozhunzhi2")
    private BigDecimal biaozhunzhi2;
	/**isqualified*/
	@Excel(name = "isqualified", width = 15)
    @ApiModelProperty(value = "isqualified")
    private String isqualified;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
	/**groupnum*/
	@Excel(name = "groupnum", width = 15)
    @ApiModelProperty(value = "groupnum")
    private Integer groupnum;
	/**submittime*/
	@Excel(name = "submittime", width = 15)
    @ApiModelProperty(value = "submittime")
    private String submittime;
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
    private String shjieguo;

    private String gcz;
    private String gcsj;
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "overproof_status")
    private Integer overproofStatus;
    private String overReason;// 预警原因
    private Integer teststatus;
    private Integer lqtype;
    private String asphaltnum;
    private String asphaltlevel;
    private String weatherregion;
    private String carnum;
    private String aging;
    private Integer isjtjt;

}
