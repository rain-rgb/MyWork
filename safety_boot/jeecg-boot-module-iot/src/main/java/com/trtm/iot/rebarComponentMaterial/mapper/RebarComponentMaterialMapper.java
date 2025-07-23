package com.trtm.iot.rebarComponentMaterial.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: rebar_component_material
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface RebarComponentMaterialMapper extends BaseMapper<RebarComponentMaterial> {

    String queryMaterialNumber(@Param("componentId") String componentId,@Param("materialId") String MaterialId);

    RebarComponentMaterial getComponentMaterial(@Param("componentId") String componentId,@Param("materialId") String MaterialId);

    boolean deleteByComponentIdAndMaterial(@Param("componentId") String componentId,@Param("materialId") String materialId);

    boolean deleteByComponentId(@Param("componentId") String componentId);

    String getMaterialNumberByMaterialId(@Param("cailiaono") String cailiaono);

    List<RebarComponentMaterial> queryMaterialByComponentId(@Param("componentId") String componentId);
}
