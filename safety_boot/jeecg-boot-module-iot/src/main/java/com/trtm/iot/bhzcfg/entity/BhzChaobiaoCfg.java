package com.trtm.iot.bhzcfg.entity;

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
 * @Description: 拌合站超标配置子表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
@ApiModel(value="bhz_call_cfg对象", description="拌合站超标配置")
@Data
@TableName("bhz_chaobiao_cfg")
public class BhzChaobiaoCfg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**自增id*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "自增id")
	private java.lang.String id;
	/**主键uuid*/
	@ApiModelProperty(value = "主键uuid")
	private java.lang.String uid;
	/**所属组织机构id*/
	@Excel(name = "所属组织机构id", width = 15)
	@ApiModelProperty(value = "所属组织机构id")
	private java.lang.String departid;
	/**拌和机编号*/
	@Excel(name = "拌和机编号", width = 15)
	@ApiModelProperty(value = "拌和机编号")
	private java.lang.String bhjno;
	/**材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他*/
	@Excel(name = "材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他", width = 15)
	@ApiModelProperty(value = "材料类型:1=细骨料,2=大石,3=中石,4=小石,5=水,6=水泥,7=矿粉,8=粉煤灰,9=外加剂,10=其他")
	private java.lang.Integer materialType;
	/**初级标准值（超过此标准为超标）*/
	@Excel(name = "初级标准值（超过此标准为超标）", width = 15)
	@ApiModelProperty(value = "初级标准值（超过此标准为超标）")
	private Float rimary;
	/**中级标准值（超过此标准为超标）*/
	@Excel(name = "中级标准值（超过此标准为超标）", width = 15)
	@ApiModelProperty(value = "中级标准值（超过此标准为超标）")
	private Float middle;
	/**高级标准值（超过此标准为超标）*/
	@Excel(name = "高级标准值（超过此标准为超标）", width = 15)
	@ApiModelProperty(value = "高级标准值（超过此标准为超标）")
	private Float advanced;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**所属机构*/
	@ApiModelProperty(value = "所属机构")
	private java.lang.String sysOrgCode;
}
