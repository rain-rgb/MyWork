package com.trtm.iot.hc_machine.entity;

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
 * @Description: 机械设备
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Data
@TableName("hc_machine")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="hc_machine对象", description="机械设备")
public class HcMachine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "唯一id")
    private java.lang.Integer id;
	/**
数据id*/
	@Excel(name = " 数据id", width = 15)
    @ApiModelProperty(value = " 数据id")
    private java.lang.String sjid;
	/**机械id*/
	@Excel(name = "机械id", width = 15)
    @ApiModelProperty(value = "机械id")
    private java.lang.String machineid;
	/**终端sn号*/
	@Excel(name = "终端sn号", width = 15)
    @ApiModelProperty(value = "终端sn号")
    private java.lang.String terminalsncode;
	/**机械宽度(m)*/
	@Excel(name = "机械宽度(m)", width = 15)
    @ApiModelProperty(value = "机械宽度(m)")
    private java.lang.Double machinesize;
	/**机械名称*/
	@Excel(name = "机械名称", width = 15)
    @ApiModelProperty(value = "机械名称")
    private java.lang.String machinename;
	/**机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机*/
	@Excel(name = "机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机", width = 15)
    @ApiModelProperty(value = "机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机")
    private java.lang.Integer machinetypecode;
	/**机械类型*/
	@Excel(name = "机械类型", width = 15)
    @ApiModelProperty(value = "机械类型")
    private java.lang.String machinetypename;
	/**安装开始使用时间*/
	@Excel(name = "安装开始使用时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "安装开始使用时间")
    private java.util.Date tmstarttime;
	/**结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束*/
	@Excel(name = "结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束")
    private java.util.Date tmendtime;
	/**天线高度(m)*/
	@Excel(name = "天线高度(m)", width = 15)
    @ApiModelProperty(value = "天线高度(m)")
    private java.lang.Double tmwireheight;
	/**单双天线模式（0表示单天线，1表示双天线）*/
	@Excel(name = "单双天线模式（0表示单天线，1表示双天线）", width = 15)
    @ApiModelProperty(value = "单双天线模式（0表示单天线，1表示双天线）")
    private java.lang.Integer tmantemode;
	/**单天线AC*/
	@Excel(name = "单天线AC", width = 15)
    @ApiModelProperty(value = "单天线AC")
    private java.lang.Double tmwheelwidthsingle;
	/**单天线BE长*/
	@Excel(name = "单天线BE长", width = 15)
    @ApiModelProperty(value = "单天线BE长")
    private java.lang.Double tmdistancefromwheel;
	/**单天线DE长*/
	@Excel(name = "单天线DE长", width = 15)
    @ApiModelProperty(value = "单天线DE长")
    private java.lang.Double tmdistancefromcenter;
	/**双天线CB长*/
	@Excel(name = "双天线CB长", width = 15)
    @ApiModelProperty(value = "双天线CB长")
    private java.lang.Double tmcentertomainx;
	/**双天线GC长*/
	@Excel(name = "双天线GC长", width = 15)
    @ApiModelProperty(value = "双天线GC长")
    private java.lang.Double tmcentertomainy;
	/**
双天线FE长*/
	@Excel(name = " 双天线FE长", width = 15)
    @ApiModelProperty(value = " 双天线FE长")
    private java.lang.Double tmvicetomainx;
	/**双天线GF长*/
	@Excel(name = "双天线GF长", width = 15)
    @ApiModelProperty(value = "双天线GF长")
    private java.lang.Double tmvicetomainy;
	/**双天线AD长*/
	@Excel(name = "双天线AD长", width = 15)
    @ApiModelProperty(value = "双天线AD长")
    private java.lang.Double tmwheelwidthdouble;
	private java.lang.String pjid;
	private java.lang.String sectionid;
	private java.lang.String orgcode;
	private Integer iscall;
	private String phone;
}
