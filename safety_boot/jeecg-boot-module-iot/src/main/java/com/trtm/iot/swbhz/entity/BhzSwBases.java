package com.trtm.iot.swbhz.entity;

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
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@ApiModel(value="bhz_sw_bases对象", description="水稳主表")
@Data
@TableName("bhz_sw_bases")
public class BhzSwBases implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**混合料编号*/
	@Excel(name = "混合料编号", width = 15)
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
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeibianhao;
	/**客户端编号*/
	@Excel(name = "客户端编号", width = 15)
    @ApiModelProperty(value = "客户端编号")
    private java.lang.String clientNo;
	/**保存时间*/
	@Excel(name = "保存时间", width = 15)
    @ApiModelProperty(value = "保存时间")
    private java.lang.String baocunshijian;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "采集时间")
    private java.util.Date caijishijian;
	/**拌合时间*/
	@Excel(name = "拌合时间", width = 15)
    @ApiModelProperty(value = "拌合时间")
    private java.lang.Integer banheshijian;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15, dicCode = "over_level")
    @Dict(dicCode = "over_level")
    @ApiModelProperty(value = "超标等级")
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
	/**材料超标*/
	@Excel(name = "材料超标", width = 15)
    @ApiModelProperty(value = "材料超标")
    private java.lang.Integer cailiaochaobiao;
	/**是否判断超标*/
	@Excel(name = "是否判断超标", width = 15)
    @ApiModelProperty(value = "是否判断超标")
    private java.lang.Integer alertstate;
	/**配方号*/
	@Excel(name = "配方号", width = 15)
    @ApiModelProperty(value = "配方号")
    private java.lang.String formulaNo;
	/**单位(kg/t)*/
	@Excel(name = "单位(kg/t)", width = 15)
    @ApiModelProperty(value = "单位(kg/t)")
    private java.lang.String beiy1;
	/**备用2*/
	@Excel(name = "备用2", width = 15)
    @ApiModelProperty(value = "备用2")
    private java.lang.String beiy2;
	/**备用3*/
	@Excel(name = "备用3", width = 15)
    @ApiModelProperty(value = "备用3")
    private java.lang.String beiy3;
	/**权限控制*/
    @ApiModelProperty(value = "权限控制")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    @ApiModelProperty(value = "状态")
    private Integer overproofStatus;
    private Integer iszlpz;
    private String overReason;// 预警原因
    private String jtjtuuid;// 预警原因
    private java.lang.Integer issend;
}
