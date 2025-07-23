package com.trtm.iot.anquanfxgk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjiaDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: anquan_fxfjgk_pingjia_detail
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
public interface AnquanFxfjgkPingjiaDetailMapper extends BaseMapper<AnquanFxfjgkPingjiaDetail> {
    /**根据主表删除对应的附表数据
     * @param parentId
     * @return
     */
    Integer deleteAnquanFxfjgkPingjiaDetailByParent(@Param("parentId") String parentId);

    /**根据主表关联字段获取对应的附表数据
     * @param parentId
     * @return
     */
    List<AnquanFxfjgkPingjiaDetail> getAnquanFxfjgkPingjiaDetailByParent(@Param("parentId") String parentId);
}
