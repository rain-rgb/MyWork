package com.trtm.iot.ztbhzjckh.entity;

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
 * @Description: zt_bhzjckh
 * @Author: jeecg-boot
 * @Date:   2023-11-03
 * @Version: V1.0
 */
@Data
@TableName("zt_bhzjckh")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zt_bhzjckh对象", description="zt_bhzjckh")
public class ZtBhzjckh implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**分包单位*/
	@Excel(name = "分包单位", width = 15)
    @ApiModelProperty(value = "分包单位")
    private java.lang.String customer;
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private java.lang.String taiz;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private java.lang.String projname;
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private java.lang.String conspos;
	/**分项工程*/
	@Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private java.lang.String projadr;
	/**发货日期*/
	@Excel(name = "发货日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发货日期")
    private java.util.Date dattim;
	/**25以上(65)*/
	@Excel(name = "25以上(65)", width = 15)
    @ApiModelProperty(value = "25以上(65)")
    private java.lang.Integer aboveew;
	/**25/20(80)*/
	@Excel(name = "25/20(80)", width = 15)
    @ApiModelProperty(value = "25/20(80)")
    private java.lang.Integer abovees;
	/**20/10(90)*/
	@Excel(name = "20/10(90)", width = 15)
    @ApiModelProperty(value = "20/10(90)")
    private java.lang.Integer aboveys;
	/**10/0(100)*/
	@Excel(name = "10/0(100)", width = 15)
    @ApiModelProperty(value = "10/0(100)")
    private java.lang.Integer abovehg;
	/**0/-10(95)*/
	@Excel(name = "0/-10(95)", width = 15)
    @ApiModelProperty(value = "0/-10(95)")
    private java.lang.Integer abovejs;
	/**-10/-20(75)*/
	@Excel(name = "-10/-20(75)", width = 15)
    @ApiModelProperty(value = "-10/-20(75)")
    private java.lang.Integer abovejes;
	/**-20/-40(50)*/
	@Excel(name = "-20/-40(50)", width = 15)
    @ApiModelProperty(value = "-20/-40(50)")
    private java.lang.Integer abovejss;
	/**-40/-60(-40)*/
	@Excel(name = "-40/-60(-40)", width = 15)
    @ApiModelProperty(value = "-40/-60(-40)")
    private java.lang.Integer abovejls;
	/**>-60(-60)*/
	@Excel(name = ">-60(-60)", width = 15)
    @ApiModelProperty(value = ">-60(-60)")
    private java.lang.Integer abovebhg;
	/**得分*/
	@Excel(name = "得分", width = 15)
    @ApiModelProperty(value = "得分")
    private java.lang.Integer scores;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
