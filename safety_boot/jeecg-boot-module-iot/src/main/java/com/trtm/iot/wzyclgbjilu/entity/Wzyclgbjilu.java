package com.trtm.iot.wzyclgbjilu.entity;

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
 * @Description: 物资过磅数据记录表
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
@Data
@TableName("wzyclgbjilu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzyclgbjilu对象", description="物资过磅数据记录表")
public class Wzyclgbjilu implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "进场过磅id")
    private java.lang.Integer id;
	/**进出材料单*/
	@Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
    private java.lang.String jinchuliaodanno;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**出场时间*/
	@Excel(name = "出场时间", width = 15)
    @ApiModelProperty(value = "出场时间")
    private java.lang.String chuchangshijian;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
	/**船号*/
	@Excel(name = "船号", width = 15)
    @ApiModelProperty(value = "船号")
    private java.lang.String cheliangbianhao;
	/**前车牌*/
	@Excel(name = "前车牌", width = 15)
    @ApiModelProperty(value = "前车牌")
    private java.lang.String qianchepai;
	/**后车牌*/
	@Excel(name = "后车牌", width = 15)
    @ApiModelProperty(value = "后车牌")
    private java.lang.String houchepai;
	/**第一次重量*/
	@Excel(name = "第一次重量", width = 15)
    @ApiModelProperty(value = "第一次重量")
    private java.lang.String maozhong;
	/**第二次重量*/
	@Excel(name = "第二次重量", width = 15)
    @ApiModelProperty(value = "第二次重量")
    private java.lang.String pizhong;
	/**净重*/
	@Excel(name = "净重", width = 15)
    @ApiModelProperty(value = "净重")
    private java.lang.String jingzhong;
	/**扣重*/
	@Excel(name = "扣重", width = 15)
    @ApiModelProperty(value = "扣重")
    private java.lang.String kouzhong;
	/**扣率*/
	@Excel(name = "扣率", width = 15)
    @ApiModelProperty(value = "扣率")
    private java.lang.String koulv;
	/**称重偏差*/
	@Excel(name = "称重偏差", width = 15)
    @ApiModelProperty(value = "称重偏差")
    private java.lang.String chengzhongpiancha;
	/**累计*/
	@Excel(name = "累计", width = 15)
    @ApiModelProperty(value = "累计")
    private java.lang.String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private java.lang.String sibangyuan;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private java.lang.String remark;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**供应商单位编码*/
	@Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
	/**过磅类别*/
	@Excel(name = "过磅类别", width = 15)
    @ApiModelProperty(value = "过磅类别")
    private java.lang.String guobangleibie;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @ApiModelProperty(value = "车辆类型")
    private java.lang.String cheliangleixing;
	/**唯一标识*/
	@Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isdel;
	/**0完成第一次称重1第一次称重后已确认2完成第二次称重*/
	@Excel(name = "0完成第一次称重1第一次称重后已确认2完成第二次称重", width = 15)
    @ApiModelProperty(value = "0完成第一次称重1第一次称重后已确认2完成第二次称重")
    private java.lang.String status;
	/**毛重(吨)*/
	@Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private java.lang.String maozhongt;
	/**皮重(吨)*/
	@Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private java.lang.String pizhongt;
	/**candi*/
	@Excel(name = "candi", width = 15)
    @ApiModelProperty(value = "candi")
    private java.lang.String candi;
	/**运输单位*/
	@Excel(name = "运输单位", width = 15)
    @ApiModelProperty(value = "运输单位")
    private java.lang.String yunshudanwei;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    private java.lang.String lcbianhao;
	/**1先秤毛重0先秤皮重*/
	@Excel(name = "1先秤毛重(入库)0先秤皮重(出库)", width = 15)
    @ApiModelProperty(value = "1先秤毛重0先秤皮重")
    @Dict(dicCode = "isshouliao")
    private java.lang.String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
	@Excel(name = "是否统计 默认为0:未统计，1:已统计,15:统计出错", width = 15)
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    private java.lang.Integer istongji;
	/**收货单位*/
	@Excel(name = "收货单位", width = 15)
    @ApiModelProperty(value = "收货单位")
    @Dict(dictTable = "wzshouhuodanwei", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String liaocangid;
	/**serialno*/
	@Excel(name = "serialno", width = 15)
    @ApiModelProperty(value = "serialno")
    private java.lang.String serialno;
	/**reason*/
	@Excel(name = "reason", width = 15)
    @ApiModelProperty(value = "reason")
    private java.lang.String reason;
	/**fileUpload*/
	@Excel(name = "fileUpload", width = 15)
    @ApiModelProperty(value = "fileUpload")
    private java.lang.String fileUpload;
	/**istaizhangtj*/
	@Excel(name = "istaizhangtj", width = 15)
    @ApiModelProperty(value = "istaizhangtj")
    private java.lang.String istaizhangtj;
	/**送货单*/
	@Excel(name = "送货单", width = 15)
    @ApiModelProperty(value = "送货单")
    private java.lang.String songhuodanpic;
	/**是否合格*/
	@Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private java.lang.String shifouhege;
	/**原材描述*/
	@Excel(name = "原材描述", width = 15)
    @ApiModelProperty(value = "原材描述")
    private java.lang.String yuancaimiaoshu;
	/**beizhu*/
	@Excel(name = "beizhu", width = 15)
    @ApiModelProperty(value = "beizhu")
    private java.lang.String beizhu;
	/**jianlipic*/
	@Excel(name = "jianlipic", width = 15)
    @ApiModelProperty(value = "jianlipic")
    private java.lang.String jianlipic;
	/**sibanpic*/
	@Excel(name = "sibanpic", width = 15)
    @ApiModelProperty(value = "sibanpic")
    private java.lang.String sibanpic;
	/**jcgkpic*/
	@Excel(name = "jcgkpic", width = 15)
    @ApiModelProperty(value = "jcgkpic")
    private java.lang.String jcgkpic;
	/**jccppic*/
	@Excel(name = "jccppic", width = 15)
    @ApiModelProperty(value = "jccppic")
    private java.lang.String jccppic;
	/**jchcppic*/
	@Excel(name = "jchcppic", width = 15)
    @ApiModelProperty(value = "jchcppic")
    private java.lang.String jchcppic;
	/**jcbfpic*/
	@Excel(name = "jcbfpic", width = 15)
    @ApiModelProperty(value = "jcbfpic")
    private java.lang.String jcbfpic;
	/**ccgkpic*/
	@Excel(name = "ccgkpic", width = 15)
    @ApiModelProperty(value = "ccgkpic")
    private java.lang.String ccgkpic;
	/**cccppic*/
	@Excel(name = "cccppic", width = 15)
    @ApiModelProperty(value = "cccppic")
    private java.lang.String cccppic;
	/**cchcppic*/
	@Excel(name = "cchcppic", width = 15)
    @ApiModelProperty(value = "cchcppic")
    private java.lang.String cchcppic;
	/**ccbfpic*/
	@Excel(name = "ccbfpic", width = 15)
    @ApiModelProperty(value = "ccbfpic")
    private java.lang.String ccbfpic;
	/**台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常*/
	@Excel(name = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常", width = 15)
    @ApiModelProperty(value = "台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常")
    private java.lang.Integer taizhangtj;
	/**台账id*/
	@Excel(name = "台账id", width = 15)
    @ApiModelProperty(value = "台账id")
    private java.lang.Integer taizhangid;
}
