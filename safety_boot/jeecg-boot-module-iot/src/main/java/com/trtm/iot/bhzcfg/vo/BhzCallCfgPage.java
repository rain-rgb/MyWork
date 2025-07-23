package com.trtm.iot.bhzcfg.vo;

import java.util.List;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 拌合站超标配置
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_call_cfgPage对象", description="拌合站超标配置")
public class BhzCallCfgPage {

	/**自增id*/
	@ApiModelProperty(value = "自增id")
	private java.lang.Integer id;
	/**主键uuid*/
	@Excel(name = "主键uuid", width = 15)
	@ApiModelProperty(value = "主键uuid")
	private java.lang.String uid;
	/**所属组织机构id*/
	@Excel(name = "所属组织机构id", width = 15)
	@ApiModelProperty(value = "所属组织机构id")
	private java.lang.String departid;
	/**拌合机编号*/
	@Excel(name = "拌合机编号", width = 15)
	@ApiModelProperty(value = "拌合机编号")
	private java.lang.String bhjno;
	/**是否报警:0=报警,1=不报警*/
	@Excel(name = "是否报警:0=报警,1=不报警", width = 15)
	@ApiModelProperty(value = "是否报警:0=报警,1=不报警")
	private java.lang.Integer isCall;
	/**搅拌时间报警设计值*/
	@Excel(name = "搅拌时间报警设计值", width = 15)
	@ApiModelProperty(value = "搅拌时间报警设计值")
	private java.lang.Integer stirDatetimeDesign;
	/**初级号码组*/
	@Excel(name = "初级号码组", width = 15)
	@ApiModelProperty(value = "初级号码组")
	private java.lang.String primaryPhones;
	/**中级号码组*/
	@Excel(name = "中级号码组", width = 15)
	@ApiModelProperty(value = "中级号码组")
	private java.lang.String middlePhones;
	/**高级号码组*/
	@Excel(name = "高级号码组", width = 15)
	@ApiModelProperty(value = "高级号码组")
	private java.lang.String advancedPhones;
	/**所属上级初级号码组*/
	@Excel(name = "所属上级初级号码组", width = 15)
	@ApiModelProperty(value = "所属上级初级号码组")
	private java.lang.String topprimaryPhones;
	/**所属上级中级号码组*/
	@Excel(name = "所属上级中级号码组", width = 15)
	@ApiModelProperty(value = "所属上级中级号码组")
	private java.lang.String topmiddlePhones;
	/**所属上级高级号码组*/
	@Excel(name = "所属上级高级号码组", width = 15)
	@ApiModelProperty(value = "所属上级高级号码组")
	private java.lang.String topadvancedPhones;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**权限配置*/
	@ApiModelProperty(value = "权限配置")
	private java.lang.String sysOrgCode;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**处置负责人*/
	@Excel(name = "处置负责人", width = 15)
	@ApiModelProperty(value = "处置负责人")
	private java.lang.String czperson;
	/**审核负责人*/
	@Excel(name = "审核负责人", width = 15)
	@ApiModelProperty(value = "审核负责人")
	private java.lang.String shperson;
	/**审批负责人*/
	@Excel(name = "审批负责人", width = 15)
	@ApiModelProperty(value = "审批负责人")
	private java.lang.String spperson;
	private java.lang.String supervisorPerson;

	@ExcelCollection(name="拌合站超标配置子表")
	@ApiModelProperty(value = "拌合站超标配置子表")
	private List<BhzChaobiaoCfg> bhzChaobiaoCfgList;

	private java.lang.String wenduyujingPhones;

	@Dict(dicCode = "is_call")
	private java.lang.Integer csyj;

	private java.lang.Integer cssj;

	@Dict(dicCode = "cssjdw")
	private java.lang.Integer cssjdw;
}
