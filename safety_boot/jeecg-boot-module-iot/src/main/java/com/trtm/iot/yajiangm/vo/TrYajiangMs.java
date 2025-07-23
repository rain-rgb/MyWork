package com.trtm.iot.yajiangm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 压浆主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Data
@TableName("tr_yajiang_m")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tr_yajiang_m对象", description="压浆主表信息")
public class TrYajiangMs implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主建id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主建id")
    private Integer id;
	/**32位全球唯一码*/
	@Excel(name = "32位全球唯一码", width = 15)
    @ApiModelProperty(value = "32位全球唯一码")
    private String syjid;
	/**施工单位*/
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private String sgdw;
	/**监理单位*/
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private String jldw;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private String htbh;
	/**工程名称*/
	@Excel(name = "工程名称", width = 15)
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
	/**压浆时间*/
	@Excel(name = "压浆时间", width = 15)
    @ApiModelProperty(value = "压浆时间")
    private String yjsj;
	/**桩号及部位*/
	@Excel(name = "桩号及部位", width = 15)
    @ApiModelProperty(value = "桩号及部位")
    private String zhbw;
	/**施工部位*/
	@Excel(name = "施工部位", width = 15)
    @ApiModelProperty(value = "施工部位")
    private String sgbw;
	/**构件结构*/
	@Excel(name = "构件结构", width = 15)
    @ApiModelProperty(value = "构件结构")
    private String gjjg;
	/**构件编号及长度*/
	@Excel(name = "构件编号及长度", width = 15)
    @ApiModelProperty(value = "构件编号及长度")
    private String gjbh;
	/**气温*/
	@Excel(name = "气温", width = 15)
    @ApiModelProperty(value = "气温")
    private String qw;
	/**掺减水剂量*/
	@Excel(name = "掺减水剂量", width = 15)
    @ApiModelProperty(value = "掺减水剂量")
    private String cjsjl;
	/**掺膨胀剂量*/
	@Excel(name = "掺膨胀剂量", width = 15)
    @ApiModelProperty(value = "掺膨胀剂量")
    private String cpzjl;
	/**水温*/
	@Excel(name = "水温", width = 15)
    @ApiModelProperty(value = "水温")
    private String sw;
	/**水胶比*/
	@Excel(name = "水胶比", width = 15)
    @ApiModelProperty(value = "水胶比")
    private String shuijiaobi;
	/**构件压浆水泥用量*/
	@Excel(name = "构件压浆水泥用量", width = 15)
    @ApiModelProperty(value = "构件压浆水泥用量")
    private String snyl;
	/**压浆温度*/
	@Excel(name = "压浆温度", width = 15)
    @ApiModelProperty(value = "压浆温度")
    private String yjwd;
	/**泌水率*/
	@Excel(name = "泌水率", width = 15)
    @ApiModelProperty(value = "泌水率")
    private String msl;
	/**备用*/
	@Excel(name = "备用", width = 15)
    @ApiModelProperty(value = "备用")
    private String beiyong;
	/**压浆设备编号*/
	@Excel(name = "压浆设备编号", width = 15)
    @ApiModelProperty(value = "压浆设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String yjsbbh;
	/**梁板类型*/
	@Excel(name = "梁板类型", width = 15)
    @ApiModelProperty(value = "梁板类型")
    private String lblx;
	/**梁号*/
	@Excel(name = "梁号", width = 15)
    @ApiModelProperty(value = "梁号")
    private String lianghao;
	/**张拉时间*/
	@Excel(name = "张拉时间", width = 15)
    @ApiModelProperty(value = "张拉时间")
    private String zlsj;
	/**压浆剂*/
	@Excel(name = "压浆剂", width = 15)
    @ApiModelProperty(value = "压浆剂")
    private String yajiangji;
	/**水泥名称*/
	@Excel(name = "水泥名称", width = 15)
    @ApiModelProperty(value = "水泥名称")
    private String snmc;
	/**孔道数*/
	@Excel(name = "孔道数", width = 15)
    @ApiModelProperty(value = "孔道数")
    private String kongdaoshu;
	/**压浆方向*/
	@Excel(name = "压浆方向", width = 15)
    @ApiModelProperty(value = "压浆方向")
    private String yajiangfang;
	/**压浆步骤*/
	@Excel(name = "压浆步骤", width = 15)
    @ApiModelProperty(value = "压浆步骤")
    private String yajiangbuzh;
	/**压浆过程*/
	@Excel(name = "压浆过程", width = 15)
    @ApiModelProperty(value = "压浆过程")
    private String yajiangguoc;
	/**初始流动速度*/
	@Excel(name = "初始流动速度", width = 15)
    @ApiModelProperty(value = "初始流动速度")
    private String chushisudu;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String memo;
	/**liudongdu*/
	@Excel(name = "liudongdu", width = 15)
    @ApiModelProperty(value = "liudongdu")
    private String liudongdu;
	/**完成状态(0为未完成,状态为1代表*/
	@Excel(name = "完成状态(0为未完成,状态为1代表", width = 15)
    @ApiModelProperty(value = "完成状态(0为未完成,状态为1代表")
    private String status;
	/**32位全球唯一码*/
	@Excel(name = "32位全球唯一码", width = 15)
    @ApiModelProperty(value = "32位全球唯一码")
    private String guid;
	/**压浆任务单下发时任务单id（由设备返回）*/
	@Excel(name = "压浆任务单下发时任务单id（由设备返回）", width = 15)
    @ApiModelProperty(value = "压浆任务单下发时任务单id（由设备返回）")
    private String uuid;
	/**issend*/
	@Excel(name = "issend", width = 15)
    @ApiModelProperty(value = "issend")
    private String issend;
    /**是否合格*/
    @Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格")
    private String hege;
}
