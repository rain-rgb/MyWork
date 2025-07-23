package com.trtm.iot.rebarWzcailiaonamedictMan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictManVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="0号清单材料对象", description="0号清单材料对象")
public class Number0CaiLiao implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String figureNumber;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String bussId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String appId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String subItemName;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String corrigendumQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double contractQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String imageType;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String unitName;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double designedQuantity;
    @Excel(name = "材料编号", width = 15)
    @ApiModelProperty(value = "材料编号*******")
    private String subItemNumber;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double contractAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String measureChangeQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String shortName;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String projectId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String quantityBillType;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String quantityBillId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String origin;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String erratumAfterQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String recheckAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String designQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String measureChangeAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String measureAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String measureQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String unitId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String designAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double erratumAfterAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String sectionId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double recheckQuantity;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private String createTime;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private Double contractPrice;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String wbsId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String longName;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String isErratum;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String version;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String domainId;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String totalConfirmAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String changeQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String measureRate;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String extension;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String changeAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String totalConfirmQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String isDeleted;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String lockStatus;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String corrigendumAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String childChapterNumber;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String confirmQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String aduitAmount;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String canCalAduit;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String aduitQuantity;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String remarks;
    @Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private String updatePerson;

    @Excel(name = "上传的图片", width = 15)
    @ApiModelProperty(value = "上传的图片")
    private String upload;
    /**分部分项*/
    @ApiModelProperty(value = "分部分项")
    private java.lang.String sysOrgCode;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCodes;
    /**本次使用数量*/
    @ApiModelProperty(value = "本次使用数量*****")
    private java.lang.String total;
    @Excel(name = "已使用数量", width = 15)
    @ApiModelProperty(value = "已使用数量")
    private String usedNumber;
    @Excel(name = "已使用数量", width = 15)
    @ApiModelProperty(value = "已使用数量")
    private List<WzcailiaonamedictManVo> wzcailiaonamedictManVoList;

    /**材料名称*/
    @ApiModelProperty(value = "材料名称")
    private String cailiaoName;
    /**材料编号*/
    @ApiModelProperty(value = "材料编号")
    private String cailiaoNo;
    @ApiModelProperty(value = "规格类型")
    private String guigexinghao;
    /**材料字典的唯一id*/
    @ApiModelProperty(value = "材料字典的唯一id")
    private java.lang.String guid;

}
