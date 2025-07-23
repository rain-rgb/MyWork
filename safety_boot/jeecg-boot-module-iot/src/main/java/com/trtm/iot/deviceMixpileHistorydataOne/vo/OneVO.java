package com.trtm.iot.deviceMixpileHistorydataOne.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.devicemixpilehistorydatapart.entity.DeviceMixpileHistorydataPart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2021-10-24
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("device_mixpile_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "device_mixpile_historydata_one对象", description = "device_mixpile_historydata_one")
public class OneVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;
    @Excel(name = "桩号", width = 15)
    private String pileNo;

    @Excel(name = "初搅下钻开始时间", width = 15)
    private String partStartDownTime1;
    @Excel(name = "复搅下钻开始时间", width = 15)
    private String partStartDownTime2;
    @Excel(name = "初搅提起开始时间", width = 15)
    private String partStartUpTime1;
    @Excel(name = "复搅提起开始时间", width = 15)
    private String partStartUpTime2;
    @Excel(name = "初搅下钻时长", width = 15)
    private String partDownTime1;
    @Excel(name = "复搅下钻时长", width = 15)
    private String partDownTime2;
    @Excel(name = "初搅提起时长", width = 15)
    private String partUpTime1;
    @Excel(name = "复搅提起时长", width = 15)
    private String partUpTime2;
    @Excel(name = "初搅下钻速度", width = 15)
    private String partDownSpeed1;
    @Excel(name = "复搅下钻速度", width = 15)
    private String partDownSpeed2;
    @Excel(name = "初搅提起速度", width = 15)
    private String partUpSpeed1;
    @Excel(name = "复搅提起速度", width = 15)
    private String partUpSpeed2;
    @Excel(name = "初搅下钻平均电流", width = 15)
    private String pileDelectr1;// 下钻电流
    @Excel(name = "复搅下钻平均电流", width = 15)
    private String pileDelectr2;// 下钻电流
    @Excel(name = "初搅提钻平均电流", width = 15)
    private String pileUelectr1;// 上钻电流
    @Excel(name = "复搅提钻平均电流", width = 15)
    private String pileUelectr2;// 上钻电流
    @Excel(name = "初搅下钻喷浆量", width = 15)
    private String partDownBeton1;
    @Excel(name = "复搅下钻喷浆量", width = 15)
    private String partDownBeton2;
    @Excel(name = "初搅提起喷浆量", width = 15)
    private String partUpBeton1;
    @Excel(name = "复搅提起喷浆量", width = 15)
    private String partUpBeton2;
    @Excel(name = "初搅下钻平均压力", width = 15)
    private String pileDpress1;//下钻压力
    @Excel(name = "复搅下钻平均压力", width = 15)
    private String pileDpress2;//下钻压力
    @Excel(name = "初搅提钻平均压力", width = 15)
    private String pileUpress1;//提钻压力
    @Excel(name = "复搅提钻平均压力", width = 15)
    private String pileUpress2;//提钻压力

    @Excel(name = "垂直角度", width = 15)
    private String pileY;
    @Excel(name = "实际水泥用量", width = 15)
    private String pileCement;
    @Excel(name = "钻孔长度", width = 15)
    private String pileRealdep;
    @Excel(name = "施工所用时间", width = 15)
    private String pileTime;

}
