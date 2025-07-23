package com.trtm.iot.bhzlqjipeistatistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 沥青级配
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Data
public class BhzLqJipei {
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
}
