package com.trtm.iot.yclud.entity;

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
 * @Description: ycl_usage_detail
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("ycl_usage_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ycl_usage_detail对象", description="ycl_usage_detail")
public class YclUsageDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键id*/
    private Integer id;
    /**配料时间（领料时间）*/
    private Date dosingTime;
    /**配料单号*/
    private String dosingOrderNumber;
    /**工程部位（名称）*/
    private String projectPart;
    /**使用量*/
    private String uses;
    /**料仓id*/
    @Dict(dictTable = "wzliaocang", dicText = "name", dicCode = "guid")
    private String storageId;
    /**检验批编号*/
    private String inspectionLotNumber;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createTime;
    /**组织机构*/
    private String sysOrgCode;
    /**任务单号*/
    private String renwudan;
    /**领用原因*/
    private String lingyongyuanyin;
    /**材料名称*/
    private String storageName;
    /**分部分项id*/
    private String treeid;
    /**分部分项code*/
    private String code;
    /**材料编号*/
    private String cailiaono;
    /**规格型号*/
    private String guigexinghao;
    /**报告*/
    private String bgfile;

    public static YclUsageDetailVO fromYclUsageDetail(YclUsageDetail yclUsageDetail) {
        YclUsageDetailVO vo = new YclUsageDetailVO();
        vo.setId(yclUsageDetail.getId());
        vo.setDosingTime(yclUsageDetail.getDosingTime());
        vo.setDosingOrderNumber(yclUsageDetail.getDosingOrderNumber());
        vo.setProjectPart(yclUsageDetail.getProjectPart());
        vo.setUses(yclUsageDetail.getUses());
        vo.setStorageId(yclUsageDetail.getStorageId());
        vo.setInspectionLotNumber(yclUsageDetail.getInspectionLotNumber());
        vo.setCreateBy(yclUsageDetail.getCreateBy());
        vo.setCreateTime(yclUsageDetail.getCreateTime());
        vo.setSysOrgCode(yclUsageDetail.getSysOrgCode());
        vo.setRenwudan(yclUsageDetail.getRenwudan());
        vo.setLingyongyuanyin(yclUsageDetail.getLingyongyuanyin());
        vo.setStorageName(yclUsageDetail.getStorageName());
        vo.setTreeid(yclUsageDetail.getTreeid());
        vo.setCode(yclUsageDetail.getCode());
        vo.setCailiaono(yclUsageDetail.getCailiaono());
        vo.setGuigexinghao(yclUsageDetail.getGuigexinghao());
        return vo;
    }
}
