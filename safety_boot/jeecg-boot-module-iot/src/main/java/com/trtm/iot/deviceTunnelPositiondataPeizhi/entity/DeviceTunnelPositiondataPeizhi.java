package com.trtm.iot.deviceTunnelPositiondataPeizhi.entity;

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
 * @Description: 隧道二衬台车与掌子面配置
 * @Author: jeecg-boot
 * @Date:   2021-08-02
 * @Version: V1.0
 */
@Data
@TableName("device_tunnel_positiondata_peizhi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_tunnel_positiondata_peizhi对象", description="隧道二衬台车与掌子面配置")
public class DeviceTunnelPositiondataPeizhi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private java.lang.String shebeiNo;
	/**二衬台车距离洞口位置*/
	@Excel(name = "二衬台车距离洞口位置", width = 15)
    @ApiModelProperty(value = "二衬台车距离洞口位置")
    private java.lang.Double jzkou;
	/**二衬台车距离掌子面位置*/
	@Excel(name = "二衬台车距离掌子面位置", width = 15)
    @ApiModelProperty(value = "二衬台车距离掌子面位置")
    private java.lang.Double jzdv;
    @Dict(dicCode = "sddirection")
	private java.lang.Integer direction;
}
