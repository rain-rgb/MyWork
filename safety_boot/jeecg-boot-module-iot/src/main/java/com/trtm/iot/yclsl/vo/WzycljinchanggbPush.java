package com.trtm.iot.yclsl.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzycljinchanggb对象", description="原材料收料主表")
public class WzycljinchanggbPush implements Serializable {
    private static final long serialVersionUID = 1L;

	/**进场过磅id*/
	@TableId(type = IdType.AUTO)
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "进场过磅id")
    private Integer id;
	/**进出材料单*/
	@Excel(name = "进出材料单", width = 15)
    @ApiModelProperty(value = "进出材料单")
    private String jinchuliaodanno;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "wzcailiaonamedict", dicText = "cailiaoname", dicCode = "cailiaono")
    private String cailiaono;
    private String cailiaono_dictText;
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
	/**船号*/
    @ApiModelProperty(value = "船号")
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
	/**累计*/
	@Excel(name = "累计", width = 15)
    @ApiModelProperty(value = "累计")
    private String liaocang;
	/**司磅员*/
	@Excel(name = "司磅员", width = 15)
    @ApiModelProperty(value = "司磅员")
    private String sibangyuan;
	/**remark*/
    @ApiModelProperty(value = "remark")
    private String remark;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeibianhao;
	/**供应商单位编码*/
	@Excel(name = "供应商单位编码", width = 15)
    @ApiModelProperty(value = "供应商单位编码")
    @Dict(dictTable = "wzgongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private String gongyingshangdanweibianma;
    private String gongyingshangdanweibianma_dictText;
	/**过磅类别*/
    @ApiModelProperty(value = "过磅类别")
    private String guobangleibie;
	/**车辆类型*/
    @ApiModelProperty(value = "车辆类型")
    private String cheliangleixing;
	/**唯一标识*/
    @ApiModelProperty(value = "唯一标识")
    private String guid;
	/**时间戳*/
    @ApiModelProperty(value = "时间戳")
    private Integer ts;
	/**是否删除*/
    @ApiModelProperty(value = "是否删除")
    @TableLogic(value = "0", delval = "1")
    private String isdel;
	/**status*/
    @ApiModelProperty(value = "status")
    private String status;
	/**毛重(吨)*/
	@Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private String maozhongt;
	/**皮重(吨)*/
	@Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private String pizhongt;
	/**candi*/
    @ApiModelProperty(value = "candi")
    private String candi;
	/**运输单位*/
    @ApiModelProperty(value = "运输单位")
    private String yunshudanwei;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String lcbianhao;
    private String lcbianhao_dictText;
	/**isshouliao*/
    @ApiModelProperty(value = "isshouliao")
    private String isshouliao;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private String jingzhongt;
	/**是否统计 默认为0:未统计，1:已统计,15:统计出错*/
    @Dict(dicCode = "istongji")
    @ApiModelProperty(value = "是否统计 默认为0:未统计，1:已统计,15:统计出错")
    private Integer istongji;
	/**jcgkpic*/
    @ApiModelProperty(value = "jcgkpic")
    private String jcgkpic;
	/**jccppic*/
    @ApiModelProperty(value = "jccppic")
    private String jccppic;
	/**jchcppic*/
    @ApiModelProperty(value = "jchcppic")
    private String jchcppic;
	/**jcbfpic*/
    @ApiModelProperty(value = "jcbfpic")
    private String jcbfpic;
	/**ccgkpic*/
    @ApiModelProperty(value = "ccgkpic")
    private String ccgkpic;
	/**cccppic*/
    @ApiModelProperty(value = "cccppic")
    private String cccppic;
	/**cchcppic*/
    @ApiModelProperty(value = "cchcppic")
    private String cchcppic;
	/**ccbfpic*/
    @ApiModelProperty(value = "ccbfpic")
    private String ccbfpic;
	/**卸货地点*/
    @ApiModelProperty(value = "卸货地点")
    private String liaocangid;
	/**serialno*/
    @ApiModelProperty(value = "serialno")
    private String serialno;
	/**reason*/
    @ApiModelProperty(value = "reason")
    private String reason;
	/**fileUpload*/
    @ApiModelProperty(value = "fileUpload")
    private String fileUpload;
	/**istaizhangtj*/
    @ApiModelProperty(value = "istaizhangtj")
    private String istaizhangtj;
	/**送货单*/
    @ApiModelProperty(value = "送货单")
    private String songhuodanpic;
	/**是否合格*/
    @ApiModelProperty(value = "是否合格")
    private String shifouhege;
	/**原材描述*/
    @ApiModelProperty(value = "原材描述")
    private String yuancaimiaoshu;
	/**beizhu*/
    @ApiModelProperty(value = "beizhu")
    private String beizhu;
	/**jianlipic*/
    @ApiModelProperty(value = "jianlipic")
    private String jianlipic;
	/**sibanpic*/
    @ApiModelProperty(value = "sibanpic")
    private String sibanpic;
    /**台账是否统计*/
    @ApiModelProperty(value = "台账是否统计")
    @Dict(dicCode = "taizhangtj")
    private Integer taizhangtj;
    /**台账id*/
    @ApiModelProperty(value = "台账id")
    private Integer taizhangid;
    /**净重T统计*/
    @ApiModelProperty(value = "净重T统计")
    private Integer jingzhongttj;
}
