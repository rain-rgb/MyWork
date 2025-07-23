package com.trtm.iot.wbdestination.entity;

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
 * @Description: 电子锁目的地数据信息
 * @Author: jeecg-boot
 * @Date:   2022-02-23
 * @Version: V1.0
 */
@Data
@TableName("wb_destination")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wb_destination对象", description="电子锁目的地数据信息")
public class WbDestination implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**组织机构*/
    @ApiModelProperty(value = "组织机构")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**到达目的地*/
	@Excel(name = "到达目的地", width = 15)
    @ApiModelProperty(value = "到达目的地")
    private java.lang.String departname;

    //（0供货地；1收货地地）用于计算来回
    @Dict(dicCode = "mddtype")
    private java.lang.Integer mddtype;

}
