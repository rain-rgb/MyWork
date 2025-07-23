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
 * @Description: chaoshengbo_syljzs
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_syljzs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_syljzs对象", description="chaoshengbo_syljzs")
public class ChaoshengboSyljzs implements Serializable {
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
	/**剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3*/
	@Excel(name = "剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3", width = 15)
    @ApiModelProperty(value = "剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3")
    private java.lang.String poumianlist;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
    private java.lang.String liushuihao;
	/**试桩编号*/
	@Excel(name = "试桩编号", width = 15)
    @ApiModelProperty(value = "试桩编号")
    private java.lang.String shizhuangno;
	/**传输内容: 7：原始数据临界值包，17：分析数据临界值包*/
	@Excel(name = "传输内容: 7：原始数据临界值包，17：分析数据临界值包", width = 15)
    @ApiModelProperty(value = "传输内容: 7：原始数据临界值包，17：分析数据临界值包")
    private java.lang.Integer chuanshuleirong;
	/**包序号：用于异步传输是统计回包是否全部传输完成*/
	@Excel(name = "包序号：用于异步传输是统计回包是否全部传输完成", width = 15)
    @ApiModelProperty(value = "包序号：用于异步传输是统计回包是否全部传输完成")
    private java.lang.Integer baoxuhao;
	/**剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成*/
	@Excel(name = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成", width = 15)
    @ApiModelProperty(value = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成")
    private java.lang.String poumian1;
	/**剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成*/
	@Excel(name = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成", width = 15)
    @ApiModelProperty(value = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成")
    private java.lang.String poumian2;
	/**剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成*/
	@Excel(name = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成", width = 15)
    @ApiModelProperty(value = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成")
    private java.lang.String poumian3;
	/**剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成*/
	@Excel(name = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成", width = 15)
    @ApiModelProperty(value = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成")
    private java.lang.String poumian4;
	/**剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成*/
	@Excel(name = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成", width = 15)
    @ApiModelProperty(value = "剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成")
    private java.lang.String poumian5;
}
