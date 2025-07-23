package com.trtm.iot.wztaizhang.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date:   2021-06-18
 * @Version: V1.0
 */
@Data
@TableName("wztaizhang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wztaizhang对象", description="wztaizhang")
public class Wztaizhang implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**进场时间*/
	@Excel(name = "进场时间", width = 15)
    @ApiModelProperty(value = "进场时间")
    private java.lang.String jinchangshijian;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeibianhao;
	/**料仓编号*/
	@Excel(name = "料仓编号", width = 15)
    @ApiModelProperty(value = "料仓编号")
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private java.lang.String lcbianhao;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    @Dict(dictTable = "ycl_cailiaodict", dicText = "cailiaoname", dicCode = "cailiaono")
    private java.lang.String cailiaono;
	/**毛重(吨)*/
	@Excel(name = "毛重(吨)", width = 15)
    @ApiModelProperty(value = "毛重(吨)")
    private java.lang.String maozhongt;
	/**皮重(吨)*/
	@Excel(name = "皮重(吨)", width = 15)
    @ApiModelProperty(value = "皮重(吨)")
    private java.lang.String pizhongt;
	/**净重(吨)*/
	@Excel(name = "净重(吨)", width = 15)
    @ApiModelProperty(value = "净重(吨)")
    private java.lang.String jingzhongt;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private java.lang.String pici;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**检验状态*/
    @Excel(name = "检验状态", width = 15)
    @ApiModelProperty(value = "检验状态")
    @Dict(dicCode = "jianyanstate")
    private java.lang.Integer jianyanstate;
    /**抽检状态*/
    @Excel(name = "抽检状态", width = 15)
    @ApiModelProperty(value = "抽检状态")
    @Dict(dicCode = "choujianstate")
    private java.lang.Integer choujianstate;
    /**供应商*/
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "ycl_gongyingshang", dicText = "gongyingshangname", dicCode = "guid")
    private java.lang.String gongyingshangdanweibianma;
    /**形成检验批状态*/
    @Excel(name = "形成检验批状态", width = 15)
    @ApiModelProperty(value = "形成检验批状态")
    private java.lang.Integer isfinish;
    /**是否已取样*/
    @Excel(name = "是否已取样", width = 15)
    @ApiModelProperty(value = "是否已取样")
    private java.lang.Integer isquyang;
    /**报告附件*/
    @Excel(name = "报告附件", width = 15)
    @ApiModelProperty(value = "报告附件")
    private java.lang.String baogaofile;
    /**数据推送义东高速状态（0 未推送 1已推送 2 推送失败）*/
    @Excel(name = "数据推送义东高速状态（0 未推送 1已推送 2 推送失败）", width = 15)
    @ApiModelProperty(value = "数据推送义东高速状态（0 未推送 1已推送 2 推送失败）")
    private java.lang.Integer yidongstatus;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
    /**闭合状态*/
    @ApiModelProperty(value = "闭合状态(0:未处置,10:待审核,20已闭合)")
    private java.lang.Integer overproofStatus;
    /**存放地点*/
    @Excel(name = "存放地点", width = 15)
    @ApiModelProperty(value = "存放地点")
    private java.lang.String storagePlace;
    /**使用部位*/
    @Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private java.lang.String usePart;
    /**委托状态（0：未委托，1：已委托）*/
    @Excel(name = "委托状态（0：未委托，1：已委托）", width = 15)
    @ApiModelProperty(value = "委托状态（0：未委托，1：已委托）")
    private java.lang.Integer delegateState;
    /**过磅类型: 0:无人过磅 1:人工过磅*/
    @Excel(name = "过磅类型: 0:无人过磅 1:人工过磅", width = 15)
    @ApiModelProperty(value = "过磅类型: 0:无人过磅 1:人工过磅")
    private java.lang.Integer gblx;
    /**计算规则方式 0：根据地磅等计算 1：根据项目等计算*/
    @Excel(name = "计算规则方式 0：根据地磅等计算 1：根据项目等计算", width = 15)
    @ApiModelProperty(value = "计算规则方式 0：根据地磅等计算 1：根据项目等计算")
    private java.lang.Integer ruleway;
    /**0:未处置；10:待审核，20已闭合*/
    /**检验批数据推送（0 未推送 1已推送 2 推送失败）*/
    @Excel(name = "检验批数据推送（0 未推送 1已推送 2 推送失败）", width = 15)
    @ApiModelProperty(value = "检验批数据推送（0 未推送 1已推送 40 推送失败）")
    @Dict(dicCode = "taizhangtj")
    private java.lang.Integer jystatus;
    /**出厂时间*/
    @Excel(name = "出厂时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出厂时间")
    private java.util.Date chuchangshijian;
    /**质保单*/
    @Excel(name = "质保单", width = 15)
    @ApiModelProperty(value = "质保单")
    private java.lang.String zhibaodan;
    @Dict(dicCode = "nodeType")
    private java.lang.String nodetype;
    private java.lang.Integer quyangzhuangtai;
    private java.lang.String danwei;
    private java.lang.String sampleNo;
    private java.lang.String titCode;
    private java.lang.Integer waiweizhuangtai;
    /**标段是否自检（0：未自检，1：已自检）*/
    @Excel(name = "标段是否自检（0：未自检，1：已自检）", width = 15)
    @ApiModelProperty(value = "标段是否自检（0：未自检，1：已自检）")
    private java.lang.Integer bdzj;
    @TableField(exist = false)
    private java.lang.String zjcsl;
    // 质保单号
    private java.lang.String shengchanpihao;
    private java.lang.Double usenum;
    @Dict(dicCode = "zgzt")
    private java.lang.String zgzt;
    private java.lang.Integer iszlpz;

    public String getZjcsl() {
        if (null == this.danwei) {
            return this.jingzhongt;
        } else {
            return this.jingzhongt + this.danwei;
        }
    }
}
