package com.trtm.iot.bhzrwdxx.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: bhzrwdxx
 * @Author: jeecg-boot
 * @Date:   2022-03-02
 * @Version: V1.0
 */
@Data
@TableName("bhzrwdxx")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhzrwdxx对象", description="bhzrwdxx")
public class Bhzrwdxx implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**生产线（0公用 1 第一生产线  2 第二生产线）*/
	@Excel(name = "生产线（0公用 1 第一生产线  2 第二生产线）", width = 15)
    @ApiModelProperty(value = "生产线（0公用 1 第一生产线  2 第二生产线）")
    @Dict(dicCode = "station")
    private java.lang.Integer station;
	/**任务单编号*/
	@Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private java.lang.String rwdcode;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date dattim;
	/**任务性质*/
	@Excel(name = "任务性质", width = 15)
    @ApiModelProperty(value = "任务性质")
    private java.lang.String attribute;
	/**1线配合比编号*/
	@Excel(name = "1线配合比编号", width = 15)
    @ApiModelProperty(value = "1线配合比编号")
    private java.lang.String recipe;
	/**2线配合比编号*/
	@Excel(name = "2线配合比编号", width = 15)
    @ApiModelProperty(value = "2线配合比编号")
    private java.lang.String recipes;
	/**合同信息*/
	@Excel(name = "合同信息", width = 15)
    @ApiModelProperty(value = "合同信息")
    private java.lang.String contract;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private java.lang.String customer;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projname;
	/**工程类别*/
	@Excel(name = "工程类别", width = 15)
    @ApiModelProperty(value = "工程类别")
    private java.lang.String projtype;
	/**工程级别*/
	@Excel(name = "工程级别", width = 15)
    @ApiModelProperty(value = "工程级别")
    private java.lang.String projgrade;
	/**开工面积*/
	@Excel(name = "开工面积", width = 15)
    @ApiModelProperty(value = "开工面积")
    private java.lang.Double projarea;
	/**施工地址*/
	@Excel(name = "施工地址", width = 15)
    @ApiModelProperty(value = "施工地址")
    private java.lang.String projadr;
	/**运输距离*/
	@Excel(name = "运输距离", width = 15)
    @ApiModelProperty(value = "运输距离")
    private java.lang.Double distance;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String conspos;
	/**浇筑方式*/
	@Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    private java.lang.String pour;
	/**产品种类*/
	@Excel(name = "产品种类", width = 15)
    @ApiModelProperty(value = "产品种类")
    private java.lang.String variety;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String betlev;
	/**抗渗等级*/
	@Excel(name = "抗渗等级", width = 15)
    @ApiModelProperty(value = "抗渗等级")
    private java.lang.String filter;
	/**抗冻等级*/
	@Excel(name = "抗冻等级", width = 15)
    @ApiModelProperty(value = "抗冻等级")
    private java.lang.String freeze;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    private java.lang.String lands;
	/**水泥品种*/
	@Excel(name = "水泥品种", width = 15)
    @ApiModelProperty(value = "水泥品种")
    private java.lang.String cement;
	/**石子种类*/
	@Excel(name = "石子种类", width = 15)
    @ApiModelProperty(value = "石子种类")
    private java.lang.String stone;
	/**骨料粒径*/
	@Excel(name = "骨料粒径", width = 15)
    @ApiModelProperty(value = "骨料粒径")
    private java.lang.String bnsize;
	/**外加剂要求*/
	@Excel(name = "外加剂要求", width = 15)
    @ApiModelProperty(value = "外加剂要求")
    private java.lang.String addliq;
	/**技术要求*/
	@Excel(name = "技术要求", width = 15)
    @ApiModelProperty(value = "技术要求")
    private java.lang.String request;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private java.lang.Double mixlast;
	/**任务方量*/
	@Excel(name = "任务方量", width = 15)
    @ApiModelProperty(value = "任务方量")
    private java.lang.Double mete;
	/**浇注日期*/
	@Excel(name = "浇注日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇注日期")
    private java.util.Date begtim;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止日期")
    private java.util.Date endtim;
	/**调度人员*/
	@Excel(name = "调度人员", width = 15)
    @ApiModelProperty(value = "调度人员")
    private java.lang.String attamper;
	/**标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private java.lang.String flag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**是否删除 0未删除 1已删除*/
	@Excel(name = "是否删除 0未删除 1已删除", width = 15)
    @ApiModelProperty(value = "是否删除 0未删除 1已删除")
    private java.lang.Integer isdel;
	/**0 未审核  1已审核*/
//	@Excel(name = "0 未审核  1已审核", width = 15)
//    @ApiModelProperty(value = "0 未审核  1已审核")
//    private java.lang.Integer status;
	/**施工部位节点id*/
	@Excel(name = "施工部位节点id", width = 15)
    @ApiModelProperty(value = "施工部位节点id")
    private java.lang.String treeid;
	/**生产方量*/
	@Excel(name = "生产方量", width = 15)
    @ApiModelProperty(value = "生产方量")
    private java.lang.Double metes;
	/**进度百分比*/
	@Excel(name = "进度百分比", width = 15)
    @ApiModelProperty(value = "进度百分比")
    @TableField(exist = false)
    private java.lang.Double bfb  ;

    /*
     * get set方法
     */
    public Double getBfb() {
        if(metes!=null&&mete!=null){
            double num = metes / mete * 100;
            double v = (double) Math.round(num * 100) / 100;
            return v>100?100:v;
        }
        return null;
    }


    /**jzlsts*/
	@Excel(name = "jzlsts", width = 15)
    @ApiModelProperty(value = "jzlsts")
    private java.lang.Integer jzlsts;
    private java.lang.String orderno;//订单编号


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生产结束时间")
    private java.util.Date endtim1;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生产开始时间")
    private java.util.Date starttim;

    /**设备编号*/
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;


}
