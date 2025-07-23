package com.trtm.iot.rebarWzcailiaonamedictMan.entity;

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
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
@Data
@TableName("rebar_wzcailiaonamedict_man")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rebar_wzcailiaonamedict_man对象", description="rebar_wzcailiaonamedict_man")
public class RebarWzcailiaonamedictMan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private java.lang.String id;
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
    private java.lang.String nodetype;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private java.lang.String guigexinghao;
	/**分部分项*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**所属部门*/
    @ApiModelProperty(value = "分部分项")
    private java.lang.String sysOrgCodes;
	/**称重*/
	@Excel(name = "称重", width = 15)
    @ApiModelProperty(value = "称重")
    private java.lang.String ischengzhong;
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
    private java.lang.Integer lmcailiaolx;
	/**原材料上限*/
	@Excel(name = "原材料上限", width = 15)
    @ApiModelProperty(value = "原材料上限")
    private java.lang.Double shangxian;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
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
    private java.lang.Integer iselocks;
	/**重量*/
	@Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.lang.Double weight;
	/**过磅类型: 0:无人过磅 1:人工过磅*/
	@Excel(name = "过磅类型: 0:无人过磅 1:人工过磅", width = 15)
    @ApiModelProperty(value = "过磅类型: 0:无人过磅 1:人工过磅")
    private java.lang.Integer gblx;
	/**材料字典表id*/
	@Excel(name = "材料字典表id", width = 15)
    @ApiModelProperty(value = "材料字典表id")
    private java.lang.String allguid;
    /**数量*/
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.lang.Double number;
    @Excel(name = "上传的图片", width = 15)
    @ApiModelProperty(value = "上传的图片")
    private java.lang.String image;
}
