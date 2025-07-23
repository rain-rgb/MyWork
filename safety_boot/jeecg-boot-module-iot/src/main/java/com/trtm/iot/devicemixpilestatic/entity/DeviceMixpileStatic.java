package com.trtm.iot.devicemixpilestatic.entity;

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
 * @Description: device_mixpile_static
 * @Author: jeecg-boot
 * @Date:   2022-01-24
 * @Version: V1.0
 */
@Data
@TableName("device_mixpile_static")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_static对象", description="device_mixpile_static")
public class DeviceMixpileStatic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    private java.lang.String orgcode;
	/**单位名称*/
    @ApiModelProperty(value = "单位名称")
    private java.lang.String utilname;
	/**统计日期*/
    @ApiModelProperty(value = "统计日期")
    private java.lang.String stadate;
	/**里程名称*/
	@Excel(name = "里程名称", width = 15)
    @ApiModelProperty(value = "里程名称")
    private java.lang.String mileage;
	/**桩基数（根）*/
	@Excel(name = "桩基数（根）", width = 15)
    @ApiModelProperty(value = "桩基数（根）")
    private java.lang.String pilecount;
	/**里程id*/
    @ApiModelProperty(value = "里程id")
    private java.lang.Integer mileageid;
	/**施工总长度（m）*/
	@Excel(name = "施工总长度（m）", width = 15)
    @ApiModelProperty(value = "施工总长度（m）")
    private java.lang.String worklength;
	/**总用灰量(方)*/
	@Excel(name = "总用灰量(方)", width = 15)
    @ApiModelProperty(value = "总用灰量(方)")
    private java.lang.String allash;
	/**水泥总用量（kg）*/
	@Excel(name = "水泥总用量（kg）", width = 15)
    @ApiModelProperty(value = "水泥总用量（kg）")
    private java.lang.String allcement;
	/**水泥浆总量*/
    @ApiModelProperty(value = "水泥浆总量")
    private java.lang.String allbeton;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeino;//设备编号
    private java.lang.Integer chaobiaototal;//超标数
    private java.lang.Integer piletype;//桩类型 0表示水泥搅拌桩，1表示高压旋喷桩，2:双头水泥搅拌桩 3: 双头高压旋喷桩
}
