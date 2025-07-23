package com.trtm.iot.ycltesttaizhang.entity;

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
 * @Description: ycl_test_taizhang
 * @Author: jeecg-boot
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Data
@TableName("ycl_test_taizhang")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ycl_test_taizhang对象", description="ycl_test_taizhang")
public class YclTestTaizhang implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String cailiaoname;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String guige;
	/**材料 nodetype*/
	@Excel(name = "材料 nodetype", width = 15)
    @ApiModelProperty(value = "材料 nodetype")
    @Dict(dicCode = "nodeType")
    private java.lang.String nodetype;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String gongyingshang;
	/**生产批号*/
	@Excel(name = "生产批号", width = 15)
    @ApiModelProperty(value = "生产批号")
    private java.lang.String pici;
	/**进厂时间*/
	@Excel(name = "进厂时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "进厂时间")
    private java.util.Date jinchangtime;
	/**存放地点*/
	@Excel(name = "存放地点", width = 15)
    @ApiModelProperty(value = "存放地点")
    private java.lang.String cunfangplace;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.lang.String shuliang;
	/**使用部位*/
	@Excel(name = "使用部位", width = 15)
    @ApiModelProperty(value = "使用部位")
    private java.lang.String usepart;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**自检结果*/
	@Excel(name = "自检结果", width = 15)
    @ApiModelProperty(value = "自检结果")
    private java.lang.String reslut;
	/**自检pdf*/
	@Excel(name = "自检pdf", width = 15)
    @ApiModelProperty(value = "自检pdf")
    private java.lang.String zjpdf;
	/**抽检结果*/
	@Excel(name = "抽检结果", width = 15)
    @ApiModelProperty(value = "抽检结果")
    private java.lang.String cjreslut;
	/**抽检pdf*/
	@Excel(name = "抽检pdf", width = 15)
    @ApiModelProperty(value = "抽检pdf")
    private java.lang.String cjpdf;
	/**计算值*/
	@Excel(name = "计算值", width = 15)
    @ApiModelProperty(value = "计算值")
    private java.lang.Double count;
}
