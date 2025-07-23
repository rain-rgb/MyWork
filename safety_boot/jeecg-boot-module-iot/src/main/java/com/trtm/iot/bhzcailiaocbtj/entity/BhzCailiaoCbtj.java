package com.trtm.iot.bhzcailiaocbtj.entity;

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
 * @Description: bhz_cailiao_cbtj
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Data
@TableName("bhz_cailiao_cbtj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_cailiao_cbtj对象", description="bhz_cailiao_cbtj")
public class BhzCailiaoCbtj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**shebeiNo*/
	@Excel(name = "shebeiNo", width = 15)
    @ApiModelProperty(value = "shebeiNo")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**材料类别*/
	@Excel(name = "材料类别", width = 15)
    @ApiModelProperty(value = "材料类别")
    @Dict(dicCode = "cailiaono")
    private java.lang.Integer materialeType;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private java.lang.String materialeName;
	/**初级超标次数*/
	@Excel(name = "初级超标次数", width = 15)
    @ApiModelProperty(value = "初级超标次数")
    private java.lang.Integer overPrimarySetvalue;
	/**中级超标次数*/
	@Excel(name = "中级超标次数", width = 15)
    @ApiModelProperty(value = "中级超标次数")
    private java.lang.Integer overMiddleSetvalue;
	/**高级超标次数*/
	@Excel(name = "高级超标次数", width = 15)
    @ApiModelProperty(value = "高级超标次数")
    private java.lang.Integer overAdvancedSetvalue;
	/**权限*/
    @ApiModelProperty(value = "权限")
    private java.lang.String sysOrgCode;
}
