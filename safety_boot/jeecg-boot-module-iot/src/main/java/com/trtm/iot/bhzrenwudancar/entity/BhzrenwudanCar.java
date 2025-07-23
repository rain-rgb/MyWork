package com.trtm.iot.bhzrenwudancar.entity;

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
 * @Description: 中铁一局拌合站生产流向
 * @Author: jeecg-boot
 * @Date:   2023-10-24
 * @Version: V1.0
 */
@Data
@TableName("bhzrenwudan_car")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhzrenwudan_car对象", description="中铁一局拌合站生产流向")
public class BhzrenwudanCar implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;

    private java.lang.Integer bhzid;
	/**票据号*/
	@Excel(name = "票据号", width = 15)
    @ApiModelProperty(value = "票据号")
    private java.lang.String danhao;

    @ApiModelProperty(value = "任务单编号")
    private java.lang.String code;
    @ApiModelProperty(value = "生产线(1或2)")
    private java.lang.String station;
    @ApiModelProperty(value = "砂浆配合比编号")
    private java.lang.String morrec;
    @ApiModelProperty(value = "施工配合比编号")
    private java.lang.String recipe;

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
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private java.lang.String projgrade;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String betlev;
	/**生产方量(含砂浆方量*/
	@Excel(name = "生产方量(含砂浆方量", width = 15)
    @ApiModelProperty(value = "生产方量(含砂浆方量")
    private java.lang.Double prodmete;

    @ApiModelProperty(value = "砂浆方量")
    private java.lang.Double mormete;
    @ApiModelProperty(value = "累计方量")
    private java.lang.Double totmete;
    @ApiModelProperty(value = "运输方量")
    private java.lang.Double transportvolume;
    @ApiModelProperty(value = "任务方量")
    private java.lang.Double mete;
    @ApiModelProperty(value = "节超方量")
    private java.lang.Double dvalue;

	/**车辆编号*/
	@Excel(name = "车牌号", width = 15)
    @ApiModelProperty(value = "车牌号")
    private java.lang.String vehicle;
	/**驾驶员*/
	@Excel(name = "司机姓名", width = 15)
    @ApiModelProperty(value = "司机姓名")
    private java.lang.String driver;
	/**发货日期*/
	@Excel(name = "发货日期", width = 20, format = "yyyy年MM月dd日")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    @ApiModelProperty(value = "发货日期")
    private java.util.Date dattim;
	/**发货时间*/
	@Excel(name = "发货时间", width = 20, format = "HH : mm")
	@JsonFormat(timezone = "GMT+8",pattern = "HH : mm")
    @DateTimeFormat(pattern="HH : mm")
    @ApiModelProperty(value = "发货时间")
    private java.util.Date dattimShij;
	/**浇注日期*/
//	@Excel(name = "浇注日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇注日期")
    private java.util.Date begtim;
	/**截止日期*/
//	@Excel(name = "截止日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止日期")
    private java.util.Date endtim;
	/**调度人员*/
//	@Excel(name = "调度人员", width = 15)
    @ApiModelProperty(value = "调度人员")
    private java.lang.String attamper;
	/**到工作面时间*/
	@Excel(name = "到工作面时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到工作面时间")
    private java.util.Date dgzmsj;
    @Excel(name = "签收时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签收时间")
    private java.util.Date qianshoutime;
    @Dict(dicCode = "car_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**卸料时间*/
	@Excel(name = "卸料时间", width = 15)
    @ApiModelProperty(value = "卸料时间")
    private java.lang.String xlsj;
	/**卸完时间*/
	@Excel(name = "卸完时间", width = 15)
    @ApiModelProperty(value = "卸完时间")
    private java.lang.String xwsj;
	/**账期*/
	@Excel(name = "账期", width = 15)
    @ApiModelProperty(value = "账期")
    private java.lang.String zq;
	/**使用单位*/
	@Excel(name = "使用单位", width = 15)
    @ApiModelProperty(value = "使用单位")
    private java.lang.String customer;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;

    private String taiz;
    /**
     * Erp端ID
     */
    @Excel(name = "Erp端ID", width = 15)
    @ApiModelProperty(value = "Erp端ID")
    private java.lang.Integer erpid;
}
