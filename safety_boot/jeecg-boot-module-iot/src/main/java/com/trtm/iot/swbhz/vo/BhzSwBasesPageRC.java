package com.trtm.iot.swbhz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
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
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_sw_basesPage对象", description="水稳主表")
public class BhzSwBasesPageRC {

	/**id*/
	@ApiModelProperty(value = "id")
	private Integer id;
	/**混合料编号*/
	@Excel(name = "混合料编号", width = 15)
	@ApiModelProperty(value = "混合料编号")
	private String hunheliaoid;
	/**出料时间*/
	@Excel(name = "出料时间", width = 15)
	@ApiModelProperty(value = "出料时间")
	private String chuliaoshijian;
	/**用户*/
	@Excel(name = "用户", width = 15)
	@ApiModelProperty(value = "用户")
	private String yonghu;
	/**总产量*/
	@Excel(name = "总产量", width = 15)
	@ApiModelProperty(value = "总产量")
	private Double zongchanliang;
	/**累计产量*/
	@Excel(name = "累计产量", width = 15)
	@ApiModelProperty(value = "累计产量")
	private Double leijichanliang;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
	@ApiModelProperty(value = "设备编号")
	private String shebeibianhao;
	/**客户端编号*/
	@Excel(name = "客户端编号", width = 15)
	@ApiModelProperty(value = "客户端编号")
	private String clientNo;
	/**保存时间*/
	@Excel(name = "保存时间", width = 15)
	@ApiModelProperty(value = "保存时间")
	private String baocunshijian;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "采集时间")
	private Date caijishijian;
	/**拌合时间*/
	@Excel(name = "拌合时间", width = 15)
	@ApiModelProperty(value = "拌合时间")
	private Integer banheshijian;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@ApiModelProperty(value = "是否删除")
	private String isdel;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15, dicCode = "over_level")
    @Dict(dicCode = "over_level")
	@ApiModelProperty(value = "超标等级")
	private Integer chaobiaodengji;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
	@ApiModelProperty(value = "施工部位")
	private String poureLocation;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
	@ApiModelProperty(value = "施工地点")
	private String jobLocation;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
	@ApiModelProperty(value = "时间戳")
	private String ts;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
	@ApiModelProperty(value = "唯一标识")
	private String guid;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
	@ApiModelProperty(value = "强度等级")
	private String strengthRank;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
	@ApiModelProperty(value = "工程名称")
	private String projectName;
	/**是否统计*/
	@Excel(name = "是否统计", width = 15)
	@ApiModelProperty(value = "是否统计")
	private Integer istongji;
	/**时间超标*/
	@Excel(name = "时间超标", width = 15)
	@ApiModelProperty(value = "时间超标")
	private Integer timechaobiao;
	/**材料超标*/
	@Excel(name = "材料超标", width = 15)
	@ApiModelProperty(value = "材料超标")
	private Integer cailiaochaobiao;
	/**是否判断超标*/
	@Excel(name = "是否判断超标", width = 15)
	@ApiModelProperty(value = "是否判断超标")
	private Integer alertstate;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
	@ApiModelProperty(value = "配方号")
	private String formulaNo;
	/**备用1*/
	@Excel(name = "备用1", width = 15)
	@ApiModelProperty(value = "备用1")
	private String beiy1;
	/**备用2*/
	@Excel(name = "备用2", width = 15)
	@ApiModelProperty(value = "备用2")
	private String beiy2;
	/**备用3*/
	@Excel(name = "备用3", width = 15)
	@ApiModelProperty(value = "备用3")
	private String beiy3;
	/**权限控制*/
	@ApiModelProperty(value = "权限控制")
	private String sysOrgCode;
	@ApiModelProperty(value = "状态")
	private Integer overproofStatus;

	@ExcelCollection(name="水稳材料表信息")
	@ApiModelProperty(value = "拌合站子表材料信息")
	private List<BhzCementDetail> bhzCementDetailList;
	@ApiModelProperty(value = "拌合站处置信息表")
	private BhzCementOverHandler bhzCementOverHandler;


	private String overlevelName;
	private String overproofStatusName;
}
