package com.trtm.iot.sysconfig.entity;

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
 * @Description: 定时任务配置表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Data
@TableName("sys_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_config对象", description="定时任务配置表")
public class SysConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**配置类型*/
	@Excel(name = "配置类型", width = 15)
    @ApiModelProperty(value = "配置类型")
    private java.lang.Integer cfgtype;
	/**当前表名*/
	@Excel(name = "当前表名", width = 15)
    @ApiModelProperty(value = "当前表名")
    private java.lang.String curtable;
	/**当前id*/
	@Excel(name = "当前id", width = 15)
    @ApiModelProperty(value = "当前id")
    private java.lang.Integer curid;
	/**curdate*/
	@Excel(name = "curdate", width = 15)
    @ApiModelProperty(value = "curdate")
    private java.lang.String curdate;
	/**正在工作中*/
	@Excel(name = "正在工作中", width = 15)
    @ApiModelProperty(value = "正在工作中")
    private java.lang.Integer isworking;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**用json*/
	@Excel(name = "用json", width = 15)
    @ApiModelProperty(value = "用json")
    private java.lang.String extra;
	/**批量条数*/
	@Excel(name = "批量条数", width = 15)
    @ApiModelProperty(value = "批量条数")
    private java.lang.Integer batchcount;

	private String token;
}
