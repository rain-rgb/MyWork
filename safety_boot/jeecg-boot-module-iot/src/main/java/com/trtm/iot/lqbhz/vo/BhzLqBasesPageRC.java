package com.trtm.iot.lqbhz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_lq_basesPage对象", description="沥青主表")
public class BhzLqBasesPageRC {

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
	/**沥青温度*/
	@Excel(name = "沥青温度", width = 15)
	@ApiModelProperty(value = "沥青温度")
	private String liqingwd;
	/**出料温度*/
	@Excel(name = "出料温度", width = 15)
	@ApiModelProperty(value = "出料温度")
	private String chuliaowd;
	/**骨料温度*/
	@Excel(name = "骨料温度", width = 15)
	@ApiModelProperty(value = "骨料温度")
	private String guliaowd;
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
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
	@ApiModelProperty(value = "材料编号")
	private String cailiaobianhao;
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
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
	@ApiModelProperty(value = "材料类型")
	private String cailiaoleixing;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@ApiModelProperty(value = "是否删除")
	private String isdel;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15)
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
	/**组织结构id*/
	@Excel(name = "组织结构id", width = 15)
	@ApiModelProperty(value = "组织结构id")
	private String departid;
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
	/**温度超标*/
	@Excel(name = "温度超标", width = 15)
	@ApiModelProperty(value = "温度超标")
	private Integer wenduchaobiao;
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
	/**油石比*/
	@Excel(name = "油石比", width = 15)
	@ApiModelProperty(value = "油石比")
	private String youshibi;
	/**理论油石比*/
	@Excel(name = "理论油石比", width = 15)
	@ApiModelProperty(value = "理论油石比")
	private String llysb;
	/**筛分实验统计*/
	@Excel(name = "筛分实验统计", width = 15)
	@ApiModelProperty(value = "筛分实验统计")
	private Integer jipeitongji;
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
	/**权限*/
	@ApiModelProperty(value = "权限")
	private String sysOrgCode;

	@ExcelCollection(name="沥青子表信息")
	@ApiModelProperty(value = "拌合站子表材料信息")
	private List<BhzCementDetail> bhzCementDetailList;
	@ApiModelProperty(value = "拌合站处置信息表")
	private BhzCementOverHandler bhzCementOverHandler;
	private Integer overproofStatus;
	private String overReason;// 预警原因

	private String overlevelName;
	private String overproofStatusName;
}
