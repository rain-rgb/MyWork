package com.trtm.iot.trzhanglastatistics.entity;

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
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_statistics对象", description="张拉统计表")
public class TrZhanglaStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private int id;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
	/**预制梁场*/
	@Excel(name = "预制梁场", width = 15)
    @ApiModelProperty(value = "预制梁场")
    private String yzlc;
	/**桥梁名称*/
	@Excel(name = "桥梁名称", width = 15)
    @ApiModelProperty(value = "桥梁名称")
    private String gjbh;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeibianhao;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private String gjmc;
	/**砼强度*/
	@Excel(name = "砼强度", width = 15)
    @ApiModelProperty(value = "砼强度")
    private String snsjqd;
	/**日期*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private Date sqsj;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**总条数*/
	@Excel(name = "总条数", width = 15)
    @ApiModelProperty(value = "总条数")
    private Integer trZhanglaSum;
	/**超标总条数*/
	@Excel(name = "超标总条数", width = 15)
    @ApiModelProperty(value = "超标总条数")
    private Integer trZhanglaOversum;
}
