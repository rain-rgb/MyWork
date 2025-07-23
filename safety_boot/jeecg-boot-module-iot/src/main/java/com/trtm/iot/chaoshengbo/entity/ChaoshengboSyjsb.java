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
 * @Description: chaoshengbo_syjsb
 * @Author: jeecg-boot
 * @Date:   2022-02-15
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_syjsb")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_syjsb对象", description="chaoshengbo_syjsb")
public class ChaoshengboSyjsb implements Serializable {
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
	/**'包序号：用于异步传输是统计回包是否全部传输完成*/
	@Excel(name = "'包序号：用于异步传输是统计回包是否全部传输完成", width = 15)
    @ApiModelProperty(value = "'包序号：用于异步传输是统计回包是否全部传输完成")
    private java.lang.Integer baoxuhao;
	/**状态：0：中途退出，1：完成上传*/
	@Excel(name = "状态：0：中途退出，1：完成上传", width = 15)
    @ApiModelProperty(value = "状态：0：中途退出，1：完成上传")
    private java.lang.Integer zhuangtai;
	/**大序号*/
	@Excel(name = "大序号", width = 15)
    @ApiModelProperty(value = "大序号")
    private java.lang.Integer daxuhao;
	/**深度*/
	@Excel(name = "深度", width = 15)
    @ApiModelProperty(value = "深度")
    private java.lang.Double shendu;
}
