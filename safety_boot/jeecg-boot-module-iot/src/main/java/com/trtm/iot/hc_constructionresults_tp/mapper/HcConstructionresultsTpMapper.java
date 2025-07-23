package com.trtm.iot.hc_constructionresults_tp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_constructionresults_tp.entity.HcConstructionresultsTp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 施工成果摊铺
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface HcConstructionresultsTpMapper extends BaseMapper<HcConstructionresultsTp> {

    Integer selectsum(List<String> querySheBeiList);
}
