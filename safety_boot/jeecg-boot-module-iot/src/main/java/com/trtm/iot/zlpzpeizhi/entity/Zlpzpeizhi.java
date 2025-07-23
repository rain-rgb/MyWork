package com.trtm.iot.zlpzpeizhi.entity;

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
 * @Description: 浙路品质推送配置
 * @Author: jeecg-boot
 * @Date:   2023-12-12
 * @Version: V1.0
 */
@Data
@TableName("zlpzpeizhi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zlpzpeizhi对象", description="浙路品质推送配置")
public class Zlpzpeizhi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**shebeino*/
	@Excel(name = "shebeino", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "shebeino")
    private String shebeino;
	/**project*/
	@Excel(name = "project", width = 15)
    @Dict(dictTable = "zlpz_project", dicText = "name", dicCode = "xmid")
    @ApiModelProperty(value = "project")
    private String project;
	/**biaoduan*/
	@Excel(name = "biaoduan", width = 15)
    @Dict(dictTable = "zlpz_tenders", dicText = "name", dicCode = "bdid")
    @ApiModelProperty(value = "biaoduan")
    private String biaoduan;
	/**shebeiid*/
	@Excel(name = "shebeiid", width = 15)
    @ApiModelProperty(value = "shebeiid")
    private String shebeiid;
	/**shebeiname*/
	@Excel(name = "shebeiname", width = 15)
    @ApiModelProperty(value = "shebeiname")
    private String shebeiname;
	/**sbtype*/
	@Excel(name = "sbtype", width = 15)
    @ApiModelProperty(value = "sbtype")
    @Dict(dicCode = "sbtype")
    private Integer sbtype;
	/**orgcode*/
	@Excel(name = "orgcode", width = 15)
    @ApiModelProperty(value = "orgcode")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String orgcode;
}
