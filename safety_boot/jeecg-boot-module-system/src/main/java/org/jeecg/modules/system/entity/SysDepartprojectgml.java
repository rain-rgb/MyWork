package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 分部分项表
 * <p>
 *
 * @Author Steve
 * @Since  2019-01-22
 */
@Data
public class SysDepartprojectgml implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**父机构ID*/
	private String parentId;
	/**机构/部门名称*/
	@Excel(name="机构/部门名称",width=15)
	private String departName;
	/**英文名*/
	@Excel(name="英文名",width=15)
	private String departNameEn;
	/**缩写*/
	private String departNameAbbr;
	/**排序*/
	@Excel(name="排序",width=15)
	private Integer departOrder;
	/**描述*/
	@Excel(name="描述",width=15)
	private String description;
	/**机构类别 1组织机构，2岗位*/
	@Excel(name="机构类别",width=15,dicCode="org_category")
	private String orgCategory;
	/**机构类型*/
	private String orgType;
	/**机构编码*/
	@Excel(name="机构编码",width=15)
	private String orgCode;
	/**手机号*/
	@Excel(name="手机号",width=15)
	private String mobile;
	/**传真*/
	@Excel(name="传真",width=15)
	private String fax;
	/**地址*/
	@Excel(name="地址",width=15)
	private String address;
	/**备注*/
	@Excel(name="备注",width=15)
	private String memo;
	/**状态（1启用，0不启用）*/
	@Dict(dicCode = "depart_status")
	private String status;
	/**删除状态（0，正常，1已删除）*/
	@Dict(dicCode = "del_flag")
	private String delFlag;
	/**创建人*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
	private String orgCodes;
	private String oldid;
	private String treeid;
	private Integer pici;
	private Integer parentstatus;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updatime;

	private String wbsStructureType;
	private String templateid;

	List<SysDepartproject> sysdplist;
}
