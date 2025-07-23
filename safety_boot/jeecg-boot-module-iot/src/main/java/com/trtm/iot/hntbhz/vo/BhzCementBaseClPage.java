package com.trtm.iot.hntbhz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="bhz_cement_baseover对象", description="拌合站超标查询带处置信息")
public class BhzCementBaseClPage {
    @ApiModelProperty(value = "拌合站主表")
    private BhzCementBase bhzCementBase;
    @Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
    private String shebeiNo;
    @Excel(name = "唯一ID", width = 15)
    @ApiModelProperty(value = "唯一ID")
    private String batchNo;
    /**超标状态：0为未处理，10为施工方已处理，20为监理方已处理*/
    @Excel(name = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理", width = 15)
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理")
    private Integer overproofStatus;
    @ApiModelProperty(value = "拌合站处置信息表")
    private BhzCementOverHandler bhzCementOverHandler;
}
