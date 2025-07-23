package com.trtm.iot.kanbaninfo.entity;

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
 * @Description: kanbaninfo
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Data
@TableName("kanbaninfo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="kanbaninfo对象", description="kanbaninfo")
public class Kanbaninfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**kanbanname*/
	@Excel(name = "kanbanname", width = 15)
    @ApiModelProperty(value = "kanbanname")
    private java.lang.String kanbanname;
	/**1:梁场；2.隧道；3：梁场主要岗位负责人 4：人员工种分布图*/
	@Excel(name = "1:梁场；2.隧道；3：梁场主要岗位负责人 4：人员工种分布图", width = 15)
    @ApiModelProperty(value = "1:梁场；2.隧道；3：梁场主要岗位负责人 4：人员工种分布图")
    private java.lang.Integer type;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String sysOrgCode;
	/**负责人姓名*/
	@Excel(name = "负责人姓名", width = 15)
    @ApiModelProperty(value = "负责人姓名")
    private java.lang.String people;
	/**总数量（制梁）*/
	@Excel(name = "总数量（制梁）", width = 15)
    @ApiModelProperty(value = "总数量（制梁）")
    private java.lang.String allcount;
	/**负责人职位*/
	@Excel(name = "负责人职位", width = 15)
    @ApiModelProperty(value = "负责人职位")
    private java.lang.String job;
	/**负责人状态；隧道图片*/
	@Excel(name = "负责人状态；隧道图片", width = 15)
    @ApiModelProperty(value = "负责人状态；隧道图片")
    private java.lang.String pic;
	/**负责人联系方式*/
	@Excel(name = "负责人联系方式", width = 15)
    @ApiModelProperty(value = "负责人联系方式")
    private java.lang.String phone;
	/**人员工种分布图*/
	@Excel(name = "人员工种分布图", width = 15)
    @ApiModelProperty(value = "人员工种分布图")
    private java.lang.String peoplesf;
	/**2 隧道介绍*/
	@Excel(name = "2 隧道介绍", width = 15)
    @ApiModelProperty(value = "2 隧道介绍")
    private java.lang.String suidaoinfo;
}
