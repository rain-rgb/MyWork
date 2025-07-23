package com.trtm.iot.lqbhz.vo;

import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value="bhz_cement_basesar对象", description="沥青超标查询带处置信息")
public class BhzlqBaseCailiao {
    @ApiModelProperty(value = "沥青主表")
    private BhzLqBases bhzLqBases;
    /**设备编号*/
    @ApiModelProperty(value = "设备编号")
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    private String shebeibianhao;
    /**唯一标识*/
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private String guid;
    /**超标状态：0为未处理，10为施工方已处理，20为监理方已处理*/
    @Excel(name = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理", width = 15)
    @ApiModelProperty(value = "超标状态：0为未处理，10为施工方已处理，20为监理方已处理")
    private Integer overproofStatus;
    @ApiModelProperty(value = "拌合站处置信息表")
    private BhzCementOverHandler bhzCementOverHandler;


}
