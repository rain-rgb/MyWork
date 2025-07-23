package com.trtm.iot.lqbhz.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@ApiModel(value="bhz_lq_bases对象", description="沥青主表")
@Data
@TableName("bhz_lq_bases")
public class BhzLqBases implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**混合料编号*/
	@Excel(name = "混合料编号", width = 15)
    @Dict(dicCode = "hhllx")
    @ApiModelProperty(value = "混合料编号")
    private java.lang.String hunheliaoid;
	/**出料时间*/
	@Excel(name = "出料时间", width = 15)
    @ApiModelProperty(value = "出料时间")
    private java.lang.String chuliaoshijian;
	/**用户*/
	@Excel(name = "用户", width = 15)
    @ApiModelProperty(value = "用户")
    private java.lang.String yonghu;
	/**沥青温度*/
	@Excel(name = "沥青温度", width = 15)
    @ApiModelProperty(value = "沥青温度")
    private java.lang.String liqingwd;
	/**出料温度*/
	@Excel(name = "出料温度", width = 15)
    @ApiModelProperty(value = "出料温度")
    private java.lang.String chuliaowd;
	/**骨料温度*/
	@Excel(name = "骨料温度", width = 15)
    @ApiModelProperty(value = "骨料温度")
    private java.lang.String guliaowd;
	/**总产量*/
	@Excel(name = "总产量", width = 15)
    @ApiModelProperty(value = "总产量")
    private java.lang.Double zongchanliang;
	/**累计产量*/
	@Excel(name = "累计产量", width = 15)
    @ApiModelProperty(value = "累计产量")
    private java.lang.Double leijichanliang;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**客户端编号*/
	@Excel(name = "客户端编号", width = 15)
    @ApiModelProperty(value = "客户端编号")
    private java.lang.String clientNo;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String cailiaobianhao;
	/**保存时间*/
	@Excel(name = "保存时间", width = 15)
    @ApiModelProperty(value = "保存时间")
    private java.lang.String baocunshijian;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
    private java.util.Date caijishijian;
	/**拌合时间*/
	@Excel(name = "拌合时间", width = 15)
    @ApiModelProperty(value = "拌合时间")
    private java.lang.Integer banheshijian;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private java.lang.String cailiaoleixing;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15)
    @ApiModelProperty(value = "超标等级")
    @Dict(dicCode = "over_level")
    private java.lang.Integer chaobiaodengji;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String poureLocation;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
    @ApiModelProperty(value = "施工地点")
    private java.lang.String jobLocation;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.String ts;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private java.lang.String strengthRank;
	/**组织结构id*/
	@Excel(name = "组织结构id", width = 15)
    @ApiModelProperty(value = "组织结构id")
    private java.lang.String departid;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private java.lang.String projectName;
	/**是否统计*/
	@Excel(name = "是否统计", width = 15)
    @ApiModelProperty(value = "是否统计")
    private java.lang.Integer istongji;
	/**时间超标*/
	@Excel(name = "时间超标", width = 15)
    @ApiModelProperty(value = "时间超标")
    private java.lang.Integer timechaobiao;
	/**温度超标*/
	@Excel(name = "温度超标", width = 15)
    @ApiModelProperty(value = "温度超标")
    private java.lang.Integer wenduchaobiao;
	/**材料超标*/
	@Excel(name = "材料超标", width = 15)
    @ApiModelProperty(value = "材料超标")
    private java.lang.Integer cailiaochaobiao;
	/**是否判断超标*/
	@Excel(name =    "是否判断超标", width = 15)
    @ApiModelProperty(value = "是否判断超标")
    private java.lang.Integer alertstate;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
    @ApiModelProperty(value = "配方号")
    private java.lang.String formulaNo;
	/**油石比*/
	@Excel(name = "油石比", width = 15)
    @ApiModelProperty(value = "油石比")
    private java.lang.String youshibi;
	/**理论油石比*/
	@Excel(name = "理论油石比", width = 15)
    @ApiModelProperty(value = "理论油石比")
    private java.lang.String llysb;
	/**筛分实验统计*/
	@Excel(name = "筛分实验统计", width = 15)
    @ApiModelProperty(value = "筛分实验统计")
    private java.lang.Integer jipeitongji;
	/**备用1*/
	@Excel(name = "备用1", width = 15)
    @ApiModelProperty(value = "备用1")
    private java.lang.String beiy1;
	/**备用2*/
	@Excel(name = "备用2", width = 15)
    @ApiModelProperty(value = "备用2")
    private java.lang.String beiy2;
	/**备用3*/
	@Excel(name = "备用3", width = 15)
    @ApiModelProperty(value = "备用3")
    private java.lang.String beiy3;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    @ApiModelProperty(value = "状态")
    private Integer overproofStatus;
    private String overReason;// 预警原因
    private Integer iszlpz;
    private String jtjtuuid;//交投集团

}
