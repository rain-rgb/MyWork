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
 * @Description: chaoshengbo_sybltsj
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Data
@TableName("chaoshengbo_sybltsj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="chaoshengbo_sybltsj对象", description="chaoshengbo_sybltsj")
public class ChaoshengboSybltsj implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**设备商标识*/
	@Excel(name = "设备商标识", width = 15)
    @ApiModelProperty(value = "设备商标识")
    private String shebeino;
	/**测试仪编号：仪器商必须保证编号的唯一性*/
	@Excel(name = "测试仪编号：仪器商必须保证编号的唯一性", width = 15)
    @ApiModelProperty(value = "测试仪编号：仪器商必须保证编号的唯一性")
    private String ceshiyino;
	/**包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2*/
	@Excel(name = "包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2", width = 15)
    @ApiModelProperty(value = "包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2")
    private Integer baoxuhao;
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
	/**波列图：将byte[]转换为字符串，UTF-8编码,利用base64转码*/
	@Excel(name = "波列图：将byte[]转换为字符串，UTF-8编码,利用base64转码", width = 15)
    @ApiModelProperty(value = "波列图：将byte[]转换为字符串，UTF-8编码,利用base64转码")
    private String bolietu;
	/**剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值*/
	@Excel(name = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值", width = 15)
    @ApiModelProperty(value = "剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值")
    private String poumianhao;
	/**波列图*/
	@Excel(name = "波列图", width = 15)
    @ApiModelProperty(value = "波列图")
    private byte[] imagedata;
}
