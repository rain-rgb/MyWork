package com.trtm.iot.rebarComponentMaterial.service.impl;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentMaterial.mapper.RebarComponentMaterialMapper;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: rebar_component_material
 * @Author: jeecg-boot
 * @Date: 2023-06-15
 * @Version: V1.0
 */
@Service
public class RebarComponentMaterialServiceImpl extends ServiceImpl<RebarComponentMaterialMapper, RebarComponentMaterial> implements IRebarComponentMaterialService {
    @Autowired
    private RebarComponentMaterialMapper rebarComponentMaterialMapper;

    @Override
    public String getMaterialNumberByComtenontId(String componentId, String materialId) {
        return rebarComponentMaterialMapper.queryMaterialNumber(componentId, materialId);
    }

    @Override
    public RebarComponentMaterial getComponentMaterial(String componentId, String materialId) {
        return rebarComponentMaterialMapper.getComponentMaterial(componentId, materialId);
    }

    @Override
    public boolean deleteByComponentIdAndMaterial(String componentId, String materialId) {
        return rebarComponentMaterialMapper.deleteByComponentIdAndMaterial(componentId,materialId);
    }

    @Override
    public String getMaterialNumberByMaterialId(String cailiaono) {
        return rebarComponentMaterialMapper.getMaterialNumberByMaterialId(cailiaono);
    }

    @Override
    public List<RebarComponentMaterial> queryMaterialByComponentId(String componentId) {
        return rebarComponentMaterialMapper.queryMaterialByComponentId(componentId);

    }
}
