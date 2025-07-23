package com.trtm.iot.bhzlqjipeifanwei.entity;

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
 * @Description: 沥青级配范围配置表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Data
@TableName("bhz_lq_jipei_fanwei")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_lq_jipei_fanwei对象", description="沥青级配范围配置表")
public class BhzLqJipeiFanwei implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbjno;
	/**级配标准*/
	@Excel(name = "级配标准", width = 15)
    @ApiModelProperty(value = "级配标准")
    private java.lang.String poureLocation;
	/**0.075筛孔下限*/
	@Excel(name = "0.075筛孔下限", width = 15)
    @ApiModelProperty(value = "0.075筛孔下限")
    private java.lang.String sk0075xiaxian;
	/**0.075筛孔中限*/
	@Excel(name = "0.075筛孔中限", width = 15)
    @ApiModelProperty(value = "0.075筛孔中限")
    private java.lang.String sk0075zhongxian;
	/**0.075筛孔上限*/
	@Excel(name = "0.075筛孔上限", width = 15)
    @ApiModelProperty(value = "0.075筛孔上限")
    private java.lang.String sk0075shangxian;
	/**0.15筛孔下限*/
	@Excel(name = "0.15筛孔下限", width = 15)
    @ApiModelProperty(value = "0.15筛孔下限")
    private java.lang.String sk015xiaxian;
	/**0.15筛孔中限*/
	@Excel(name = "0.15筛孔中限", width = 15)
    @ApiModelProperty(value = "0.15筛孔中限")
    private java.lang.String sk015zhongxian;
	/**0.15筛孔上限*/
	@Excel(name = "0.15筛孔上限", width = 15)
    @ApiModelProperty(value = "0.15筛孔上限")
    private java.lang.String sk015shangxian;
	/**0.3筛孔下限*/
	@Excel(name = "0.3筛孔下限", width = 15)
    @ApiModelProperty(value = "0.3筛孔下限")
    private java.lang.String sk03xiaxian;
	/**0.3筛孔中限*/
	@Excel(name = "0.3筛孔中限", width = 15)
    @ApiModelProperty(value = "0.3筛孔中限")
    private java.lang.String sk03zhongxian;
	/**0.3筛孔上限*/
	@Excel(name = "0.3筛孔上限", width = 15)
    @ApiModelProperty(value = "0.3筛孔上限")
    private java.lang.String sk03shangxian;
	/**0.6筛孔下限*/
	@Excel(name = "0.6筛孔下限", width = 15)
    @ApiModelProperty(value = "0.6筛孔下限")
    private java.lang.String sk06xiaxian;
	/**0.6筛孔中限*/
	@Excel(name = "0.6筛孔中限", width = 15)
    @ApiModelProperty(value = "0.6筛孔中限")
    private java.lang.String sk06zhongxian;
	/**0.6筛孔上限*/
	@Excel(name = "0.6筛孔上限", width = 15)
    @ApiModelProperty(value = "0.6筛孔上限")
    private java.lang.String sk06shangxian;
	/**1.18筛孔下限*/
	@Excel(name = "1.18筛孔下限", width = 15)
    @ApiModelProperty(value = "1.18筛孔下限")
    private java.lang.String sk118xiaxian;
	/**1.18筛孔中限*/
	@Excel(name = "1.18筛孔中限", width = 15)
    @ApiModelProperty(value = "1.18筛孔中限")
    private java.lang.String sk118zhongxian;
	/**1.18筛孔上限*/
	@Excel(name = "1.18筛孔上限", width = 15)
    @ApiModelProperty(value = "1.18筛孔上限")
    private java.lang.String sk118shangxian;
	/**2.36筛孔下限*/
	@Excel(name = "2.36筛孔下限", width = 15)
    @ApiModelProperty(value = "2.36筛孔下限")
    private java.lang.String sk236xiaxian;
	/**2.36筛孔中限*/
	@Excel(name = "2.36筛孔中限", width = 15)
    @ApiModelProperty(value = "2.36筛孔中限")
    private java.lang.String sk236zhongxian;
	/**2.36筛孔上限*/
	@Excel(name = "2.36筛孔上限", width = 15)
    @ApiModelProperty(value = "2.36筛孔上限")
    private java.lang.String sk236shangxian;
	/**4.75筛孔下限*/
	@Excel(name = "4.75筛孔下限", width = 15)
    @ApiModelProperty(value = "4.75筛孔下限")
    private java.lang.String sk475xiaxian;
	/**4.75筛孔中限*/
	@Excel(name = "4.75筛孔中限", width = 15)
    @ApiModelProperty(value = "4.75筛孔中限")
    private java.lang.String sk475zhongxian;
	/**4.75筛孔上限*/
	@Excel(name = "4.75筛孔上限", width = 15)
    @ApiModelProperty(value = "4.75筛孔上限")
    private java.lang.String sk475shangxian;
	/**9.5筛孔下限*/
	@Excel(name = "9.5筛孔下限", width = 15)
    @ApiModelProperty(value = "9.5筛孔下限")
    private java.lang.String sk95xiaxian;
	/**9.5筛孔中限*/
	@Excel(name = "9.5筛孔中限", width = 15)
    @ApiModelProperty(value = "9.5筛孔中限")
    private java.lang.String sk95zhongxian;
	/**9.5筛孔上限*/
	@Excel(name = "9.5筛孔上限", width = 15)
    @ApiModelProperty(value = "9.5筛孔上限")
    private java.lang.String sk95shangxian;
	/**13.2筛孔下限*/
	@Excel(name = "13.2筛孔下限", width = 15)
    @ApiModelProperty(value = "13.2筛孔下限")
    private java.lang.String sk132xiaxian;
	/**13.2筛孔中限*/
	@Excel(name = "13.2筛孔中限", width = 15)
    @ApiModelProperty(value = "13.2筛孔中限")
    private java.lang.String sk132zhongxian;
	/**13.2筛孔上限*/
	@Excel(name = "13.2筛孔上限", width = 15)
    @ApiModelProperty(value = "13.2筛孔上限")
    private java.lang.String sk132shangxian;
	/**16筛孔下限*/
	@Excel(name = "16筛孔下限", width = 15)
    @ApiModelProperty(value = "16筛孔下限")
    private java.lang.String sk16xiaxian;
	/**16筛孔中限*/
	@Excel(name = "16筛孔中限", width = 15)
    @ApiModelProperty(value = "16筛孔中限")
    private java.lang.String sk16zhongxian;
	/**16筛孔上限*/
	@Excel(name = "16筛孔上限", width = 15)
    @ApiModelProperty(value = "16筛孔上限")
    private java.lang.String sk16shangxian;
	/**19筛孔下限*/
	@Excel(name = "19筛孔下限", width = 15)
    @ApiModelProperty(value = "19筛孔下限")
    private java.lang.String sk19xiaxian;
	/**19筛孔中限*/
	@Excel(name = "19筛孔中限", width = 15)
    @ApiModelProperty(value = "19筛孔中限")
    private java.lang.String sk19zhongxian;
	/**19筛孔上限*/
	@Excel(name = "19筛孔上限", width = 15)
    @ApiModelProperty(value = "19筛孔上限")
    private java.lang.String sk19shangxian;
	/**26.5筛孔下限*/
	@Excel(name = "26.5筛孔下限", width = 15)
    @ApiModelProperty(value = "26.5筛孔下限")
    private java.lang.String sk265xiaxian;
	/**26.5筛孔中限*/
	@Excel(name = "26.5筛孔中限", width = 15)
    @ApiModelProperty(value = "26.5筛孔中限")
    private java.lang.String sk265zhongxian;
	/**26.5筛孔上限*/
	@Excel(name = "26.5筛孔上限", width = 15)
    @ApiModelProperty(value = "26.5筛孔上限")
    private java.lang.String sk265shangxian;
	/**31.5筛孔下限*/
	@Excel(name = "31.5筛孔下限", width = 15)
    @ApiModelProperty(value = "31.5筛孔下限")
    private java.lang.String sk315xiaxian;
	/**31.5筛孔中限*/
	@Excel(name = "31.5筛孔中限", width = 15)
    @ApiModelProperty(value = "31.5筛孔中限")
    private java.lang.String sk315zhongxian;
	/**31.5筛孔上限*/
	@Excel(name = "31.5筛孔上限", width = 15)
    @ApiModelProperty(value = "31.5筛孔上限")
    private java.lang.String sk315shangxian;
	/**37.5筛孔下限*/
	@Excel(name = "37.5筛孔下限", width = 15)
    @ApiModelProperty(value = "37.5筛孔下限")
    private java.lang.String sk375xiaxian;
	/**37.5筛孔中限*/
	@Excel(name = "37.5筛孔中限", width = 15)
    @ApiModelProperty(value = "37.5筛孔中限")
    private java.lang.String sk375zhongxian;
	/**37.5筛孔上限*/
	@Excel(name = "37.5筛孔上限", width = 15)
    @ApiModelProperty(value = "37.5筛孔上限")
    private java.lang.String sk375shangxian;
	/**53筛孔下限*/
	@Excel(name = "53筛孔下限", width = 15)
    @ApiModelProperty(value = "53筛孔下限")
    private java.lang.String sk53xiaxian;
	/**53筛孔中限*/
	@Excel(name = "53筛孔中限", width = 15)
    @ApiModelProperty(value = "53筛孔中限")
    private java.lang.String sk53zhongxian;
	/**53筛孔上限*/
	@Excel(name = "53筛孔上限", width = 15)
    @ApiModelProperty(value = "53筛孔上限")
    private java.lang.String sk53shangxian;
    /**唯一标识*/
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private java.lang.String uuid;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date creattime;
}
