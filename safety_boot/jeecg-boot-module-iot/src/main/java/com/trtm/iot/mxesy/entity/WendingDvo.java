package com.trtm.iot.mxesy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.StabilityDetail.entity.StabilityDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: w_wendingdu_m
 * @Author: jeecg-boot
 * @Date: 2021-04-28
 * @Version: V1.0
 */
@Data
@TableName("w_wendingdu_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "w_wendingdu_m对象", description = "w_wendingdu_m")
public class WendingDvo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String syjid;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * isEnd
     */
    @Excel(name = "isEnd", width = 15)
    @ApiModelProperty(value = "isEnd")
    private String isEnd;

    /**
     * isTesttime
     */
    @Excel(name = "isTesttime", width = 15)
    @ApiModelProperty(value = "isTesttime")
    private String isTesttime;

    /**
     * fSbbh
     */
    @Excel(name = "fSbbh", width = 15)
    @ApiModelProperty(value = "fSbbh")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String fsbbh;

    /**
     * projectname
     */
    @Excel(name = "projectname", width = 15)
    @ApiModelProperty(value = "projectname")
    private String projectname;

    /**
     * sampleno
     */
    @Excel(name = "sampleno", width = 15)
    @ApiModelProperty(value = "sampleno")
    private String sampleno;

    /**
     * gcbuwei
     */
    @Excel(name = "gcbuwei", width = 15)
    @ApiModelProperty(value = "gcbuwei")
    private String gcbuwei;

    /**
     * samplename
     */
    @Excel(name = "samplename", width = 15)
    @ApiModelProperty(value = "samplename")
    private String samplename;

    /**
     * samplems
     */
    @Excel(name = "samplems", width = 15)
    @ApiModelProperty(value = "samplems")
    private String samplems;

    /**
     * liuzhiavg
     */
    @Excel(name = "liuzhiavg", width = 15)
    @ApiModelProperty(value = "liuzhiavg")
    private BigDecimal liuzhiavg;

    /**
     * wddavg
     */
    @Excel(name = "wddavg", width = 15)
    @ApiModelProperty(value = "wddavg")
    private BigDecimal wddavg;

    /**
     * biaozhunzhi1
     */
    @Excel(name = "biaozhunzhi1", width = 15)
    @ApiModelProperty(value = "biaozhunzhi1")
    private BigDecimal biaozhunzhi1;

    /**
     * biaozhunzhi2
     */
    @Excel(name = "biaozhunzhi2", width = 15)
    @ApiModelProperty(value = "biaozhunzhi2")
    private BigDecimal biaozhunzhi2;

    /**
     * biaozhunzhi3
     */
    @Excel(name = "biaozhunzhi3", width = 15)
    @ApiModelProperty(value = "biaozhunzhi3")
    private BigDecimal biaozhunzhi3;

    /**
     * isqualified
     */
    @Excel(name = "isqualified", width = 15)
    @ApiModelProperty(value = "isqualified")
    private String isqualified;

    /**
     * status
     */
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;

    /**
     * groupnum
     */
    @Excel(name = "groupnum", width = 15)
    @ApiModelProperty(value = "groupnum")
    private Integer groupnum;

    /**
     * submittime
     */
    @Excel(name = "submittime", width = 15)
    @ApiModelProperty(value = "submittime")
    private String submittime;

    /**
     * shjieguo
     */
    @Excel(name = "shjieguo", width = 15)
    @ApiModelProperty(value = "shjieguo")
    private String shjieguo;

    //稳定度子表
    @TableField(exist = false)
    private List<StabilityDetail> stabilityDetails;


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

}
