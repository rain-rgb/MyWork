package com.trtm.sy.symainconf.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sy_main_conf
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Data
@TableName("sy_main_conf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sy_main_conf对象", description = "sy_main_conf")
public class SyMainConf implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 唯一id
     */
    @Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String uuid;
    /**
     * 试验ID
     */
    @Excel(name = "试验ID", width = 15)
    @ApiModelProperty(value = "试验ID")
    private String testId;
    /**
     * 试验名称
     */
    @Excel(name = "试验名称", width = 15)
    @ApiModelProperty(value = "试验名称")
    private String testName;
    /**
     * 试验类型，与dps_jc_testItemType表titCode关联
     */
    @Excel(name = "试验类型，与dps_jc_testItemType表titCode关联", width = 15)
    @ApiModelProperty(value = "试验类型，与dps_jc_testItemType表titCode关联")
    private String testType;
    /**
     * 工序编号，与sys_main_process表process_id关联
     */
    @Excel(name = "工序编号，与sys_main_process表process_id关联", width = 15)
    @ApiModelProperty(value = "工序编号，与sys_main_process表process_id关联")
    private String processId;
    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 组织机构编码
     */
    @Excel(name = "组织机构编码", width = 15)
    @ApiModelProperty(value = "组织机构编码")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysorgcode;
}
