package com.trtm.iot.lqbhz.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 沥青子表信息
 * @Author: jeecg-boot
 * @Date:   2021-02-22
 * @Version: V1.0
 */
@ApiModel(value="bhz_lq_bases对象", description="沥青主表")
@Data
@TableName("bhz_lq_cailiao")
public class BhzLqCailiao implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "编号")
	private java.lang.Integer id;
	/**与lq_base表中的guid关联*/
	@ApiModelProperty(value = "与lq_base表中的guid关联")
	private java.lang.String baseGuid;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
	@ApiModelProperty(value = "材料编号")
	private java.lang.String cailiaono;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
	@ApiModelProperty(value = "材料名")
	private java.lang.String cailiaoming;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15)
	@ApiModelProperty(value = "材料类型")
	private java.lang.Integer cailiaoleixing;
	/**材料编号*/
	@Excel(name = "材料编号", width = 15)
	@ApiModelProperty(value = "材料编号")
	private java.lang.String cailiaoid;
	/**实际用量*/
	@Excel(name = "实际用量", width = 15)
	@ApiModelProperty(value = "实际用量")
	private java.lang.Double shijiyongliang;
	/**理论用量*/
	@Excel(name = "理论用量", width = 15)
	@ApiModelProperty(value = "理论用量")
	private java.lang.Double theoryNumber;
	/**实际配比*/
	@Excel(name = "实际配比", width = 15)
	@ApiModelProperty(value = "实际配比")
	private java.lang.String shijipb;
	/**理论配比*/
	@Excel(name = "理论配比", width = 15)
	@ApiModelProperty(value = "理论配比")
	private java.lang.String lilunpb;
	/**误差值*/
	@Excel(name = "误差值", width = 15)
	@ApiModelProperty(value = "误差值")
	private java.lang.Double wucha;
	/**超标值*/
	@Excel(name = "超标值", width = 15)
	@ApiModelProperty(value = "超标值")
	private java.lang.Double chaobiao;
	/**超标等级*/
	@Excel(name = "超标等级", width = 15)
	@ApiModelProperty(value = "超标等级")
	private java.lang.Integer chaobiaodengji;
	/**初级超标*/
	@Excel(name = "初级超标", width = 15)
	@ApiModelProperty(value = "初级超标")
	private java.lang.Double chujichaobiao;
	/**中级超标*/
	@Excel(name = "中级超标", width = 15)
	@ApiModelProperty(value = "中级超标")
	private java.lang.Double zhongjichaobiao;
	/**高级超标*/
	@Excel(name = "高级超标", width = 15)
	@ApiModelProperty(value = "高级超标")
	private java.lang.Double gaojichaobiao;
	private java.lang.String shebeibianhao;
}
