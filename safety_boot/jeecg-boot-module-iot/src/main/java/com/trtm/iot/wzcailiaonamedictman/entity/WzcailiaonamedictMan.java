package com.trtm.iot.wzcailiaonamedictman.entity;

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
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Data
@TableName("wzcailiaonamedict_man")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wzcailiaonamedict_man对象", description="wzcailiaonamedict_man")
public class WzcailiaonamedictMan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private java.lang.Integer id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String cailiaoname;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private java.lang.String cailiaono;
	/**parentnode*/
	@Excel(name = "parentnode", width = 15)
    @ApiModelProperty(value = "parentnode")
    private java.lang.String parentnode;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    @Dict(dicCode = "nodeType")
    private java.lang.String nodetype;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**称重*/
	@Excel(name = "称重", width = 15)
    @ApiModelProperty(value = "称重")
    private java.lang.String ischengzhong;
    /**重量*/
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    //对于没有重量的材料,默认为1
    private java.lang.Double weight;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String miaoshu;
	/**偏差*/
	@Excel(name = "偏差", width = 15)
    @ApiModelProperty(value = "偏差")
    private java.lang.String isyunxupiancha;
	/**正偏差*/
	@Excel(name = "正偏差", width = 15)
    @ApiModelProperty(value = "正偏差")
    private java.lang.String zhengpiancha;
	/**负偏差*/
	@Excel(name = "负偏差", width = 15)
    @ApiModelProperty(value = "负偏差")
    private java.lang.String fupiancha;
	/**材料计量单位*/
	@Excel(name = "材料计量单位", width = 15)
    @ApiModelProperty(value = "材料计量单位")
    @Dict(dicCode = "WZCaiLiaoJiLiangDanWei")
    private java.lang.String wzcailiaojiliangdanwei;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    private java.lang.String pinpai;
	/**产地*/
	@Excel(name = "产地", width = 15)
    @ApiModelProperty(value = "产地")
    private java.lang.String chandi;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.lang.String danjia;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**材料字典的唯一id*/
	@Excel(name = "材料字典的唯一id", width = 15)
    @ApiModelProperty(value = "材料字典的唯一id")
    private java.lang.String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private java.lang.Integer ts;
	/**isdel*/
	@Excel(name = "isdel", width = 15)
    @ApiModelProperty(value = "isdel")
    private java.lang.Integer isdel;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;
	/**路面项目 1 原材料  2混合料*/
	@Excel(name = "路面项目 1 原材料  2混合料", width = 15)
    @ApiModelProperty(value = "路面项目 1 原材料  2混合料")
    @Dict(dicCode = "lmcailiaolx")
    private java.lang.Integer lmcailiaolx;
	/**原材料上限*/
	@Excel(name = "原材料上限", width = 15)
    @ApiModelProperty(value = "原材料上限")
    private java.lang.Double shangxian;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**testid*/
	@Excel(name = "testid", width = 15)
    @ApiModelProperty(value = "testid")
    private java.lang.Integer testid;
	/**editperson*/
	@Excel(name = "editperson", width = 15)
    @ApiModelProperty(value = "editperson")
    private java.lang.String editperson;
	/**editdatetime*/
	@Excel(name = "editdatetime", width = 15)
    @ApiModelProperty(value = "editdatetime")
    private java.lang.String editdatetime;
	/**wzcailiaodanweihuansuan*/
	@Excel(name = "wzcailiaodanweihuansuan", width = 15)
    @ApiModelProperty(value = "wzcailiaodanweihuansuan")
    private java.lang.String wzcailiaodanweihuansuan;
	/**是否使用电子锁 0：否，1：是*/
	@Excel(name = "是否使用电子锁 0：否，1：是", width = 15)
    @ApiModelProperty(value = "是否使用电子锁 0：否，1：是")
    @Dict(dicCode = "yn")
    private java.lang.Integer iselocks;
    private java.lang.String allguid;


}
