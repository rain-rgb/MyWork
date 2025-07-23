package org.jeecg.modules.job.jgdatapush.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
@Data
@ApiModel(value="BhzCementBasePageJG对象", description="拌合站主表")
public class BhzCementBasePageJG {

	/**主键id*/
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**唯一ID*/
	@Excel(name = "唯一ID", width = 15)
	@ApiModelProperty(value = "唯一ID")
	private String batchNo;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
	@ApiModelProperty(value = "设备编号")
	@Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
	private String shebeiNo;
	/**工单号*/
	@Excel(name = "工单号", width = 15)
	@ApiModelProperty(value = "工单号")
	private String workNo;
	/**操作者*/
	@Excel(name = "操作者", width = 15)
	@ApiModelProperty(value = "操作者")
	private String handlers;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
	@ApiModelProperty(value = "工程名称")
	private String projectName;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
	@ApiModelProperty(value = "施工地点")
	private String jobLocation;
	/**浇筑部位*/
	@Excel(name = "浇筑部位", width = 15)
	@ApiModelProperty(value = "浇筑部位")
	private String poureLocation;
	/**水泥品种*/
	@Excel(name = "水泥品种", width = 15)
	@ApiModelProperty(value = "水泥品种")
	private String cementVariety;
	/**外加剂品种*/
	@Excel(name = "外加剂品种", width = 15)
	@ApiModelProperty(value = "外加剂品种")
	private String additiveVariety;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
	@ApiModelProperty(value = "配方号")
	private String formulaNo;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
	@ApiModelProperty(value = "强度等级")
	private String strengthRank;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
	@ApiModelProperty(value = "搅拌时间")
	private Integer stirDatetime;
	/**保存时间*/
	@Excel(name = "保存时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "保存时间")
	private Date saveDatetime;
	/**客户端编号*/
	@Excel(name = "客户端编号", width = 15)
	@ApiModelProperty(value = "客户端编号")
	private String clientNo;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private Integer status;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "采集时间")
	private Date collectDatetime;

	public void setEstimateNumber(Double estimateNumber) {
		this.estimateNumber = (double) Math.round(estimateNumber * 100) / 100;
	}

	/**方量*/
	@Excel(name = "方量", width = 15)
	@ApiModelProperty(value = "方量")
	private Double estimateNumber;
	/**出料时间*/
	@Excel(name = "出料时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "出料时间")
	private Date productDatetime;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
	@ApiModelProperty(value = "坍落度")
	private String slump;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15)
	@ApiModelProperty(value = "超标等级")
	private Integer overLevel;
	/**是否超标*/
	@Excel(name = "是否超标", width = 15)
	@ApiModelProperty(value = "是否超标")
	private Integer alertstate;
	/**配方uuid(车结束符)*/
	@Excel(name = "配方uuid(车结束符)", width = 15)
	@ApiModelProperty(value = "配方uuid(车结束符)")
	private String formulaId;
	/**搅拌时间超标等级*/
	@Excel(name = "搅拌时间超标等级", width = 15)
	@ApiModelProperty(value = "搅拌时间超标等级")
	private Integer timeOverLevel;
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
	@ApiModelProperty(value = "状态")
	private Integer overproofStatus;
	@ApiModelProperty(value = "任务单计算状态")
	private Integer renwudanstatus;
	private Integer jiaozhustatus;//浇筑部位计算状态
	private String chuliaowd;//出料温度
	private String overReason;// 预警原因
	private String orgCode;// 交工组织机构id
	private String orgName;// 交工组织名称
	private String deviceName;// 设备名称

	@ExcelCollection(name="拌合站子表材料信息")
	@ApiModelProperty(value = "拌合站子表材料信息")
	private List<BhzCementDetail> bhzCementDetailList;


}
