package com.trtm.iot.projectinfo.entity;

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
 * @Description: project_info
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
@Data
@TableName("project_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="project_info对象", description="project_info")
public class ProjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**场地类别（3：公司； 4项目； 5指挥部 ； 6监理；  7标段； 8中心实验室；9 其他 ）*/
	@Excel(name = "场地类别（3公司；4项目；5指挥部；6监理；7标段；8中心实验室；9拌合站；10隧道；11梁场；12路基路面；13数字实验室；14建设物联网；100其他）", width = 15)
    @ApiModelProperty(value = "场地类别（3公司；4项目；5指挥部；6监理；7标段；8中心实验室；9拌合站；10隧道；11梁场；12路基路面；13数字实验室；14建设物联网；100其他）")
    @Dict(dicCode = "orgCategory")
    private java.lang.String orgCategory;
	/**场地名称*/
	@Excel(name = "场地名称", width = 15)
    @ApiModelProperty(value = "场地名称")
    private java.lang.String name;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
	/**所属省市*/
	@Excel(name = "所属省市",width = 15)
    @ApiModelProperty(value = "所属省市")
    private java.lang.String address;
    /**系统数据*/
    @Excel(name = "系统数据(项目结算)",width = 15)
    @ApiModelProperty(value = "系统数据(项目结算)")
    private java.lang.String sysdata;
    /**面板地址*/
    @Excel(name = "面板地址",width = 15)
    @ApiModelProperty(value = "面板地址")
    private java.lang.String mbdz;
    /**视频地址*/
    @Excel(name = "视频地址",width = 15)
    @ApiModelProperty(value = "视频地址")
    private java.lang.String spdz;
	/**相关介绍*/
	@Excel(name = "相关介绍", width = 15)
    @ApiModelProperty(value = "相关介绍")
    private java.lang.String describe1;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
    @ApiModelProperty(value = "图片地址")
    private java.lang.String fileimg;
	/**关联设备类型*/
	@Excel(name = "关联设备类型", width = 15)
    @ApiModelProperty(value = "关联设备类型")
    private java.lang.String sbtype;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String fuzeren;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String phone;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**工期*/
	@Excel(name = "工期", width = 15)
    @ApiModelProperty(value = "工期")
    private java.lang.String duration;

    private java.lang.Integer isshow;
}
