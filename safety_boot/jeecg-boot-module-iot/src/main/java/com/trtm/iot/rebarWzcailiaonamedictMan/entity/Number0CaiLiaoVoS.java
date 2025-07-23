package com.trtm.iot.rebarWzcailiaonamedictMan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
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
@ApiModel(value="0号清单添加材料对象", description="0号清单材料对象")
public class Number0CaiLiaoVoS implements Serializable {
    private static final long serialVersionUID = 1L;

    /**0号清单材料集合*/
    @Excel(name = "0号清单材料集合", width = 15)
    @ApiModelProperty(value = "0号清单材料集合")
    private List<Number0CaiLiao> number0CaiLiaoList;

    @ApiModelProperty(value = "所属部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    @ApiModelProperty(value = "分部分项")
    @Dict(dictTable = "sys_depart_project", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCodes;

}
