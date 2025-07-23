package com.trtm.iot.entranceGuardRecord.entity;

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
 * @Description: entrance_guard_type
 * @Author: jeecg-boot
 * @Date:   2021-07-06
 * @Version: V1.0
 */
@Data
@TableName("entrance_guard_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="entrance_guard_type对象", description="entrance_guard_type")
public class EntranceGuardType implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String shebeiNo;
	/**关联考勤表的DoorID*/
	@Excel(name = "关联考勤表的DoorID", width = 15)
    @ApiModelProperty(value = "关联考勤表的DoorID")
    private java.lang.Integer doorid;
	/**1  左洞  2右洞*/
	@Excel(name = "1  左洞  2右洞", width = 15)
    @ApiModelProperty(value = "1  左洞  2右洞")
    private java.lang.Integer type;
}
