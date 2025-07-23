package com.trtm.iot.hntbhz.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 拌合站子表材料信息
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
@ApiModel(value="bhz_cement_base对象", description="拌合站主表")
@Data
@TableName("bhz_cement_detail")
public class BhzCementDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Integer id;
	/**材料类别*/
	@Excel(name = "材料类别", width = 15)
	@ApiModelProperty(value = "材料类别")
	private Integer materialeType;
	/**材料uuid*/
	@Excel(name = "材料uuid", width = 15)
	@ApiModelProperty(value = "材料uuid")
	private String materialeId;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
	@ApiModelProperty(value = "材料名")
	private String materialeName;
	/**实际用量*/
	@Excel(name = "实际用量", width = 15)
	@ApiModelProperty(value = "实际用量")
	private Double realityNumber;
	/**理论用量*/
	@Excel(name = "理论用量", width = 15)
	@ApiModelProperty(value = "理论用量")
	private Double theoryNumber;
	/**关联的主表*/
	@ApiModelProperty(value = "关联的主表")
	private String batchNo;
	/**误差值*/
	@Excel(name = "误差值", width = 15)
	@ApiModelProperty(value = "误差值")
	private Double errorValue;
	/**超标值*/
	@Excel(name = "超标值", width = 15)
	@ApiModelProperty(value = "超标值")
	private Double overValue;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15)
	@Dict(dicCode = "over_level")
	@ApiModelProperty(value = "超标等级")
	private Integer materialeOverLevel;
	/**初级超标配置值*/
	@Excel(name = "初级超标配置值", width = 15)
	@ApiModelProperty(value = "初级超标配置值")
	private Double overPrimarySetvalue;
	/**中级超标配置值*/
	@Excel(name = "中级超标配置值", width = 15)
	@ApiModelProperty(value = "中级超标配置值")
	private Double overMiddleSetvalue;
	/**高级超标配置值*/
	@Excel(name = "高级超标配置值", width = 15)
	@ApiModelProperty(value = "高级超标配置值")
	private Double overAdvancedSetvalue;
    // 料斗号
	private String liaodouhao;

	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
	@ApiModelProperty(value = "设备编号")
	private String shebeino;

	@TableField(value = "waterRate")
	private Double waterRate;


	//料仓名
	@TableField(exist = false)
	private String binName;

	//批次
	@TableField(exist = false)
	private String batch;
	private Double realityNumber1;
	private Double theoryNumber1;
}
