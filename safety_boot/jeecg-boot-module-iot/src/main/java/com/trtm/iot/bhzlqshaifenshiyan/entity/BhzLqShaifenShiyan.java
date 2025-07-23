package com.trtm.iot.bhzlqshaifenshiyan.entity;

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
 * @Description: 沥青筛分试验数据信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Data
@TableName("bhz_lq_shaifen_shiyan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bhz_lq_shaifen_shiyan对象", description="沥青筛分试验数据信息表")
public class BhzLqShaifenShiyan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**组织机构*/
	@Excel(name = "组织机构", width = 15)
    @ApiModelProperty(value = "组织机构")
    private java.lang.String departid;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String sbjno;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private java.lang.String poureLocation;
	/**材料名称*/
	@Excel(name = "材料类型", width = 15)
    @ApiModelProperty(value = "材料类型")
    @Dict(dicCode = "material_type")
    private java.lang.String cailiaomingcheng;
	/**级配标准*/
	@Excel(name = "级配标准", width = 15)
    @ApiModelProperty(value = "级配标准")
    @Dict(dictTable = "bhz_lq_jipei_fanwei", dicText = "poure_location", dicCode = "uuid")
    private java.lang.String jipeibiaozhun;
	/**筛孔53*/
	@Excel(name = "筛孔53", width = 15)
    @ApiModelProperty(value = "筛孔53")
    private java.lang.String shaikong53;
	/**筛孔37.5*/
	@Excel(name = "筛孔37.5", width = 15)
    @ApiModelProperty(value = "筛孔37.5")
    private java.lang.String shaikong375;
	/**筛孔31.5*/
	@Excel(name = "筛孔31.5", width = 15)
    @ApiModelProperty(value = "筛孔31.5")
    private java.lang.String shaikong315;
	/**筛孔26.5*/
	@Excel(name = "筛孔26.5", width = 15)
    @ApiModelProperty(value = "筛孔26.5")
    private java.lang.String shaikong265;
	/**筛孔19*/
	@Excel(name = "筛孔19", width = 15)
    @ApiModelProperty(value = "筛孔19")
    private java.lang.String shaikong19;
	/**筛孔16*/
	@Excel(name = "筛孔16", width = 15)
    @ApiModelProperty(value = "筛孔16")
    private java.lang.String shaikong16;
	/**筛孔13.2*/
	@Excel(name = "筛孔13.2", width = 15)
    @ApiModelProperty(value = "筛孔13.2")
    private java.lang.String shaikong132;
	/**筛孔9.5*/
	@Excel(name = "筛孔9.5", width = 15)
    @ApiModelProperty(value = "筛孔9.5")
    private java.lang.String shaikong95;
	/**筛孔4.75*/
	@Excel(name = "筛孔4.75", width = 15)
    @ApiModelProperty(value = "筛孔4.75")
    private java.lang.String shaikong475;
	/**筛孔2.36*/
	@Excel(name = "筛孔2.36", width = 15)
    @ApiModelProperty(value = "筛孔2.36")
    private java.lang.String shaikong236;
	/**筛孔1.18*/
	@Excel(name = "筛孔1.18", width = 15)
    @ApiModelProperty(value = "筛孔1.18")
    private java.lang.String shaikong118;
	/**筛孔0.6*/
	@Excel(name = "筛孔0.6", width = 15)
    @ApiModelProperty(value = "筛孔0.6")
    private java.lang.String shaikong06;
	/**筛孔0.3*/
	@Excel(name = "筛孔0.3", width = 15)
    @ApiModelProperty(value = "筛孔0.3")
    private java.lang.String shaikong03;
	/**筛孔0.15*/
	@Excel(name = "筛孔0.15", width = 15)
    @ApiModelProperty(value = "筛孔0.15")
    private java.lang.String shaikong015;
	/**筛孔0.075*/
	@Excel(name = "筛孔0.075", width = 15)
    @ApiModelProperty(value = "筛孔0.075")
    private java.lang.String shaikong0075;
	/**是否使用:0=未使用,1=已使用*/
	@Excel(name = "是否使用:0=未使用,1=已使用", width = 15)
    @ApiModelProperty(value = "是否使用:0=未使用,1=已使用")
    @Dict(dicCode = "isuse")
    private java.lang.Integer isuse;
}
