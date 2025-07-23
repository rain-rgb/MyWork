package com.trtm.iot.rebarWzcailiaonamedictMan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="原材统计分析对象", description="原材统计分析对象")
public class RebarWzcailiaonamedictManVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String cailiaoname;
    @ApiModelProperty(value = "开始时间", required = true, dataType = "String")
    private String startDate;

    @ApiModelProperty(value = "结束时间", required = true, dataType = "String")
    private String endDate;
    /**总数*/
    @Excel(name = "总数", width = 15)
    @ApiModelProperty(value = "总数")
    private Integer allNum;
    /**总重量*/
    @Excel(name = "总重量", width = 15)
    @ApiModelProperty(value = "总重量")
    private Double allWeight;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号")
    private String cailiaono;
	/**parentnode*/
	@Excel(name = "parentnode", width = 15)
    @ApiModelProperty(value = "parentnode")
    private String parentnode;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    private String nodetype;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
    @ApiModelProperty(value = "规格类型")
    private String guigexinghao;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    @ApiModelProperty(value = "分部分项")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCodes;
	/**称重*/
	@Excel(name = "称重", width = 15)
    @ApiModelProperty(value = "称重")
    private String ischengzhong;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String miaoshu;
	/**偏差*/
	@Excel(name = "偏差", width = 15)
    @ApiModelProperty(value = "偏差")
    private String isyunxupiancha;
	/**正偏差*/
	@Excel(name = "正偏差", width = 15)
    @ApiModelProperty(value = "正偏差")
    private String zhengpiancha;
	/**负偏差*/
	@Excel(name = "负偏差", width = 15)
    @ApiModelProperty(value = "负偏差")
    private String fupiancha;
	/**材料计量单位*/
	@Excel(name = "材料计量单位", width = 15)
    @ApiModelProperty(value = "材料计量单位")
    private String wzcailiaojiliangdanwei;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    private String pinpai;
	/**产地*/
	@Excel(name = "产地", width = 15)
    @ApiModelProperty(value = "产地")
    private String chandi;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private String danjia;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**材料字典的唯一id*/
	@Excel(name = "材料字典的唯一id", width = 15)
    @ApiModelProperty(value = "材料字典的唯一id")
    private String guid;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private Integer ts;
	/**isdel*/
	@Excel(name = "isdel", width = 15)
    @ApiModelProperty(value = "isdel")
    private Integer isdel;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
	/**路面项目 1 原材料  2混合料*/
	@Excel(name = "路面项目 1 原材料  2混合料", width = 15)
    @ApiModelProperty(value = "路面项目 1 原材料  2混合料")
    private Integer lmcailiaolx;
	/**原材料上限*/
	@Excel(name = "原材料上限", width = 15)
    @ApiModelProperty(value = "原材料上限")
    private Double shangxian;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**testid*/
	@Excel(name = "testid", width = 15)
    @ApiModelProperty(value = "testid")
    private Integer testid;
	/**editperson*/
	@Excel(name = "editperson", width = 15)
    @ApiModelProperty(value = "editperson")
    private String editperson;
	/**editdatetime*/
	@Excel(name = "editdatetime", width = 15)
    @ApiModelProperty(value = "editdatetime")
    private String editdatetime;
	/**wzcailiaodanweihuansuan*/
	@Excel(name = "wzcailiaodanweihuansuan", width = 15)
    @ApiModelProperty(value = "wzcailiaodanweihuansuan")
    private String wzcailiaodanweihuansuan;
	/**是否使用电子锁 0：否，1：是*/
	@Excel(name = "是否使用电子锁 0：否，1：是", width = 15)
    @ApiModelProperty(value = "是否使用电子锁 0：否，1：是")
    private Integer iselocks;
	/**重量*/
	@Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private Double weight;
	/**过磅类型: 0:无人过磅 1:人工过磅*/
	@Excel(name = "过磅类型: 0:无人过磅 1:人工过磅", width = 15)
    @ApiModelProperty(value = "过磅类型: 0:无人过磅 1:人工过磅")
    private Integer gblx;
	/**材料字典表id*/
	@Excel(name = "材料字典表id", width = 15)
    @ApiModelProperty(value = "材料字典表id")
    private String allguid;
    /**数量*/
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Double number;
}
