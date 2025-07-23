package com.trtm.iot.syj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: f_yaliji
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Data
@TableName("f_yaliji")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="f_yaliji对象", description="f_yaliji")
public class FsYaliji implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**version*/
	@Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private Integer version;
	/**唯一码*/
	@Excel(name = "唯一码", width = 15)
    @ApiModelProperty(value = "唯一码")
    private String fGuid;
	/**试件序号*/
	@Excel(name = "试件序号", width = 15)
    @ApiModelProperty(value = "试件序号")
    private String sjxh;
	/**试验主表唯一码*/
	@Excel(name = "试验主表唯一码", width = 15)
    @ApiModelProperty(value = "试验主表唯一码")
    private String syjid;
	/**制件编号*/
	@Excel(name = "制件编号", width = 15)
    @ApiModelProperty(value = "制件编号")
    private String zzjj;
	/**抗压力值*/
	@Excel(name = "抗压力值", width = 15)
    @ApiModelProperty(value = "抗压力值")
    private String kylz;
	/**抗压强度*/
	@Excel(name = "抗压强度", width = 15)
    @ApiModelProperty(value = "抗压强度")
    private String kyqd;
	/**试验时间*/
	@Excel(name = "试验时间", width = 15)
    @ApiModelProperty(value = "试验时间")
    private String sysj;
	/**完成时间*/
	@Excel(name = "完成时间", width = 15)
    @ApiModelProperty(value = "完成时间")
    private String wcsj;
	/**力值过程值*/
	@Excel(name = "力值过程值", width = 15)
    @ApiModelProperty(value = "力值过程值")
    private String yskylz;
	/**时间过程值*/
	@Excel(name = "时间过程值", width = 15)
    @ApiModelProperty(value = "时间过程值")
    private String sjgc;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
	/**二维码*/
	@Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码")
    private String qrcode;

    private String spic;// 开始图片
    private String pspic;// 破碎图片
    private String videopic;// 视频ID
    private Integer issend;
    private String sbbh;
}
