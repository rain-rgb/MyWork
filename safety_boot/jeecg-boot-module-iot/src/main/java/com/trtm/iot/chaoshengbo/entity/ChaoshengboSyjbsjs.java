package com.trtm.iot.chaoshengbo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Description: chaoshengbo_syjbsj
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_syjbsj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_syjbsj对象", description="chaoshengbo_syjbsj")
public class ChaoshengboSyjbsjs  implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeino;
	/**实验方法*/
	@Excel(name = "实验方法", width = 15)
    @ApiModelProperty(value = "实验方法")
    private String syff;
	/**试桩类型：20：圆桩；21：方桩；22：地下连续墙*/
	@Excel(name = "试桩类型：20：圆桩；21：方桩；22：地下连续墙", width = 15)
    @ApiModelProperty(value = "试桩类型：20：圆桩；21：方桩；22：地下连续墙")
    private Integer shizhuangleixing;
	/**测试仪编号：仪器商必须保证编号的唯一性*/
	@Excel(name = "测试仪编号：仪器商必须保证编号的唯一性", width = 15)
    @ApiModelProperty(value = "测试仪编号：仪器商必须保证编号的唯一性")
    private String ceshiyino;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
    private String liushuihao;
	/**试桩编号*/
	@Excel(name = "试桩编号", width = 15)
    @ApiModelProperty(value = "试桩编号")
    private String shizhuangno;
	/**剖面列表:剖面列表，以逗号分隔。	比如：1-2,1-3*/
	@Excel(name = "剖面列表:剖面列表，以逗号分隔。	比如：1-2,1-3", width = 15)
    @ApiModelProperty(value = "剖面列表:剖面列表，以逗号分隔。	比如：1-2,1-3")
    private String poumianlist;
	/**传输内容: 0：原始数据;10：分析数据*/
	@Excel(name = "传输内容: 0：原始数据;10：分析数据", width = 15)
    @ApiModelProperty(value = "传输内容: 0：原始数据;10：分析数据")
    private Integer chuanshuleirong;
	/**包序号：固定值“1“*/
	@Excel(name = "包序号：固定值“1“", width = 15)
    @ApiModelProperty(value = "包序号：固定值“1“")
    private Integer baoxuhao;
	/**当前时间：yyyy-MM-dd hh:mm:ss*/
	@Excel(name = "当前时间：yyyy-MM-dd hh:mm:ss", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "当前时间：yyyy-MM-dd hh:mm:ss")
    private Date dangqiantime;
	/**测试时间：yyyy-MM-dd hh:mm:ss*/
	@Excel(name = "测试时间：yyyy-MM-dd hh:mm:ss", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "测试时间：yyyy-MM-dd hh:mm:ss")
    private Date ceshitime;
	/**浇铸日期：yyyy-MM-dd hh:mm:ss*/
	@Excel(name = "浇铸日期：yyyy-MM-dd hh:mm:ss", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇铸日期：yyyy-MM-dd hh:mm:ss")
    private Date jiaozhutime;
	/**文件名*/
	@Excel(name = "文件名", width = 15)
    @ApiModelProperty(value = "文件名")
    private String wenjianming;
	/**桩径*/
	@Excel(name = "桩径", width = 15)
    @ApiModelProperty(value = "桩径")
    private Double zhuangjing;
	/**桩长*/
	@Excel(name = "桩长", width = 15)
    @ApiModelProperty(value = "桩长")
    private Double zhuangchang;
	/**移距*/
	@Excel(name = "移距", width = 15)
    @ApiModelProperty(value = "移距")
    private Double yiju;
	/**管数*/
	@Excel(name = "管数", width = 15)
    @ApiModelProperty(value = "管数")
    private Integer guanshu;
	/**剖面数*/
	@Excel(name = "剖面数", width = 15)
    @ApiModelProperty(value = "剖面数")
    private Integer poumianshu;
	/**采样频率*/
	@Excel(name = "采样频率", width = 15)
    @ApiModelProperty(value = "采样频率")
    private Double caiyangpinlv;
	/**采样长度*/
	@Excel(name = "采样长度", width = 15)
    @ApiModelProperty(value = "采样长度")
    private Integer caiyanglength;
	/**AD转换器的位数：硬件模拟信号转数字信号转换器的转换位数*/
	@Excel(name = "AD转换器的位数：硬件模拟信号转数字信号转换器的转换位数", width = 15)
    @ApiModelProperty(value = "AD转换器的位数：硬件模拟信号转数字信号转换器的转换位数")
    private Integer adzhuanhuan;
	/**波形幅值系数: 波形数据转换为电压mv值的系数*/
	@Excel(name = "波形幅值系数: 波形数据转换为电压mv值的系数", width = 15)
    @ApiModelProperty(value = "波形幅值系数: 波形数据转换为电压mv值的系数")
    private Integer boxingfuzhi;
	/**波形基线：波形数据基线处的值*/
	@Excel(name = "波形基线：波形数据基线处的值", width = 15)
    @ApiModelProperty(value = "波形基线：波形数据基线处的值")
    private Integer boxingjixian;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private Double jingdu;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private Double weidu;
	/**首波增强: 1：是,0： 否*/
	@Excel(name = "首波增强: 1：是,0： 否", width = 15)
    @ApiModelProperty(value = "首波增强: 1：是,0： 否")
    private Integer shoubozengqiang;
	/**软件虑波: 1：是,0： 否*/
	@Excel(name = "软件虑波: 1：是,0： 否", width = 15)
    @ApiModelProperty(value = "软件虑波: 1：是,0： 否")
    private Integer ruanjianlvbo;
	/**声测管材料速度:单位km/s 精确到小数点后三位*/
	@Excel(name = "声测管材料速度:单位km/s 精确到小数点后三位", width = 15)
    @ApiModelProperty(value = "声测管材料速度:单位km/s 精确到小数点后三位")
    private Double shengceguan;
	/**声测管外径*/
	@Excel(name = "声测管外径", width = 15)
    @ApiModelProperty(value = "声测管外径")
    private Double shengceguanwaijing;
	/**声测管内径*/
	@Excel(name = "声测管内径", width = 15)
    @ApiModelProperty(value = "声测管内径")
    private Double shengceguanneijing;
	/**探头外径*/
	@Excel(name = "探头外径", width = 15)
    @ApiModelProperty(value = "探头外径")
    private Double tantouwaijing;
	/**方位角*/
	@Excel(name = "方位角", width = 15)
    @ApiModelProperty(value = "方位角")
    private Integer fangweijiao;
	/**轮径*/
	@Excel(name = "轮径", width = 15)
    @ApiModelProperty(value = "轮径")
    private Double lunjing;
	/**现径*/
	@Excel(name = "线径", width = 15)
    @ApiModelProperty(value = "线径")
    private Double xianjing;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian1;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成。")
    private String poumian2;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian3;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian4;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian5;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian6;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian7;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian8;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian9;
	/**剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成*/
	@Excel(name = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成", width = 15)
    @ApiModelProperty(value = "剖面1-10数据:以”,”分隔组成的数据，由剖面，测试深度，传感器高差，跨距，增益，校零，延迟时间，高通，低通几个float类型数据  组成")
    private String poumian10;
	/**测试规范：测试的桩需要符合的规范*/
	@Excel(name = "测试规范：测试的桩需要符合的规范", width = 15)
    @ApiModelProperty(value = "测试规范：测试的桩需要符合的规范")
    private Integer ceshiguifan;
	/**hege*/
	@Excel(name = "hege", width = 15)
    @ApiModelProperty(value = "hege")
    private String hege;
	@Excel(name = "sgbw", width = 15)
    @ApiModelProperty(value = "sgbw")
    private String sgbw;
	@Excel(name = "bumen", width = 15)
    @ApiModelProperty(value = "bumen")
    private String bumen;
	/**chuzhiid*/
	@Excel(name = "chuzhiid", width = 15)
    @ApiModelProperty(value = "chuzhiid")
    private String chuzhiid;
	/**上传时间：yyyy-MM-dd hh:mm:ss*/
	@Excel(name = "上传时间：yyyy-MM-dd hh:mm:ss", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间：yyyy-MM-dd hh:mm:ss")
    private Date shangchuantime;
    private String jcdw;
    private String sgdw;
    private String projectName;
    private String sgbwName;
    private String code;


}
