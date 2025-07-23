package com.trtm.iot.ztbhzkhdf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 混凝土配送考核得分表
 * @Author: jeecg-boot
 * @Date:   2023-11-02
 * @Version: V1.0
 */
@Data
@TableName("zt_bhzkhdf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zt_bhzkhdf对象", description="混凝土配送考核得分表")
public class ZtBhzkhdfVO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**分包单位*/
	@Excel(name = "分包单位", width = 15)
    @ApiModelProperty(value = "分包单位")
    private String customer;
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String taiz;
	/**单位工程*/
	@Excel(name = "单位工程", width = 15)
    @ApiModelProperty(value = "单位工程")
    private String projname;
	/**分部工程*/
	@Excel(name = "分部工程", width = 15)
    @ApiModelProperty(value = "分部工程")
    private String conspos;
	/**分项工程*/
	@Excel(name = "分项工程", width = 15)
    @ApiModelProperty(value = "分项工程")
    private String projadr;
	/**25以上(-45)*/
	@Excel(name = "25以上(-45)", width = 15)
    @ApiModelProperty(value = "25以上(-45)")
    private Integer aboveew;
	/**25/20(10)*/
	@Excel(name = "25/20(10)", width = 15)
    @ApiModelProperty(value = "25/20(10)")
    private Integer abovees;
	/**20/10(40)*/
	@Excel(name = "20/10(40)", width = 15)
    @ApiModelProperty(value = "20/10(40)")
    private Integer aboveys;
	/**10/0(100)*/
	@Excel(name = "10/0(100)", width = 15)
    @ApiModelProperty(value = "10/0(100)")
    private Integer abovehg;
	/**0/-10(95)*/
	@Excel(name = "0/-10(95)", width = 15)
    @ApiModelProperty(value = "0/-10(95)")
    private Integer abovejs;
	/**-10/-20(40)*/
	@Excel(name = "-10/-20(40)", width = 15)
    @ApiModelProperty(value = "-10/-20(40)")
    private Integer abovejes;
	/**-20/-40(-20)*/
	@Excel(name = "-20/-40(-20)", width = 15)
    @ApiModelProperty(value = "-20/-40(-20)")
    private Integer abovejss;
	/**-40/-60(-40)*/
	@Excel(name = "-40/-60(-40)", width = 15)
    @ApiModelProperty(value = "-40/-60(-40)")
    private Integer abovejls;
	/**>-60*/
	@Excel(name = ">-60", width = 15)
    @ApiModelProperty(value = ">-60")
    private Integer abovebhg;
	/**得分*/
	@Excel(name = "得分", width = 15)
    @ApiModelProperty(value = "得分")
    private Integer scores;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**发货日期*/
    @Excel(name = "发货日期", width = 20, format = "yyyy年MM月dd日")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    @ApiModelProperty(value = "发货日期")
    private Date dattim;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    private Date dattim_begin;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    private Date dattim_end;

    private String timepj;
}
