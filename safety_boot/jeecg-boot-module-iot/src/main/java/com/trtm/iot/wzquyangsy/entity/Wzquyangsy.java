package com.trtm.iot.wzquyangsy.entity;

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
 * @Description: 原材料物资取样表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Data
@TableName("wzquyangsy")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzquyangsy对象", description="原材料物资取样表信息")
public class Wzquyangsy implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**台账id*/
	@Excel(name = "台账id", width = 15)
    @ApiModelProperty(value = "台账id")
    private java.lang.Integer taizhangid;
	/**取样时间*/
	@Excel(name = "取样时间", width = 15)
    @ApiModelProperty(value = "取样时间")
    private java.lang.String quyangshijian;
	/**qypic*/
	@Excel(name = "qypic", width = 15)
    @ApiModelProperty(value = "qypic")
    private java.lang.String qypic;
	/**gzpic*/
	@Excel(name = "gzpic", width = 15)
    @ApiModelProperty(value = "gzpic")
    private java.lang.String gzpic;
	/**jlpic*/
	@Excel(name = "jlpic", width = 15)
    @ApiModelProperty(value = "jlpic")
    private java.lang.String jlpic;
	/**pzpic*/
	@Excel(name = "pzpic", width = 15)
    @ApiModelProperty(value = "pzpic")
    private java.lang.String pzpic;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**sy1pic*/
	@Excel(name = "sy1pic", width = 15)
    @ApiModelProperty(value = "sy1pic")
    private java.lang.String sy1pic;
	/**sy2pic*/
	@Excel(name = "sy2pic", width = 15)
    @ApiModelProperty(value = "sy2pic")
    private java.lang.String sy2pic;
	/**systate*/
	@Excel(name = "systate", width = 15)
    @ApiModelProperty(value = "systate")
    private java.lang.String systate;
	/**shouyang1pic*/
	@Excel(name = "shouyang1pic", width = 15)
    @ApiModelProperty(value = "shouyang1pic")
    private java.lang.String shouyang1pic;
	/**shouyang2pic*/
	@Excel(name = "shouyang2pic", width = 15)
    @ApiModelProperty(value = "shouyang2pic")
    private java.lang.String shouyang2pic;
	/**提交判断 1为已提交*/
	@Excel(name = "提交判断 1为已提交", width = 15)
    @ApiModelProperty(value = "提交判断 1为已提交")
    private java.lang.String tjstate;

    private java.lang.String pici;//批次号
    private java.lang.String sytime;// 收样时间
    private java.lang.String qrcode;
    private java.lang.String file;
    private java.lang.String sytype;
    private  java.lang.String wtbh;// 委托编号

}
