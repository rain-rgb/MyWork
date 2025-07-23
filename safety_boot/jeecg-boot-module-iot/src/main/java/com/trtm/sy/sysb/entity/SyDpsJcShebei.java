package com.trtm.sy.sysb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: sy_dps_jc_shebei
 * @Author: jeecg-boot
 * @Date: 2023-10-16
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_shebei")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_dps_jc_shebei对象", description = "sy_dps_jc_shebei")
public class SyDpsJcShebei implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键UUID")
    private String id;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeino;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String shebeiname;
    /**
     * 设备规格
     */
    @Excel(name = "设备规格", width = 15)
    @ApiModelProperty(value = "设备规格")
    private String shebeiguige;
    /**
     * 设备生产厂家
     */
    @Excel(name = "设备生产厂家", width = 15)
    @ApiModelProperty(value = "设备生产厂家")
    private String shebeichangjia;
    /**
     * 设备购置日期(yyyy-MM-dd)
     */
    @Excel(name = "设备购置日期(yyyy-MM-dd)", width = 15)
    @ApiModelProperty(value = "设备购置日期(yyyy-MM-dd)")
    private String shebeigouzhiriqi;
    /**
     * 设备校准周期(月)
     */
    @Excel(name = "设备校准周期(月)", width = 15)
    @ApiModelProperty(value = "设备校准周期(月)")
    private String shebeijiaozhunzhouqi;
    /**
     * 设备出厂日期(yyyy-MM-dd)
     */
    @Excel(name = "设备出厂日期(yyyy-MM-dd)", width = 15)
    @ApiModelProperty(value = "设备出厂日期(yyyy-MM-dd)")
    private String shebeichuchangriqi;
    /**
     * 设备出厂编号(yyyy-MM-dd)
     */
    @Excel(name = "设备出厂编号(yyyy-MM-dd)", width = 15)
    @ApiModelProperty(value = "设备出厂编号(yyyy-MM-dd)")
    private String shebeichuchangbianhao;
    /**
     * 设备存放地点
     */
    @Excel(name = "设备存放地点", width = 15)
    @ApiModelProperty(value = "设备存放地点")
    private String shebeicunfangdidian;
    /**
     * 设备供货商
     */
    @Excel(name = "设备供货商", width = 15)
    @ApiModelProperty(value = "设备供货商")
    private String shebeigonghuoshang;
    /**
     * 设备管理人
     */
    @Excel(name = "设备管理人", width = 15)
    @ApiModelProperty(value = "设备管理人")
    private String shebeiguanliren;
    /**
     * 设备数量
     */
    @Excel(name = "设备数量", width = 15)
    @ApiModelProperty(value = "设备数量")
    private Integer shebeishuliang;
    /**
     * 设备单价(元)
     */
    @Excel(name = "设备单价(元)", width = 15)
    @ApiModelProperty(value = "设备单价(元)")
    private String shebeidanjia;
    /**
     * 设备图片（上传）
     */
    @Excel(name = "设备图片（上传）", width = 15)
    @ApiModelProperty(value = "设备图片（上传）")
    private String shebeiimage;
    /**
     * 使用状态（合格，准用，停用）使用数据字典
     */
    @Excel(name = "使用状态（合格，准用，停用）使用数据字典", width = 15)
    @ApiModelProperty(value = "使用状态（合格，准用，停用）使用数据字典")
    private String shebeishiyongzhuangtai;
    /**
     * 计量单位（吨，千克，台，套，把，米，平方米，立方米，公里，个）使用数据字典
     */
    @Excel(name = "计量单位（吨，千克，台，套，把，米，平方米，立方米，公里，个）使用数据字典", width = 15)
    @ApiModelProperty(value = "计量单位（吨，千克，台，套，把，米，平方米，立方米，公里，个）使用数据字典")
    private String shebeijiliangdanwei;
    /**
     * 设备备注
     */
    @Excel(name = "设备备注", width = 15)
    @ApiModelProperty(value = "设备备注")
    private String shebeiremark;
    /**
     * 外键，组织机构id(T_S_Depart)
     */
    @Excel(name = "外键，组织机构id(T_S_Depart)", width = 15)
    @ApiModelProperty(value = "外键，组织机构id(T_S_Depart)")
    private String departid;
    /**
     * 外键，组织机构编码（T_S_Depart）
     */
    @Excel(name = "外键，组织机构编码（T_S_Depart）", width = 15)
    @ApiModelProperty(value = "外键，组织机构编码（T_S_Depart）")
    private String orgcode;
    /**
     * 是否删除（0：正常1：已删除）
     */
    @Excel(name = "是否删除（0：正常1：已删除）", width = 15)
    @ApiModelProperty(value = "是否删除（0：正常1：已删除）")
    @TableLogic(value = "0", delval = "1")
    private Integer shebeiisdel;
    /**
     * 外键，试验类型（dps_jc_testItemType）
     */
    @Excel(name = "外键，试验类型（dps_jc_testItemType）", width = 15)
    @ApiModelProperty(value = "外键，试验类型（dps_jc_testItemType）")
    private String titcode;
    /**
     * 检定周期（月）
     */
    @Excel(name = "检定周期（月）", width = 15)
    @ApiModelProperty(value = "检定周期（月）")
    private String shebeijiandingzhouqi;
    /**
     * 检定单位
     */
    @Excel(name = "检定单位", width = 15)
    @ApiModelProperty(value = "检定单位")
    private String shebeijiandingdanwei;
    /**
     * 最近检定日期(YYYY-MM-dd)
     */
    @Excel(name = "最近检定日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "最近检定日期(YYYY-MM-dd)")
    private String shebeizuijinjiandingriqi;
    /**
     * 下次检定日期(YYYY-MM-dd)
     */
    @Excel(name = "下次检定日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "下次检定日期(YYYY-MM-dd)")
    private String shebeixiacijiandingriqi;
    /**
     * 最近校准日期(YYYY-MM-dd)
     */
    @Excel(name = "最近校准日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "最近校准日期(YYYY-MM-dd)")
    private String shebeizuijinjiaozhunriqi;
    /**
     * 下次校准日期(YYYY-MM-dd)
     */
    @Excel(name = "下次校准日期(YYYY-MM-dd)", width = 15)
    @ApiModelProperty(value = "下次校准日期(YYYY-MM-dd)")
    private String shebeixiacijiaozhunriqi;
    /**
     * 校准单位
     */
    @Excel(name = "校准单位", width = 15)
    @ApiModelProperty(value = "校准单位")
    private String shebeijiaozhundanwei;
    /**
     * 创建人，获取当前登录用户
     */
    @Excel(name = "创建人，获取当前登录用户", width = 15)
    @ApiModelProperty(value = "创建人，获取当前登录用户")
    private String chuangjianren;
    /**
     * 创建日期（YYYY-MM-dd）
     */
    @Excel(name = "创建日期（YYYY-MM-dd）", width = 15)
    @ApiModelProperty(value = "创建日期（YYYY-MM-dd）")
    private String chuangjianriqi;
    /**
     * 0：创建 1：导入
     */
    @Excel(name = "0：创建 1：导入", width = 15)
    @ApiModelProperty(value = "0：创建 1：导入")
    private Integer chuangjianleibie;

}
