package com.trtm.sy.sydpssysample.entity;

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
 * @Description: sy_dps_sy_sampleqrcode
 * @Author: jeecg-boot
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_sampleqrcode")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_sampleqrcode对象", description="sy_dps_sy_sampleqrcode")
public class SyDpsSySampleqrcode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号UUID,主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号UUID,主键")
    private String id;
	/**二维码编码*/
	@Excel(name = "二维码编码", width = 15)
    @ApiModelProperty(value = "二维码编码")
    private String qrcode;
	/**芯片编码*/
	@Excel(name = "芯片编码", width = 15)
    @ApiModelProperty(value = "芯片编码")
    private String uhfcode;
	/**外键，F_Yaliji表的F_GUID*/
	@Excel(name = "外键，F_Yaliji表的F_GUID", width = 15)
    @ApiModelProperty(value = "外键，F_Yaliji表的F_GUID")
    private String fGuid;
	/**外键，T_SYJZB表的SYJID*/
	@Excel(name = "外键，T_SYJZB表的SYJID", width = 15)
    @ApiModelProperty(value = "外键，T_SYJZB表的SYJID")
    private String syjid;
	/**二维码和芯片的验证时间（yyyy-MM-dd HH:mm:ss）*/
	@Excel(name = "二维码和芯片的验证时间（yyyy-MM-dd HH:mm:ss）", width = 15)
    @ApiModelProperty(value = "二维码和芯片的验证时间（yyyy-MM-dd HH:mm:ss）")
    private String verificationtime;
	/**外键，dps_sy_sample表的sampleNo*/
	@Excel(name = "外键，dps_sy_sample表的sampleNo", width = 15)
    @ApiModelProperty(value = "外键，dps_sy_sample表的sampleNo")
    private String sampleno;
	/**试验开始图片*/
	@Excel(name = "试验开始图片", width = 15)
    @ApiModelProperty(value = "试验开始图片")
    private String beginimage;
	/**试验结束图片*/
	@Excel(name = "试验结束图片", width = 15)
    @ApiModelProperty(value = "试验结束图片")
    private String endimage;
	/**上传状态 默认0，已上传1,此字段终端使用*/
	@Excel(name = "上传状态 默认0，已上传1,此字段终端使用", width = 15)
    @ApiModelProperty(value = "上传状态 默认0，已上传1,此字段终端使用")
    private Integer status;
}
