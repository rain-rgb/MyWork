package org.jeecg.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("oss_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OSSFile extends JeecgEntity {

	private static final long serialVersionUID = 1L;

	@Excel(name = "文件名称")
	private String fileName;

	@Excel(name = "文件描述")
	private String name;

	@Excel(name = "文件地址")
	private String url;
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
	@Excel(name = "所属组织机构")
	private String sysOrgCode;

	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Excel(name = "创建人")
	private String createBy;

	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@Dict(dicCode = "file_type")
	private Integer type;//1:人机安全管理；2：风险管控；3：现场应急管理；4：安全专项方案管理；5：隐患排查;6：安全培训；7：施工安全智慧化监测；8：土壤检测


}
