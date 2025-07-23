package com.trtm.iot.rlsb.entity;

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
 * @Description: 人脸识别主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@TableName("transaction_rl")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="transaction_rl对象", description="人脸识别主表")
public class TransactionRl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibanhao;
	/**指令（70）*/
	@Excel(name = "指令（70）", width = 15)
    @ApiModelProperty(value = "指令（70）")
    private java.lang.Integer drivers;
	/**保留*/
	@Excel(name = "保留", width = 15)
    @ApiModelProperty(value = "保留")
    private java.lang.Integer no;
	/**数据包JSON*/
	@Excel(name = "数据包JSON", width = 15)
    @ApiModelProperty(value = "数据包JSON")
    private java.lang.String src;
	/**人脸照片ID*/
	@Excel(name = "人脸照片ID", width = 15)
    @ApiModelProperty(value = "人脸照片ID")
    private java.lang.String faceid;
	/**抓拍照片base64*/
	@Excel(name = "抓拍照片base64", width = 15)
    @ApiModelProperty(value = "抓拍照片base64")
    private java.lang.String image;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "记录时间")
    private java.util.Date recordTime;
	/**体温*/
	@Excel(name = "体温", width = 15)
    @ApiModelProperty(value = "体温")
    private java.lang.String temp;
}
