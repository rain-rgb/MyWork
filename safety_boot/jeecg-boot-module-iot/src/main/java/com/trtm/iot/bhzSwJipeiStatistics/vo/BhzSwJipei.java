package com.trtm.iot.bhzSwJipeiStatistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 沥青级配
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@Data
public class BhzSwJipei {
	/**筛孔*/
	@Excel(name = "筛孔", width = 15)
	@ApiModelProperty(value = "筛孔")
	private String shaikong;
	/**合成级配*/
	@Excel(name = "合成级配", width = 15)
	@ApiModelProperty(value = "合成级配")
	private Double hechengjipei;
	/**中值*/
	@Excel(name = "中值", width = 15)
	@ApiModelProperty(value = "中值")
	private String zhongzhi;
	/**上限*/
	@Excel(name = "上限", width = 15)
	@ApiModelProperty(value = "上限")
	private String shangxian;
	/**下限*/
	@Excel(name = "下限", width = 15)
	@ApiModelProperty(value = "下限")
	private String xiaxian;
}
