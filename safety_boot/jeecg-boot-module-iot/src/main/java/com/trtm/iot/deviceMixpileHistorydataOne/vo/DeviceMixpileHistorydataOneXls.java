package com.trtm.iot.deviceMixpileHistorydataOne.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2021-10-24
 * @Version: V1.0
 */
@Data
public class DeviceMixpileHistorydataOneXls {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    @Excel(name = "设备名称", dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
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
    @Excel(name = "峰值电流(A)")
    private String pileMaxelec;
    @Excel(name = "x轴角度", width = 15)
    private String pileX;
    @Excel(name = "y轴角度", width = 15)
    private String pileY;
    @Excel(name = "经度", width = 15)
    private String pileLgd;
    @Excel(name = "纬度", width = 15)
    private String pileLtd;
    @Excel(name = "历史累计浆量(L)", width = 15)
    private String pileFlowtotal;
    private String pileTime;
    private String pileStarttime;
    @Excel(name = "成桩时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date datatime;
    private String starttime;
    private String endtime;
    private java.lang.String pileUspeed;
    private java.lang.String pileUelectr;
    private java.lang.String pileDspeed;
    private java.lang.String pileDelectr;
    private java.lang.String pileCement;
    private java.lang.String pileDensity;
    private java.lang.String pileMileage;// 里程桩号
    private java.lang.String jilustarttime;//记录日期开始时间
    private java.lang.String jiluendtime;//记录日期结束时间
    private String supervisionUnit;//监理单位
    private String constructionUnit;//施工单位
    private String departNameAbbr;//项目名称
    private java.lang.Integer piletype;
    private java.lang.String pileUpress;//提钻压力
    private java.lang.String pileDpress;//下钻压力
    private java.lang.String permCement;// 每米喷浆量
    private java.lang.Integer gzcount;// 每米喷浆量

    public void setPileTime(String pileTime) throws ParseException {
        this.pileTime = pileTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end = dateFormat.parse(pileTime);
        this.endtime = sdf1.format(end);
        if (!StringUtils.isEmpty(this.pileDowntime) && !StringUtils.isEmpty(this.pileUptime)) {
            int time = (int) Double.parseDouble(this.pileDowntime) + (int) Double.parseDouble(this.pileUptime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);
            calendar.add(Calendar.SECOND, -time);
//            Long starttimes = end.getTime() - time * 1000;
//            this.starttime = sdf1.format(starttimes);
            this.starttime = sdf1.format(calendar.getTime());
        }
    }

}
