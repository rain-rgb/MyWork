package com.trtm.iot.wzyclchuchanggbman.entity;

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
 * @Description: wzyclchuchanggb_man
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
@Data
@TableName("wzyclchuchanggb_man")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzyclchuchanggb_man对象", description="wzyclchuchanggb_man")
public class WzyclchuchanggbMan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "进场过磅id")
    private Integer id;
	/**进出料单号*/
	@Excel(name = "进出料单号", width = 15)
    @ApiModelProperty(value = "进出料单号")
    private String jinchuliaodanno;
	/**材料单号*/
	@Excel(name = "材料单号", width = 15)
    @ApiModelProperty(value = "材料单号")
    private String cailiaono;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private String jinchangshijian;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private String chuchangshijian;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String pici;
	/**车辆编号*/
	@Excel(name = "车辆编号", width = 15)
    @ApiModelProperty(value = "车辆编号")
    private String cheliangbianhao;
	/**前车牌*/
	@Excel(name = "前车牌", width = 15)
    @ApiModelProperty(value = "前车牌")
    private String qianchepai;
	/**后车牌*/
	@Excel(name = "后车牌", width = 15)
    @ApiModelProperty(value = "后车牌")
    private String houchepai;
	/**毛重*/
	@Excel(name = "毛重", width = 15)
    @ApiModelProperty(value = "毛重")
    private String maozhong;
	/**皮重*/
	@Excel(name = "皮重", width = 15)
    @ApiModelProperty(value = "皮重")
    private String pizhong;
	/**净重*/
	@Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private String jingzhong;
	/**扣重*/
	@Excel(name = "扣重", width = 15)
    @ApiModelProperty(value = "扣重")
    private String kouzhong;
	/**扣率*/
	@Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private String koulv;
	/**称重偏差*/
	@Excel(name = "称重偏差", width = 15)
    @ApiModelProperty(value = "称重偏差")
    private String chengzhongpiancha;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    private String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private String sibangyuan;
	/**出场原因*/
	@Excel(name = "出场原因", width = 15)
    @ApiModelProperty(value = "出场原因")
    private String remark;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String shebeibianhao;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String gongyingshangdanweibianma;
	/**过磅类别*/
	@Excel(name = "过磅类别", width = 15)
    @ApiModelProperty(value = "过磅类别")
    private String guobangleibie;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @ApiModelProperty(value = "车辆类型")
    private String cheliangleixing;
	/**STATUS*/
	@Excel(name = "STATUS", width = 15)
    @ApiModelProperty(value = "STATUS")
    private String status;
	/**ISDEL*/
	@Excel(name = "ISDEL", width = 15)
    @ApiModelProperty(value = "ISDEL")
    private String isdel;
	/**TS*/
	@Excel(name = "TS", width = 15)
    @ApiModelProperty(value = "TS")
    private Integer ts;
	/**GUID*/
	@Excel(name = "GUID", width = 15)
    @ApiModelProperty(value = "GUID")
    private String guid;
	/**毛重(吨)*/
	@Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private String maozhongt;
	/**皮重(吨)*/
	@Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private String pizhongt;
	/**candi*/
	@Excel(name = "candi", width = 15)
    @ApiModelProperty(value = "candi")
    private String candi;
	/**运输单位*/
	@Excel(name = "运输单位", width = 15)
    @ApiModelProperty(value = "运输单位")
    private String yunshudanwei;
	/**料仓*/
	@Excel(name = "料仓", width = 15)
    @ApiModelProperty(value = "料仓")
    private String lcbianhao;
	/**ISSHOULIAO*/
	@Excel(name = "ISSHOULIAO", width = 15)
    @ApiModelProperty(value = "ISSHOULIAO")
    private String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private String jingzhongt;
	/**ISTONGJI*/
	@Excel(name = "ISTONGJI", width = 15)
    @ApiModelProperty(value = "ISTONGJI")
    private Integer istongji;
	/**进场高空*/
	@Excel(name = "进场高空", width = 15)
    @ApiModelProperty(value = "进场高空")
    private String jcgkpic;
	/**进场车牌1*/
	@Excel(name = "进场车牌1", width = 15)
    @ApiModelProperty(value = "进场车牌1")
    private String jccppic;
	/**进场车牌2*/
	@Excel(name = "进场车牌2", width = 15)
    @ApiModelProperty(value = "进场车牌2")
    private String jchcppic;
	/**进场磅房*/
	@Excel(name = "进场磅房", width = 15)
    @ApiModelProperty(value = "进场磅房")
    private String jcbfpic;
	/**出场高空*/
	@Excel(name = "出场高空", width = 15)
    @ApiModelProperty(value = "出场高空")
    private String ccgkpic;
	/**出场车牌1*/
	@Excel(name = "出场车牌1", width = 15)
    @ApiModelProperty(value = "出场车牌1")
    private String cccppic;
	/**出场车牌2*/
	@Excel(name = "出场车牌2", width = 15)
    @ApiModelProperty(value = "出场车牌2")
    private String cchcppic;
	/**出场磅房*/
	@Excel(name = "出场磅房", width = 15)
    @ApiModelProperty(value = "出场磅房")
    private String ccbfpic;
	/**料仓id*/
	@Excel(name = "料仓id", width = 15)
    @ApiModelProperty(value = "料仓id")
    private String liaocangid;
	/**SERIALNO*/
	@Excel(name = "SERIALNO", width = 15)
    @ApiModelProperty(value = "SERIALNO")
    private String serialno;
	/**原因*/
	@Excel(name = "原因", width = 15)
    @ApiModelProperty(value = "原因")
    private String reason;
	/**文件上传*/
	@Excel(name = "文件上传", width = 15)
    @ApiModelProperty(value = "文件上传")
    private String fileUpload;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private String beizhu;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private String projectid;
	/**施工地点*/
	@Excel(name = "施工地点", width = 15)
    @ApiModelProperty(value = "施工地点")
    private String constructionsite;
	/**台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常*/
	@Excel(name = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常", width = 15)
    @ApiModelProperty(value = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常")
    private Integer taizhangtj;
	/**台账id*/
	@Excel(name = "台账id", width = 15)
    @ApiModelProperty(value = "台账id")
    private Integer taizhangid;
	/**istaizhangtj*/
	@Excel(name = "istaizhangtj", width = 15)
    @ApiModelProperty(value = "istaizhangtj")
    private String istaizhangtj;
	/**送货单*/
	@Excel(name = "送货单", width = 15)
    @ApiModelProperty(value = "送货单")
    private String songhuodanpic;
	/**是否合格*/
	@Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private String shifouhege;
	/**原材描述*/
	@Excel(name = "原材描述", width = 15)
    @ApiModelProperty(value = "原材描述")
    private String yuancaimiaoshu;
	/**jianlipic*/
	@Excel(name = "jianlipic", width = 15)
    @ApiModelProperty(value = "jianlipic")
    private String jianlipic;
	/**sibanpic*/
	@Excel(name = "sibanpic", width = 15)
    @ApiModelProperty(value = "sibanpic")
    private String sibanpic;
}
