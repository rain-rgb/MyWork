package com.trtm.iot.bhzrecipe.vo;

import java.util.List;
import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 砼拌合站理论配合比主表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@Data
@ApiModel(value="bhz_recipePage对象", description="砼拌合站理论配合比主表")
public class BhzRecipePage {

	/**id*/
	@ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**唯一ID*/
	@Excel(name = "唯一ID", width = 15)
	@ApiModelProperty(value = "唯一ID")
	private java.lang.String uuid;
	/**配合比编号*/
	@Excel(name = "配合比编号", width = 15)
	@ApiModelProperty(value = "配合比编号")
	private java.lang.String code;
	/**砼标记*/
	@Excel(name = "砼标记", width = 15)
	@ApiModelProperty(value = "砼标记")
	private java.lang.String tag;
	/**混凝土类别*/
	@Excel(name = "混凝土类别", width = 15)
	@ApiModelProperty(value = "混凝土类别")
	private java.lang.String variety;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
	@ApiModelProperty(value = "强度等级")
	private java.lang.String betlevel;
	/**抗渗等级*/
	@Excel(name = "抗渗等级", width = 15)
	@ApiModelProperty(value = "抗渗等级")
	private java.lang.String filters;
	/**抗冻等级*/
	@Excel(name = "抗冻等级", width = 15)
	@ApiModelProperty(value = "抗冻等级")
	private java.lang.String freeze;
	/**抗折等级*/
	@Excel(name = "抗折等级", width = 15)
	@ApiModelProperty(value = "抗折等级")
	private java.lang.String bend;
	/**施工季节*/
	@Excel(name = "施工季节", width = 15)
	@ApiModelProperty(value = "施工季节")
	private java.lang.String season;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
	@ApiModelProperty(value = "坍落度")
	private java.lang.String lands;
	/**搅拌时长*/
	@Excel(name = "搅拌时长", width = 15)
	@ApiModelProperty(value = "搅拌时长")
	private java.lang.Integer mixlast;
	/**设计比例*/
	@Excel(name = "设计比例", width = 15)
	@ApiModelProperty(value = "设计比例")
	private java.lang.String scale;
	/**骨料最大粒径*/
	@Excel(name = "骨料最大粒径", width = 15)
	@ApiModelProperty(value = "骨料最大粒径")
	private java.lang.String bonesz;
	/**水泥品种*/
	@Excel(name = "水泥品种", width = 15)
	@ApiModelProperty(value = "水泥品种")
	private java.lang.String cementtype;
	/**status*/
	@Excel(name = "status", width = 15)
	@ApiModelProperty(value = "status")
	private java.lang.Integer status;
	/**建立时间*/
	@Excel(name = "建立时间", width = 15)
	@ApiModelProperty(value = "建立时间")
	private java.lang.String createdate;
	/**拌合机编号*/
	@Excel(name = "拌合机编号", width = 15)
	@ApiModelProperty(value = "拌合机编号")
	private java.lang.String bhjno;
	/**砂率*/
	@Excel(name = "砂率", width = 15)
	@ApiModelProperty(value = "砂率")
	private java.lang.String sandratio;
	/**水胶比*/
	@Excel(name = "水胶比", width = 15)
	@ApiModelProperty(value = "水胶比")
	private java.lang.String waterbindratio;
	/**每方重量kg*/
	@Excel(name = "每方重量kg", width = 15)
	@ApiModelProperty(value = "每方重量kg")
	private java.lang.Double onevolume;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@ApiModelProperty(value = "是否删除")
	private java.lang.Integer isdel;

	@ExcelCollection(name="砼拌合站理论配合比子表")
	@ApiModelProperty(value = "砼拌合站理论配合比子表")
	private List<BhzRecipedetail> bhzRecipedetailList;

}
