package com.trtm.iot.system.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/11/15
 * @time 9:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "任务单统计和分页Vo")
public class StatisticsAndPageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private IPage<Bhzrenwudan> page;

    @ApiModelProperty(value = "已完成数量", dataType = "Integer")
    private Integer done;

    @ApiModelProperty(value = "生产中数量", dataType = "Integer")
    private Integer inProduction;

    @ApiModelProperty(value = "已滞后数量", dataType = "Integer")
    private Integer lag;

    @ApiModelProperty(value = "配料未生产数量", dataType = "Integer")
    private Integer ingredientsNotProduced;

    @ApiModelProperty(value = "审核未配料数量", dataType = "Integer")
    private Integer reviewNotIngredients;

    @ApiModelProperty(value = "未审核数量", dataType = "Integer")
    private Integer unreviewed;
}
