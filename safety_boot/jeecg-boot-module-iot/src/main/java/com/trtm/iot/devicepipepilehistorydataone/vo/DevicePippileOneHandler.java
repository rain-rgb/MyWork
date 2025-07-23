package com.trtm.iot.devicepipepilehistorydataone.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.pippileOneOverHandler.entity.PippileOneOverHandler;
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
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("device_pipepile_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_pipepile_historydata_one对象", description="device_pipepile_historydata_one")
public class DevicePippileOneHandler implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序号")
    private Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
	/**桩号*/
	@Excel(name = "桩号", width = 15)
    @ApiModelProperty(value = "桩号")
    private String pileNo;
	/**施工长度(m)*/
	@Excel(name = "施工长度(m)", width = 15)
    @ApiModelProperty(value = "施工长度(m)")
    private String pileRealdep;
	/**成桩时间*/
	@Excel(name = "成桩时间", width = 15)
    @ApiModelProperty(value = "成桩时间")
    private String pileWorktime;
	/**最大垂直度(%)*/
	@Excel(name = "最大垂直度(%)", width = 15)
    @ApiModelProperty(value = "最大垂直度(%)")
    private String pileY;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
    private String pileTime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
    private String pileStarttime;
	/**最大压桩力(KN)*/
	@Excel(name = "最大压桩力(KN)", width = 15)
    @ApiModelProperty(value = "最大压桩力(KN)")
    private String pileUpress;
	/**最大夹持力(KN)*/
	@Excel(name = "最大夹持力(KN)", width = 15)
    @ApiModelProperty(value = "最大夹持力(KN)")
    private String pileDpress;
	/**平均速度(cm/min)*/
	@Excel(name = "平均速度(cm/min)", width = 15)
    @ApiModelProperty(value = "平均速度(cm/min)")
    private String pileSpeed;
	/**接桩次数*/
	@Excel(name = "接桩次数", width = 15)
    @ApiModelProperty(value = "接桩次数")
    private Integer times;
	/**设计桩长*/
	@Excel(name = "设计桩长", width = 15)
    @ApiModelProperty(value = "设计桩长")
    private Integer pileDesigndep;
	/**关联软基任务单 device_mixpile_rwd*/
	@Excel(name = "关联软基任务单 device_mixpile_rwd", width = 15)
    @ApiModelProperty(value = "关联软基任务单 device_mixpile_rwd")
    private String rjrwd;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String uuid;
	/**里程*/
	@Excel(name = "里程", width = 15)
    @ApiModelProperty(value = "里程")
    private String pileMileage;
	/**数据时间*/
	@Excel(name = "数据时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据时间")
    private Date datatime;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
    private String ts;
	/**桩经度*/
	@Excel(name = "桩经度", width = 15)
    @ApiModelProperty(value = "桩经度")
    private String pileLgd;
	/**桩纬度*/
	@Excel(name = "桩纬度", width = 15)
    @ApiModelProperty(value = "桩纬度")
    private String pileLtd;
    private Integer istongji;//是否统计
    private Integer chaobiaodengji;//超标等级0：合格；1不合格
    private String address;//图片地址

    private Integer overproofStatus;
    private java.lang.String ycyy;//预警原因 桩长异常，倾角异常
    @ApiModelProperty(value = "软基成桩数据处置信息表")
    private PippileOneOverHandler handler;
}
