package com.trtm.iot.deviceMixpileHistorydataOne.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2021-10-24
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("device_mixpile_historydata_one")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="device_mixpile_historydata_one对象", description="device_mixpile_historydata_one")
public class DeviceMixpileHistorydataOnePage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**设备编号*/
    @ApiModelProperty(value = "设备编号")
    @Excel(name="设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
    @Excel(name = "桩号", width = 15)
    private String pileNo;
    @Excel(name = "设计桩长(m)", width = 15)
    private String pileDesigndep;
    @Excel(name = "实际桩长(m)", width = 15)
    private String pileRealdep;
    @Excel(name = "成桩时长(s)", width = 15)
    private String pileWorktime;
    @Excel(name = "下钻时长(s)", width = 15)
    private String pileDowntime;
    @Excel(name = "下钻平均速度", width = 15)
    private String pileDspeed;// 下钻速度
    @Excel(name = "下钻平均电流",width = 15)
    private String pileDelectr;// 下钻电流
    @Excel(name = "下钻浆量(m3)", width = 15)
    private String pileDownbeton;
    @Excel(name = "提钻时长(s)", width = 15)
    private String pileUptime;
    @Excel(name = "提钻平均速度", width = 15)
    private String pileUspeed;// 上钻速度
    @Excel(name = "提钻平均电流", width = 15)
    private String pileUelectr;// 上钻电流
    @Excel(name = "提钻浆量(m3)", width = 15)
    private String pileUobeton;
    @Excel(name = "泥浆比重", width = 15)
    private String pileDensity;// 水泥浆比重
    @Excel(name = "水泥用量(kg)", width = 15)
    private String pileCement;// 水泥用量
    @Excel(name = "水平角度", width = 15)
    private String pileX;
    @Excel(name = "垂直角度", width = 15)
    private String pileY;
    @Excel(name = "经度", width = 15)
    private String pileLgd;
    @Excel(name = "纬度", width = 15)
    private String pileLtd;
    @Excel(name = "下钻平均压力", width = 15)
    private String pileDpress;//下钻压力
    @Excel(name = "提钻平均压力", width = 15)
    private String pileUpress;//提钻压力
    @Excel(name = "总累计流量(m3)", width = 15)
    private String pileFlowtotal;
    @Excel(name = "里程", width = 15)
    private String pileMileage;// 里程桩号
    @Excel(name = "成桩时间",format = "yyyy-MM-dd HH:mm:ss")
    private String pileTime;
    @Excel(name = "桩类型", width = 15)
    private Integer piletype;
    @Excel(name = "每米水泥用量", width = 15)
    private String pileMinelec;//最小电流(每米水泥用量)
    @Excel(name = "峰值电流(A)", width = 15)
    private String pileMaxelec;
    @Excel(name = "水灰比(%)", width = 15)
    private String pileRatio;
    @Excel(name = "开始时间", width = 15)
    private String pileStarttime;
    @Excel(name = "桩最大电流", width = 15)
    private String pileMaxelectr;
//    @Excel(name = "数据时间",format = "yyyy-MM-dd HH:mm:ss")
    private Date datatime;
    private Integer alertstate;// 超标字段
    private Integer chaobiaodengji; // 超标等级
    private Integer handlestate; // 预警流程
    private String problem;// 问题描述
    @Excel(name = "喷浆深度", width = 15)
    private String pjdep;//喷浆深度
    private  String rjrwd;//关联软基任务单
    private String pileTs;
    @Excel(name = "桩平均速度", width = 15)
    private String pileSpeed;
    private Integer istongji;//是否统计
    private Integer taicangpush;//太仓256项目是否推送
    @Excel(name = "搅拌次数", width = 15)
    private Integer gzcount;//1初搅；2复搅；
    private String emptydep;//空搅拌米数
    public void setPileWorktime(String pileWorktime) {
        this.pileWorktime = pileWorktime.trim();
    }

    public void setPileDowntime(String pileDowntime) {
        this.pileDowntime = pileDowntime.trim();
    }

    public void setPileUptime(String pileUptime) {
        this.pileUptime = pileUptime.trim();
    }
}
