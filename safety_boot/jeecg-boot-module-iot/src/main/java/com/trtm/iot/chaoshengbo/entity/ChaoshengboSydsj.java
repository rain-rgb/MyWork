package com.trtm.iot.chaoshengbo.entity;

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
 * @Description: chaoshengbo_sydsj
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_sydsj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_sydsj对象", description="chaoshengbo_sydsj")
public class ChaoshengboSydsj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private java.lang.Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    private java.lang.String shebeino;
	/**测试仪编号：仪器商必须保证编号的唯一性*/
	@Excel(name = "测试仪编号：仪器商必须保证编号的唯一性", width = 15)
    @ApiModelProperty(value = "测试仪编号：仪器商必须保证编号的唯一性")
    private java.lang.String ceshiyino;
	/**剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3*/
	@Excel(name = "剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3", width = 15)
    @ApiModelProperty(value = "剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3")
    private java.lang.String poumianlist;
	/**包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）*/
	@Excel(name = "包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）", width = 15)
    @ApiModelProperty(value = "包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）")
    private java.lang.Integer baoxuhao;
	/**大序号*/
	@Excel(name = "大序号", width = 15)
    @ApiModelProperty(value = "大序号")
    private java.lang.Integer daxuhao;
	/**深度：数据以”,”分隔的128个短整型的点数据组成*/
	@Excel(name = "深度：数据以”,”分隔的128个短整型的点数据组成", width = 15)
    @ApiModelProperty(value = "深度：数据以”,”分隔的128个短整型的点数据组成")
    private java.lang.Double shendu;
	/**跨距*/
	@Excel(name = "跨距", width = 15)
    @ApiModelProperty(value = "跨距")
    private java.lang.Double kuaju;
	/**接收高度*/
	@Excel(name = "接收高度", width = 15)
    @ApiModelProperty(value = "接收高度")
    private java.lang.Double jieshoulength;
	/**增益*/
	@Excel(name = "增益", width = 15)
    @ApiModelProperty(value = "增益")
    private java.lang.Double zengyi;
	/**延时*/
	@Excel(name = "延时", width = 15)
    @ApiModelProperty(value = "延时")
    private java.lang.Double yanshi;
	/**声时索引值*/
	@Excel(name = "声时索引值", width = 15)
    @ApiModelProperty(value = "声时索引值")
    private java.lang.Integer shengshiindex;
	/**首波峰索引值*/
	@Excel(name = "首波峰索引值", width = 15)
    @ApiModelProperty(value = "首波峰索引值")
    private java.lang.Integer shoubofengindex;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
    private java.lang.String liushuihao;
	/**试桩编号*/
	@Excel(name = "试桩编号", width = 15)
    @ApiModelProperty(value = "试桩编号")
    private java.lang.String shizhuangno;
	/**传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包*/
	@Excel(name = "传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包", width = 15)
    @ApiModelProperty(value = "传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包")
    private java.lang.Integer chaungshuleirong;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)")
    private java.lang.String poumianhao;
	/**数据*/
	@Excel(name = "数据", width = 15)
    @ApiModelProperty(value = "数据")
    private java.lang.String shuju;
}
