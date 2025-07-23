package com.trtm.iot.chaoshengbo.entity;

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
 * @Description: chaoshengbo_sybsj
 * @Author: jeecg-boot
 * @Date:   2021-04-16
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_sybsj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_sybsj对象", description="chaoshengbo_sybsj")
public class ChaoshengboSybsj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    private String shebeino;
	/**测试仪编号：仪器商必须保证编号唯一性*/
	@Excel(name = "测试仪编号：仪器商必须保证编号唯一性", width = 15)
    @ApiModelProperty(value = "测试仪编号：仪器商必须保证编号唯一性")
    private String ceshiyino;
	/**剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3*/
	@Excel(name = "剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3", width = 15)
    @ApiModelProperty(value = "剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3")
    private String poumianlist;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
    private String liushuihao;
	/**试桩编号*/
	@Excel(name = "试桩编号", width = 15)
    @ApiModelProperty(value = "试桩编号")
    private String shizhuangno;
	/**传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包*/
	@Excel(name = "传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包", width = 15)
    @ApiModelProperty(value = "传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包")
    private Integer chaungshuleirong;
	/**包序号：用于异步传输是统计回包是否全部传输完成*/
	@Excel(name = "包序号：用于异步传输是统计回包是否全部传输完成", width = 15)
    @ApiModelProperty(value = "包序号：用于异步传输是统计回包是否全部传输完成")
    private Integer baoxuhao;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao1;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao2;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao3;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao4;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao5;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao6;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao7;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao8;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao9;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private String poumianhao10;
	/**深度*/
	@Excel(name = "深度", width = 15)
    @ApiModelProperty(value = "深度")
    private Double shendu;
}
