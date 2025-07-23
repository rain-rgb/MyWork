package com.trtm.iot.deviceMixpileHistorydataOne.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2021-10-24
 * @Version: V1.0
 */
@Data
@ApiModel(value="device_mixpile_historydata_one处置对象", description="软基处置表")
public class DeviceMixpileOneHandlerPage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**设备编号*/
    @ApiModelProperty(value = "设备编号")
    @Excel(name="设备名称",dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
    //private String shebeino_dictText;
    @Excel(name = "单桩编号", width = 15)
    private String pileNo;
    @Excel(name = "设计桩长(m)", width = 15)
    private String pileDesigndep;
    @Excel(name = "实际桩长(m)", width = 15)
    private String pileRealdep;
    @Excel(name = "喷浆时长(S)", width = 15)
    private String pileWorktime;
    @Excel(name = "下钻时长(S)", width = 15)
    private String pileDowntime;
    @Excel(name = "下钻浆量(方)", width = 15)
    private String pileDownbeton;
    @Excel(name = "提钻时长(S)", width = 15)
    private String pileUptime;
    @Excel(name = "提钻浆量(方)", width = 15)
    private String pileUobeton;
    @Excel(name = "水灰比(%)", width = 15)
    private String pileRatio;
    @Excel(name = "平均速度(m/min)", width = 15)
    private String pileSpeed;
    @Excel(name = "电流(A)", width = 15)
    private String pileMaxelectr;
    @Excel(name = "x轴角度", width = 15)
    private String pileX;
    @Excel(name = "y轴角度", width = 15)
    private String pileY;
    @Excel(name = "经度", width = 15)
    private String pileLgd;
    @Excel(name = "纬度", width = 15)
    private String pileLtd;
    @Excel(name = "历史累计浆量(方)", width = 15)
    private String pileFlowtotal;
    private String pileTime;
    private String pileStarttime;
    @Excel(name = "成桩时间",format = "yyyy-MM-dd HH:mm:ss")
    private Date datatime;
    private String pileUspeed;// 上钻速度
    private String pileUelectr;// 上钻电流
    private String pileDspeed;// 下钻速度
    private String pileDelectr;// 下钻电流
    private String pileCement;// 水泥用量
    private String pileDensity;// 水泥浆比重
    private String pileMileage;// 里程桩号
    private Integer alertstate;// 超标字段
    private Integer chaobiaodengji; // 超标等级
    private String problem;// 问题描述
    private Integer piletype;
    private String pileUpress;//提钻压力
    private String pileDpress;//下钻压力
    private Integer handlestate; // 预警流程
    private Integer gzcount;//搅拌次数

    @ApiModelProperty(value = "软基成桩数据处置信息表")
    private MixpileOneOverHandler handler;
}
