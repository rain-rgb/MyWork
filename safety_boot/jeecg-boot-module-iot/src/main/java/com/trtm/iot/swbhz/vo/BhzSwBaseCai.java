package com.trtm.iot.swbhz.vo;

import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
@Data
public class BhzSwBaseCai {
    @ApiModelProperty(value = "水稳主表")
    private BhzSwBases bhzSwBases;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @Dict(dictTable = "shebei_info", dicText = "sbname", dicCode = "sbjno")
    @ApiModelProperty(value = "设备编号")
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
