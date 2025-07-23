package com.trtm.iot.bhzSwRecipe.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
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
import java.util.List;

/**
 * @Description: 水稳理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_sw_recipe对象", description="水稳理论配合比主表")
public class BhzSwRecipePage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**llysb*/
	@Excel(name = "llysb", width = 15)
    @ApiModelProperty(value = "llysb")
    private String llysb;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeibianhao;
	/**配比名称*/
	@Excel(name = "配比名称", width = 15)
    @ApiModelProperty(value = "配比名称")
    @Dict(dicCode = "lilunname")
    private String lilunname;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    @Dict(dicCode = "llbuwei")
    private String llbuwei;
	/**录入时间*/
	@Excel(name = "录入时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "录入时间")
    private Date llshijian;
	/**默认*/
	@Excel(name = "默认", width = 15)
    @ApiModelProperty(value = "默认")
    private Integer llmoren;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String llbeizhu;
	/**llkd*/
	@Excel(name = "llkd", width = 15)
    @ApiModelProperty(value = "llkd")
    private String llkd;
	/**llhd*/
	@Excel(name = "llhd", width = 15)
    @ApiModelProperty(value = "llhd")
    private String llhd;
	/**llmd*/
	@Excel(name = "llmd", width = 15)
    @ApiModelProperty(value = "llmd")
    private String llmd;
	/**混合料类型*/
	@Excel(name = "混合料类型", width = 15)
    @ApiModelProperty(value = "混合料类型")
    @Dict(dicCode = "hhllx")
    private String hhllx;
	/**生产配合比审批状态*/
	@Excel(name = "生产配合比审批状态", width = 15)
    @ApiModelProperty(value = "生产配合比审批状态")
    private String phbsp;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
	/**配合比编号*/
	@Excel(name = "配合比编号", width = 15)
    @ApiModelProperty(value = "配合比编号")
    private String phbid;
	/**组织机构id*/
	@Excel(name = "组织机构id", width = 15)
    @ApiModelProperty(value = "组织机构id")
    private String departid;
	/**唯一id*/
	@Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String zhuziid;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    private String isdel;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private String ts;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private String projectid;

    private List<BhzSwPhbZibiao> bhzSwPhbZibiaoList;
}
