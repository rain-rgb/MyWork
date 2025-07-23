package com.trtm.iot.system.entity;

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
 * @Description: 设备审核表
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Data
@TableName("shebei_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "shebei_info对象", description = "设备审核表")
public class ShebeiInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    /**
     * 设备类型
     */
    @Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "sbtype")
    private Integer sbtype;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String sbjno;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String sbname;
    /**
     * 设备简称
     */
    @Excel(name = "设备简称", width = 15)
    @ApiModelProperty(value = "设备简称")
    private String sbjsimplename;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer shebeiStatus;
    /**
     * 设备关联单位(kg/t)
     */
    @Excel(name = "设备关联单位(kg/t)", width = 15)
    @ApiModelProperty(value = "设备关联单位(kg/t)")
    private String unit;
    /**
     * 审核人
     */
    @Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String reviewBy;
    /**
     * 审核时间
     */
    @Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
    private Date reviewTime;
    /**
     * 驳回人
     */
    @Excel(name = "驳回人", width = 15)
    @ApiModelProperty(value = "驳回人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String turndownBy;
    /**
     * 驳回时间
     */
    @Excel(name = "驳回时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "驳回时间")
    private Date turndownTime;
    @ApiModelProperty(value = "testid")
    private Integer testid;
    @ApiModelProperty(value = "interfacetype")
    private Integer interfacetype;
    @ApiModelProperty(value = "status")
    @Dict(dicCode = "workstatus")
    private Integer status;
    /**
     * 经度
     */
    @Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private Double longitude;
    /**
     * 纬度
     */
    @Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private Double latitude;

    private String projectname;
    private String procode;
    private Integer status1;
    private String videolive;

    @Dict(dicCode = "dtubaud")
    private Integer dtubaud;

    private String projectid;
    private String sectionid;
    /**
     * 出料时间
     */
    @Excel(name = "数据上传时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据上传时间")
    private java.util.Date uploadDatetime;
    private Integer tuisongid;
    private String tunnelId;// 交工机构code
    private String tunnelName;// 交工机构名称

    private String millname;// 厂家名称

    private Integer ispd;//添加临建数据判断标示：0（否）1（是）
    private String responsperson;// 厂家名称
    private String responsphone;// 厂家名称
    private String zhijianid;// 质检资料推送路径端口号

}
