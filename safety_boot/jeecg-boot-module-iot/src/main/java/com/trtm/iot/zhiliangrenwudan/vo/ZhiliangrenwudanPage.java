package com.trtm.iot.zhiliangrenwudan.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 任务单（制梁）表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-13
 * @Version: V1.0
 */
@Data
@TableName("zhiliangrenwudan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zhiliangrenwudan对象", description="任务单（制梁）表信息")
public class ZhiliangrenwudanPage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**生产线（0 第一生产线  1 第二生产线）*/
	@Excel(name = "生产线（0 第一生产线  1 第二生产线）", width = 15)
    @ApiModelProperty(value = "生产线（0 第一生产线  1 第二生产线）")
    @Dict(dicCode = "stations")
    private Integer station;
	/**任务单编号*/
	@Excel(name = "任务单编号", width = 15)
    @ApiModelProperty(value = "任务单编号")
    private String code;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date dattim;
    /**计划生产时间*/
    @Excel(name = "计划生产时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划生产时间")
    private Date productiontime;
    /**计划架梁时间*/
    @Excel(name = "计划架梁时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划架梁时间")
    private java.util.Date productiontime1;
	/**任务性质*/
	@Excel(name = "任务性质", width = 15)
    @ApiModelProperty(value = "任务性质")
    private String attribute;
	/**1线配合比编号*/
	@Excel(name = "1线配合比编号", width = 15)
    @ApiModelProperty(value = "1线配合比编号")
    private String recipe;
	/**2线配合比编号*/
	@Excel(name = "2线配合比编号", width = 15)
    @ApiModelProperty(value = "2线配合比编号")
    private String recipes;
	/**合同信息*/
	@Excel(name = "合同信息", width = 15)
    @ApiModelProperty(value = "合同信息")
    private String contract;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private String customer;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String projname;
	/**工程类别*/
	@Excel(name = "工程类别", width = 15)
    @ApiModelProperty(value = "工程类别")
    private String projtype;
	/**工程级别*/
	@Excel(name = "工程级别", width = 15)
    @ApiModelProperty(value = "工程级别")
    private String projgrade;
	/**开工面积*/
	@Excel(name = "开工面积", width = 15)
    @ApiModelProperty(value = "开工面积")
    private Double projarea;
	/**施工地址*/
	@Excel(name = "施工地址", width = 15)
    @ApiModelProperty(value = "施工地址")
    private String projadr;
	/**运输距离*/
	@Excel(name = "运输距离", width = 15)
    @ApiModelProperty(value = "运输距离")
    private Double distance;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String conspos;
	/**浇筑方式*/
	@Excel(name = "浇筑方式", width = 15)
    @ApiModelProperty(value = "浇筑方式")
    private String pour;
	/**产品种类*/
	@Excel(name = "产品种类", width = 15)
    @ApiModelProperty(value = "产品种类")
    private String variety;
	/**强度等级*/
	@Excel(name = "强度等级", width = 15)
    @ApiModelProperty(value = "强度等级")
    private String betlev;
	/**抗渗等级*/
	@Excel(name = "抗渗等级", width = 15)
    @ApiModelProperty(value = "抗渗等级")
    private String filter;
	/**抗冻等级*/
	@Excel(name = "抗冻等级", width = 15)
    @ApiModelProperty(value = "抗冻等级")
    private String freeze;
	/**坍落度*/
	@Excel(name = "坍落度", width = 15)
    @ApiModelProperty(value = "坍落度")
    private String lands;
	/**水泥品种*/
	@Excel(name = "水泥品种", width = 15)
    @ApiModelProperty(value = "水泥品种")
    private String cement;
	/**石子种类*/
	@Excel(name = "石子种类", width = 15)
    @ApiModelProperty(value = "石子种类")
    private String stone;
	/**骨料粒径*/
	@Excel(name = "骨料粒径", width = 15)
    @ApiModelProperty(value = "骨料粒径")
    private String bnsize;
	/**外加剂要求*/
	@Excel(name = "外加剂要求", width = 15)
    @ApiModelProperty(value = "外加剂要求")
    private String addliq;
	/**技术要求*/
	@Excel(name = "技术要求", width = 15)
    @ApiModelProperty(value = "技术要求")
    private String request;
	/**搅拌时间*/
	@Excel(name = "搅拌时间", width = 15)
    @ApiModelProperty(value = "搅拌时间")
    private Double mixlast;
	/**任务方量*/
	@Excel(name = "任务方量", width = 15)
    @ApiModelProperty(value = "任务方量")
    private Double mete;
	/**浇注日期*/
	@Excel(name = "浇注日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "浇注日期")
    private Date begtim;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止日期")
    private Date endtim;
	/**调度人员*/
	@Excel(name = "调度人员", width = 15)
    @ApiModelProperty(value = "调度人员")
    private String attamper;
	/**标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String flag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
	/**权限*/
    @ApiModelProperty(value = "权限")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
	/**是否删除 0未删除 1已删除*/
	@Excel(name = "是否删除 0未删除 1已删除", width = 15)
    @ApiModelProperty(value = "是否删除 0未删除 1已删除")
    private Integer isdel;
	/**0 未审核  1已审核*/
	@Excel(name = "0 未审核  1已审核", width = 15)
    @ApiModelProperty(value = "0 未审核  1已审核")
    private Integer status;
    /**提梁状态*/
    @Excel(name = "提梁状态", width = 15)
    @ApiModelProperty(value = "提梁状态")
    private Integer wiretiestatus;
	/**浇筑状态*/
	@Excel(name = "浇筑状态", width = 15)
    @ApiModelProperty(value = "浇筑状态")
    private Integer jiaozhustatus;
	/**收面静置状态*/
	@Excel(name = "收面静置状态", width = 15)
    @ApiModelProperty(value = "收面静置状态")
    private Integer jingzhistatus;
	/**蒸养状态1*/
	@Excel(name = "蒸养状态1", width = 15)
    @ApiModelProperty(value = "蒸养状态1")
    private Integer zhengyangstatus1;
	/**蒸养状态2*/
	@Excel(name = "蒸养状态2", width = 15)
    @ApiModelProperty(value = "蒸养状态2")
    private Integer zhengyangstatus2;
	/**张拉状态*/
	@Excel(name = "张拉状态", width = 15)
    @ApiModelProperty(value = "张拉状态")
    private Integer zhanglastatus;
	/**提梁状态*/
	@Excel(name = "提梁状态", width = 15)
    @ApiModelProperty(value = "提梁状态")
    private Integer tiliangstatus;
    /**封端状态*/
    @Excel(name = "封端状态", width = 15)
    @ApiModelProperty(value = "封端状态")
    private Integer fengduanstatus;
    /**唯一id*/
    @Excel(name = "唯一id", width = 15)
    @ApiModelProperty(value = "唯一id")
    private String uuid;
    /**存梁状态*/
    @Excel(name = "存梁状态", width = 15)
    @ApiModelProperty(value = "存梁状态")
    private Integer cunliangstatus;
    /**台座编号*/
    @Excel(name = "台座编号", width = 15)
    @ApiModelProperty(value = "台座编号")
    private String taizuono;
    /**工序序号(工序字典值)*/
    @Excel(name = "工序序号(工序字典值)", width = 15)
    @ApiModelProperty(value = "工序序号(工序字典值)")
    private String xuhao;
    private java.lang.String oderno;//订单编号
    private java.lang.Integer sfhg;//是否合格
    private java.lang.String acceptOpinion;//验收意见
    private java.lang.String qcode;//二维码
    private List<ZhiliangGongxu> zhiliangGongxuList;
}
