package com.trtm.sy.sysbjdjz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: sy_dps_jc_shebei_jiandingjiaozhun
 * @Author: jeecg-boot
 * @Date: 2023-10-17
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_jc_shebei_jiandingjiaozhun")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_dps_jc_shebei_jiandingjiaozhun对象", description = "sy_dps_jc_shebei_jiandingjiaozhun")
public class SyDpsJcShebeiJiandingjiaozhun implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号，主键，UUID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号，主键，UUID")
    private String id;
    /**
     * 检定校准日期
     */
    @Excel(name = "检定校准日期", width = 15)
    @ApiModelProperty(value = "检定校准日期")
    private String jiandingjiaozhunriqi;
    /**
     * 检定校准人
     */
    @Excel(name = "检定校准人", width = 15)
    @ApiModelProperty(value = "检定校准人")
    private String jiandingjiaozhunren;
    /**
     * 检定校准单位
     */
    @Excel(name = "检定校准单位", width = 15)
    @ApiModelProperty(value = "检定校准单位")
    private String jiandingjiaozhundanwei;
    /**
     * 检定校准结果
     */
    @Excel(name = "检定校准结果", width = 15)
    @ApiModelProperty(value = "检定校准结果")
    private String jiandingjiaozhunjieguo;
    /**
     * 0：检定 1：校准
     */
    @Excel(name = "0：检定 1：校准", width = 15)
    @ApiModelProperty(value = "0：检定 1：校准")
    private Integer jiandingjiaozhunleibie;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String jiandingjiaozhunbeizhu;
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
     * 外键，来源dps_jc_shebei表id字段
     */
    @Excel(name = "外键，来源dps_jc_shebei表id字段", width = 15)
    @ApiModelProperty(value = "外键，来源dps_jc_shebei表id字段")
    private String shebeiid;

    @TableField(exist = false)
    private String shebeiname;
    @TableField(exist = false)
    private String shebeino;
}
