package com.trtm.iot.hc_datalinkage.vo;

import java.util.List;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkagePave;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="hc_datalinkagePage对象", description="数据联动（运输信息）")
public class HcDatalinkagePage {

	/**记录id，唯一递增*/
	@ApiModelProperty(value = "记录id，唯一递增")
    private java.lang.Integer id;
	/**运输车id*/
	@Excel(name = "运输车id", width = 15)
	@ApiModelProperty(value = "运输车id")
    private java.lang.String truckid;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15)
	@ApiModelProperty(value = "出厂时间")
    private java.lang.String outstationtime;
	/**卸料时间*/
	@Excel(name = "卸料时间", width = 15)
	@ApiModelProperty(value = "卸料时间")
    private java.lang.String loadoffmixturetime;
	/**接料时长*/
	@Excel(name = "接料时长", width = 15)
	@ApiModelProperty(value = "接料时长")
    private java.lang.String loadmixturetime;
	/**摊铺记录id数组，逗号分隔*/
	@Excel(name = "摊铺记录id数组，逗号分隔", width = 15)
	@ApiModelProperty(value = "摊铺记录id数组，逗号分隔")
    private java.lang.String pavemixtureinfoids;
	/**混合料生产详情id数组，逗号分隔*/
	@Excel(name = "混合料生产详情id数组，逗号分隔", width = 15)
	@ApiModelProperty(value = "混合料生产详情id数组，逗号分隔")
    private java.lang.String pavemixturematerialinfoids;
	/**混合料类型，1是水稳，2是沥青*/
	@Excel(name = "混合料类型，1是水稳，2是沥青", width = 15)
	@ApiModelProperty(value = "混合料类型，1是水稳，2是沥青")
    private java.lang.Integer materialtype;

	@ExcelCollection(name="数据联动（摊铺数据）")
	@ApiModelProperty(value = "数据联动（摊铺数据）")
	private List<HcDatalinkagePave> paveList;

}
