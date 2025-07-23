package com.trtm.iot.bhzSwJipeiStatistics.entity;

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
 * @Description: bhz_sw_jipei_statistics
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Data
@TableName("bhz_sw_jipei_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_sw_jipei_statistics对象", description="bhz_sw_jipei_statistics")
public class BhzSwJipeiStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**沥青拌合唯一标识*/
	@Excel(name = "沥青拌合唯一标识", width = 15)
    @ApiModelProperty(value = "沥青拌合唯一标识")
    private java.lang.String baseid;
	/**出料时间*/
	@Excel(name = "出料时间", width = 15)
    @ApiModelProperty(value = "出料时间")
    private java.lang.String chuliaoshijian;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbjno;
	/**筛孔*/
	@Excel(name = "筛孔", width = 15)
    @ApiModelProperty(value = "筛孔")
    private java.lang.String shaikong;
	/**合成级配*/
	@Excel(name = "合成级配", width = 15)
    @ApiModelProperty(value = "合成级配")
    private java.lang.Double hechengjipei;
	/**中值*/
	@Excel(name = "中值", width = 15)
    @ApiModelProperty(value = "中值")
    private java.lang.String zhongzhi;
	/**上限*/
	@Excel(name = "上限", width = 15)
    @ApiModelProperty(value = "上限")
    private java.lang.String shangxian;
	/**下限*/
	@Excel(name = "下限", width = 15)
    @ApiModelProperty(value = "下限")
    private java.lang.String xiaxian;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createtime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updatetime;
}
