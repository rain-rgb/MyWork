package com.trtm.sy.syztgl.entity;

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
 * @Description: 试验单位管理
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Data
@TableName("sy_danweiguanli")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_danweiguanli对象", description="试验单位管理")
public class SyDanweiguanli implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**单位编码*/
	@Excel(name = "单位编码", width = 15)
    @ApiModelProperty(value = "单位编码")
    private java.lang.String dwbm;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private java.lang.String dwname;
	/**单位类型（施工单位；监理单位；指挥部；实验室；外委单位；辅助员）*/
	@Excel(name = "单位类型（施工单位；监理单位；指挥部；实验室；外委单位；辅助员）", width = 15)
    @ApiModelProperty(value = "单位类型（施工单位；监理单位；指挥部；实验室；外委单位；辅助员）")
    @Dict(dicCode = "dwtype")
    private java.lang.Integer dwtype;
	/**自治证书编码*/
	@Excel(name = "自治证书编码", width = 15)
    @ApiModelProperty(value = "自治证书编码")
    private java.lang.String zzzs;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String file;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysorgcode;
	/**上级部门*/
	@Excel(name = "上级部门", width = 15)
    @ApiModelProperty(value = "上级部门")
    private java.lang.String syssjcode;
	/**登记人*/
	@Excel(name = "登记人", width = 15)
    @ApiModelProperty(value = "登记人")
    private java.lang.String dnegjiren;
	/**登记时间*/
	@Excel(name = "登记时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "登记时间")
    private java.util.Date dengjitime;
}
