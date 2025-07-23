package com.trtm.iot.hc_transportrecords.vo;

import java.util.List;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
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
 * @Description: 运输数据
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
@Data
@ApiModel(value="hc_transportrecordsPage对象", description="运输数据")
public class HcTransportrecordsPage {

	/**数据id，唯一递增*/
	@ApiModelProperty(value = "数据id，唯一递增")
    private java.lang.Integer id;
	/**运输车id*/
	@Dict(dictTable = "hc_truck", dicText = "licensePlate", dicCode = "truckId")
	@Excel(name = "运输车id", width = 15)
	@ApiModelProperty(value = "运输车id")
    private java.lang.String truckid;
	@Excel(name = "拌和站id", width = 15)
	@ApiModelProperty(value = "拌和站id")
    private java.lang.String stationid;
	/**拌和站名称*/
	@Excel(name = "拌和站名称", width = 15)
	@ApiModelProperty(value = "拌和站名称")
    private java.lang.String stationname;
	/**出厂时间*/
	@Excel(name = "出厂时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "出厂时间")
    private java.util.Date outstationtime;
	/**运输时长*/
	@Excel(name = "运输时长", width = 15)
	@ApiModelProperty(value = "运输时长")
    private java.lang.String transporttime;

	@Dict(dictTable = "hc_project", dicText = "pjName", dicCode = "pjId")
    private java.lang.String projectId;

	@Dict(dictTable = "hc_section", dicText = "sectionName", dicCode = "sectionId")
    private java.lang.String sectionId;

    private java.lang.String temperature;

	@ExcelCollection(name="运输数据子表")
	@ApiModelProperty(value = "运输数据子表")
	private List<HcTransportrecordsPave> paveList;

}
