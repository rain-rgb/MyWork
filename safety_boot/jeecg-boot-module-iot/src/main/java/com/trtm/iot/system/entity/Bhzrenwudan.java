package com.trtm.iot.system.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
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
 * @Description: 任务单浇筑令
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Data
@TableName("bhzrenwudan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bhzrenwudan对象", description = "任务单浇筑令")
public class Bhzrenwudan implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 生产线（0公用 1 第一生产线  2 第二生产线）
     */
    @Excel(name = "生产线（0公用 1 第一生产线  2 第二生产线）", width = 15)
    @ApiModelProperty(value = "生产线（0公用 1 第一生产线  2 第二生产线）")
    @Dict(dicCode = "station")
    private Integer station;

    /**
     * 任务单编号
     */
    @Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private String code;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date dattim;

    /**
     * 任务性质
     */
    @Excel(name = "任务性质", width = 15)
    @ApiModelProperty(value = "任务性质")
    private String attribute;

    /**
     * 1线配合比编号
     */
    @Excel(name = "1线配合比编号", width = 15)
    @ApiModelProperty(value = "1线配合比编号")
    private String recipe;

    /**
     * 2线配合比编号
     */
    @Excel(name = "2线配合比编号", width = 15)
    @ApiModelProperty(value = "2线配合比编号")
    private String recipes;

    /**
     * 合同信息
     */
    @Excel(name = "合同信息", width = 15)
    @ApiModelProperty(value = "合同信息")
    private String contract;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private String customer;

    /**
     * 工程名称
     */
    @Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projname;

    /**
     * 工程类别
     */
    @Excel(name = "工程类别", width = 15)
    @ApiModelProperty(value = "工程类别")
    private String projtype;

    /**
     * 工程级别
     */
    @Excel(name = "工程级别", width = 15)
    @ApiModelProperty(value = "工程级别")
    private String projgrade;

    /**
     * 开工面积
     */
    @Excel(name = "开工面积", width = 15)
    @ApiModelProperty(value = "开工面积")
    private Double projarea;

    /**
     * 施工地址
     */
    @Excel(name = "施工地址", width = 15)
    @ApiModelProperty(value = "施工地址")
    private String projadr;

    /**
     * 运输距离
     */
    @Excel(name = "运输距离", width = 15)
    @ApiModelProperty(value = "运输距离")
    private Double distance;

    /**
     * 施工部位
     */
    @Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String conspos;

    /**
     * 浇筑方式
     */
    @Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    @Dict(dicCode = "pour")
    private String pour;

    /**
     * 产品种类
     */
    @Excel(name = "产品种类", width = 15)
    @ApiModelProperty(value = "产品种类")
    private String variety;

    /**
     * 强度等级
     */
    @Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    @Dict(dicCode = "betlev")
    private String betlev;

    /**
     * 抗渗等级
     */
    @Excel(name = "抗渗等级", width = 15)
    @ApiModelProperty(value = "抗渗等级")
    private String filter;

    /**
     * 抗冻等级
     */
    @Excel(name = "抗冻等级", width = 15)
    @ApiModelProperty(value = "抗冻等级")
    private String freeze;

    /**
     * 坍落度
     */
    @Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    @Dict(dicCode = "lands")
    private String lands;

    /**
     * 水泥品种
     */
    @Excel(name = "水泥品种", width = 15)
    @ApiModelProperty(value = "水泥品种")
    private String cement;

    /**
     * 石子种类
     */
    @Excel(name = "石子种类", width = 15)
    @ApiModelProperty(value = "石子种类")
    private String stone;

    /**
     * 骨料粒径
     */
    @Excel(name = "骨料粒径", width = 15)
    @ApiModelProperty(value = "骨料粒径")
    private String bnsize;

    /**
     * 外加剂要求
     */
    @Excel(name = "外加剂要求", width = 15)
    @ApiModelProperty(value = "外加剂要求")
    private String addliq;

    /**
     * 技术要求
     */
    @Excel(name = "技术要求", width = 15)
    @ApiModelProperty(value = "技术要求")
    private String request;

    /**
     * 搅拌时间
     */
    @Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private Double mixlast;

    /**
     * 任务方量
     */
    @Excel(name = "任务方量", width = 15)
    @ApiModelProperty(value = "任务方量")
    private Double mete;

    /**
     * 节超方量
     */
    @Excel(name = "节超方量", width = 15)
    @ApiModelProperty(value = "节超方量", dataType = "Double")
    @TableField(exist = false)
    private Double dValue;

    /**
     * 浇注日期
     */
    @Excel(name = "浇注日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇注日期")
    private Date begtim;

    /**
     * 截止日期
     */
    @Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止日期")
    private Date endtim;

    /**
     * 调度人员
     */
    @Excel(name = "调度人员", width = 15)
    @ApiModelProperty(value = "调度人员")
    private String attamper;

    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String flag;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;

    /**
     * 是否删除 0未删除 1已删除
     */
    @Excel(name = "是否删除 0未删除 1已删除", width = 15)
    @ApiModelProperty(value = "是否删除 0未删除 1已删除")
    private Integer isdel;

    @ApiModelProperty(value = "0:未审核,1:已审核,2:审核未配料,3:配料未生产,4:生产中,5:已完成,6:已滞后", dataType = "Integer")
    @Dict(dicCode = "task_status")
    private Integer status;//数据库默认是1  已审核

    private String treeid;//施工部位节点id

    private Integer isfinish;//是否完成

    private String orderno;//订单编号

    @TableField(exist = false)
    @ApiModelProperty(value = "配料单列表")
    private List<Shigongphb> cSList;

    @TableField(exist = false)
    @ApiModelProperty(value = "任务单进度详情")
    private List<RenwudanSchedule> tS;

    @ApiModelProperty(value = "盘数量", dataType = "Integer")
    @TableField("dish_count")
    private Integer dishCount;

    private String spyj;
    private String phone;
    // 是否推送质检资料
    private Integer iszjzl;

    @Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private String shren;
    @Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private Date shshijian;

    private String wbsStructureType;

    //以下是中铁一局新增字段

    private String taiz;
    private String metes;
    private String shiyanqk;
    private String gjjietou;
    private String fjfile;
    private String gxwbs;

    /**进度百分比*/
    @Excel(name = "进度百分比", width = 15)
    @ApiModelProperty(value = "进度百分比")
    @TableField(exist = false)
    private java.lang.Double bfb  ;

    @TableField(exist = false)
    private java.lang.Integer jzlsts  ;

    public Integer getJzlsts() {
        return this.status;
    }

    /*
     * get set方法
     */
    public Double getBfb() {
        if(metes!=null&&mete!=null){
            double num = Double.parseDouble(metes)  / mete * 100;
            double v = (double) Math.round(num * 100) / 100;
            return v>100?100:v;
        }
        return null;
    }
}
