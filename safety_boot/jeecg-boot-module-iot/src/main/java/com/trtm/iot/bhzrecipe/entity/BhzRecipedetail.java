package com.trtm.iot.bhzrecipe.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 砼拌合站理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-06-29
 * @Version: V1.0
 */
@ApiModel(value="bhz_recipe对象", description="砼拌合站理论配合比主表")
@Data
@TableName("bhz_recipedetail")
public class BhzRecipedetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**唯一区分的id*/
	@Excel(name = "唯一区分的id", width = 15)
	@ApiModelProperty(value = "唯一区分的id")
	private java.lang.String origid;
	/**配合比id*/
	@ApiModelProperty(value = "配合比id")
	private java.lang.String recipeid;
	/**材料id*/
	@Excel(name = "材料id", width = 15)
	@ApiModelProperty(value = "材料id")
	private java.lang.String materialid;
	/**用量*/
	@Excel(name = "用量", width = 15)
	@ApiModelProperty(value = "用量")
	private java.lang.Double amount;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
	@ApiModelProperty(value = "材料名称")
	private java.lang.String materialname;
	/**status*/
	@Excel(name = "status", width = 15)
	@ApiModelProperty(value = "status")
	private java.lang.Integer status;
	/**材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他*/
	@Excel(name = "材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他", width = 15)
	@ApiModelProperty(value = "材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他")
	private java.lang.Integer materialTypes;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@ApiModelProperty(value = "是否删除")
	private java.lang.Integer isdel;
}
