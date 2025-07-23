package com.trtm.iot.yclsl.vo;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * \* @author: zml
 * \* Date: 2022/05/11
 * \* Time: 10:40
 * \* Description:
 * \
 */
@Data
@ApiModel(value="wzycljinchanggbPages对象", description="物资进场主表")
public class wzycljinchanggbPages {
   private Wzycljinchanggb wzycljinchanggb;
   private Wzliaocang wzliaocang;
   private Wzgongyingshang wzgongyingshang;
   private Wzcailiaonamedict wzcailiaonamedict;
}
