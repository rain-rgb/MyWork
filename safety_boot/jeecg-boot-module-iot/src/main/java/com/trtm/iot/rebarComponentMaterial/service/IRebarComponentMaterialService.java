package com.trtm.iot.rebarComponentMaterial.service;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: rebar_component_material
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
public interface IRebarComponentMaterialService extends IService<RebarComponentMaterial> {

    String getMaterialNumberByComtenontId(String componentId,String materialId);

    RebarComponentMaterial getComponentMaterial(String componentId, String materialId);

    boolean deleteByComponentIdAndMaterial(String componentId, String materialId);

    String getMaterialNumberByMaterialId(String cailiaono);

    List<RebarComponentMaterial> queryMaterialByComponentId(String componentId);
}
