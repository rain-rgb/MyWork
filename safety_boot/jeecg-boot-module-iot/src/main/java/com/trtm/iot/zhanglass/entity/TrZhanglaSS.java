package com.trtm.iot.zhanglass.entity;

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
 * @Description: 张拉过程子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
@Data
@TableName("tr_zhangla_s_s")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_zhangla_s_s对象", description="张拉过程子表")
public class TrZhanglaSS implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private java.lang.String ssid;
	/**孔号，和主表gsbh相同*/
	@Excel(name = "孔号，和主表gsbh相同", width = 15)
    @ApiModelProperty(value = "孔号，和主表gsbh相同")
    private java.lang.String holeid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeibianhao;
    /**孔道名称*/
    @Excel(name = "孔道名称", width = 15)
    @ApiModelProperty(value = "孔道名称")
    private java.lang.String holename;
    /**梁名称*/
    @Excel(name = "梁名称", width = 15)
    @ApiModelProperty(value = "梁名称")
    private java.lang.String sname;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15)
    @ApiModelProperty(value = "记录时间")
    private java.lang.String jlsj;
	/**张拉次数*/
	@Excel(name = "张拉次数", width = 15)
    @ApiModelProperty(value = "张拉次数")
    private java.lang.String zlcs;
	/**状态1*/
	@Excel(name = "状态1", width = 15)
    @ApiModelProperty(value = "状态1")
    private java.lang.String zt1;
	/**张拉力1(KN)*/
	@Excel(name = "张拉力1(KN)", width = 15)
    @ApiModelProperty(value = "张拉力1(KN)")
    private java.lang.String zll1;
	/**油压1（Mpa）*/
	@Excel(name = "油压1（Mpa）", width = 15)
    @ApiModelProperty(value = "油压1（Mpa）")
    private java.lang.String yy1;
	/**顶行程1（mm）*/
	@Excel(name = "顶行程1（mm）", width = 15)
    @ApiModelProperty(value = "顶行程1（mm）")
    private java.lang.String dxc1;
	/**伸长量1（mm）*/
	@Excel(name = "伸长量1（mm）", width = 15)
    @ApiModelProperty(value = "伸长量1（mm）")
    private java.lang.String scl1;
	/**状态2*/
	@Excel(name = "状态2", width = 15)
    @ApiModelProperty(value = "状态2")
    private java.lang.String zt2;
	/**张拉力2（KN）*/
	@Excel(name = "张拉力2（KN）", width = 15)
    @ApiModelProperty(value = "张拉力2（KN）")
    private java.lang.String zll2;
	/**油压2（MPa）*/
	@Excel(name = "油压2（MPa）", width = 15)
    @ApiModelProperty(value = "油压2（MPa）")
    private java.lang.String yy2;
	/**顶行程2（mm）*/
	@Excel(name = "顶行程2（mm）", width = 15)
    @ApiModelProperty(value = "顶行程2（mm）")
    private java.lang.String dxc2;
	/**伸长量2（mm）*/
	@Excel(name = "伸长量2（mm）", width = 15)
    @ApiModelProperty(value = "伸长量2（mm）")
    private java.lang.String scl2;
}
