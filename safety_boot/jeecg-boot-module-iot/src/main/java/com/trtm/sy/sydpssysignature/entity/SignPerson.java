package com.trtm.sy.sydpssysignature.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date:   2023-09-13
 * @Version: V1.0
 */
@Data
@TableName("sy_signperson")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_signperson对象", description="sy_signperson")
public class SignPerson implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键,uuid*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键,uuid")
    private String id;

	private String sampleno;
	private String firstsign;
	private String secondsign;
	private String thirdsign;
	private int type;
}
