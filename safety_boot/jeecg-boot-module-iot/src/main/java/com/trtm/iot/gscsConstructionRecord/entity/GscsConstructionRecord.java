package com.trtm.iot.gscsConstructionRecord.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 班组安全管控系统未施工记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
@Data
@TableName("gscs_construction_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="gscs_construction_record对象", description="班组安全管控系统未施工记录表")
public class GscsConstructionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**标段*/
	@Excel(name = "标段", width = 15)
    @ApiModelProperty(value = "标段")
    private java.lang.String section;
	/**班组*/
	@Excel(name = "班组", width = 15)
    @ApiModelProperty(value = "班组")
    private java.lang.String deptId;
	/**是否未施工（0否，1是）*/
	@Excel(name = "是否未施工（0否，1是）", width = 15)
    @ApiModelProperty(value = "是否未施工（0否，1是）")
    private java.lang.Integer isConstruction;
	/**上传日期*/
	@Excel(name = "上传日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传日期")
    private java.util.Date uploadDate;
	/**上传时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date createTime;
}
