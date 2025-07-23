package com.trtm.iot.jikeng.entity;

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
 * @Description: jikeng_anquan_zhuti
 * @Author: jeecg-boot
 * @Date:   2025-03-20
 * @Version: V1.0
 */
@Data
@TableName("jikeng_anquan_zhuti")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="jikeng_anquan_zhuti对象", description="jikeng_anquan_zhuti")
public class JikengAnquanZhuti implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**测点*/
	@Excel(name = "测点", width = 15)
    @ApiModelProperty(value = "测点")
    private java.lang.String cedian;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date datatime;
	/**原始测值（频率）*/
	@Excel(name = "原始测值（频率）", width = 15)
    @ApiModelProperty(value = "原始测值（频率）")
    private java.lang.String abparam1;
	/**原始测值温度*/
	@Excel(name = "原始测值温度", width = 15)
    @ApiModelProperty(value = "原始测值温度")
    private java.lang.String abparam2;
	/**成果值*/
	@Excel(name = "成果值", width = 15)
    @ApiModelProperty(value = "成果值")
    private java.lang.String abparam3;
	/**成果值温度（℃）*/
	@Excel(name = "成果值温度（℃）", width = 15)
    @ApiModelProperty(value = "成果值温度（℃）")
    private java.lang.String abparam4;
	/**情况*/
	@Excel(name = "情况", width = 15)
    @ApiModelProperty(value = "情况")
    private java.lang.String abparam5;
	/**备用*/
	@Excel(name = "备用", width = 15)
    @ApiModelProperty(value = "备用")
    private java.lang.String abparam6;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
	/**基底应力计测值；土压力计监测；单向测缝计测值；钢筋应力计测值；混凝土应变计组；无应力计监测*/
	@Excel(name = "基底应力计测值；土压力计监测；单向测缝计测值；钢筋应力计测值；混凝土应变计组；无应力计监测", width = 15)
    @ApiModelProperty(value = "基底应力计测值；土压力计监测；单向测缝计测值；钢筋应力计测值；混凝土应变计组；无应力计监测")
    private java.lang.String cediantype;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**sysOrgCode*/
    @ApiModelProperty(value = "sysOrgCode")
    private java.lang.String sysOrgCode;
}
