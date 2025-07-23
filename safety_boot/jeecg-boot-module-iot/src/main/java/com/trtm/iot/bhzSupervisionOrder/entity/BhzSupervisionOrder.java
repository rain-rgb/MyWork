package com.trtm.iot.bhzSupervisionOrder.entity;

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
 * @Description: bhz_supervision_order
 * @Author: jeecg-boot
 * @Date:   2023-02-08
 * @Version: V1.0
 */
@Data
@TableName("bhz_supervision_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_supervision_order对象", description="bhz_supervision_order")
public class BhzSupervisionOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
    /**
     * 唯一ID
     */
    @ApiModelProperty(value = "编号")
    private String batchNo;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeiNo;
	/**出料时间*/
	@Excel(name = "出料时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出料时间")
    private java.util.Date productDatetime;
	/**超标原因*/
	@Excel(name = "超标原因", width = 15)
    @ApiModelProperty(value = "超标原因")
    private java.lang.String overReason;
	/**保存时间*/
	@Excel(name = "保存时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "保存时间")
    private java.util.Date saveDatetime;
	/**签收人*/
	@Excel(name = "签收人", width = 15)
    @ApiModelProperty(value = "签收人")
    private java.lang.String receiver;
	/**预计处置时间*/
	@Excel(name = "预计处置时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计处置时间")
    private java.util.Date expectedDatetime;

    private String departName;
    private String projectName;
    private String supervisionUnit;//监理单位
    private String constructionUnit;//施工单位
    private Integer penaltyAmount;//惩罚金额
    private String bhzNo;
    private String documentid;
    private String contractid;
    private Integer status;
    private String zldurl;
}
