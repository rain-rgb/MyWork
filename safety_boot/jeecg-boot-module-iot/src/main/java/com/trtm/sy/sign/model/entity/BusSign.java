package com.trtm.sy.sign.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 *
 *
 * @author wcx
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sy_sign")
public class BusSign implements Serializable {

    /**
     * 签约主键id
     */
    @TableId(value = "signid", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "签约主键id")
    private String signid;

    @ApiModelProperty(value = "样品编号")
    private String sampleno;
    @ApiModelProperty(value = "电子签约文档id")
    private Long documentid;
    @ApiModelProperty(value = "电子签约文件ID")
    private Long contractid;
    @ApiModelProperty(value = "签约状态（0：未签署；10：报告检测人员待签字；11：报告审核人员待签字；12：报告批准人员待签字；13：报告表待盖章；20：记录表检测人员待签字;21:记录表记录人员待签字;22:记录表复核人员待签字;23:记录表待盖章;50:签章完成·）")
    private String status;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    @Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    private String orgCode;

}
