package com.trtm.iot.zhanglaxxb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 张拉统计表
 * @Author: jeecg-boot
 * @Date:   2022-06-08
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TrZhanglaXxbMStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private int id;
    /**syjid*/
    @Excel(name = "syjid", width = 15)
    @ApiModelProperty(value = "syjid")
    private String syjid;
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
    private Date sgsj;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    /**合格数（1为合格）*/
    @Excel(name = "合格数", width = 15)
    @ApiModelProperty(value = "合格数")
    private String hege;

}
