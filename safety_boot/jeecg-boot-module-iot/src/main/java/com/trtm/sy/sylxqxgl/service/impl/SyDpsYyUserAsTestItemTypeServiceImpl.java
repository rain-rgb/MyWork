package com.trtm.sy.sylxqxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sylxqxgl.entity.SyDpsYyUserAsTestItemType;
import com.trtm.sy.sylxqxgl.entity.SyJlbResponse;
import com.trtm.sy.sylxqxgl.entity.SyLxResponse;
import com.trtm.sy.sylxqxgl.mapper.SyDpsYyUserAsTestItemTypeMapper;
import com.trtm.sy.sylxqxgl.service.SyDpsYyUserAsTestItemTypeService;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyDpsYyUserAsTestItemTypeServiceImpl extends ServiceImpl<SyDpsYyUserAsTestItemTypeMapper, SyDpsYyUserAsTestItemType> implements SyDpsYyUserAsTestItemTypeService {

    @Override
    @Transactional
    public void saveRoleSyLx(List<SyDpsYyUserAsTestItemType> entity) {
        entity.forEach(k -> {
            QueryWrapper<SyDpsYyUserAsTestItemType> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SyDpsYyUserAsTestItemType::getRoleId, k.getRoleId())
                    .eq(SyDpsYyUserAsTestItemType::getTitCode, k.getTitCode());
            SyDpsYyUserAsTestItemType userAsTestItemType = this.getOne(wrapper);
            if (oConvertUtils.isEmpty(userAsTestItemType)) {
                this.save(k);
            } else {
                userAsTestItemType.setTiNos(k.getTiNos());
                this.updateById(userAsTestItemType);
            }
        });
    }

    @Override
    public List<SyLxResponse> getSyLxData() {
        List<SyLxResponse> list = this.baseMapper.getParentSyLx();
        list.forEach(k -> {
            String titCode = k.getTitCode();
            List<SyLxResponse> sons = this.baseMapper.getSonsSyLx(titCode);
//            sons.forEach(s -> {
//                String sTitCode = s.getTitCode();
//                List<SyJlbResponse> tiNos = this.baseMapper.getSyJlbByTitCode(sTitCode);
//                s.setJlb(tiNos);
//            });
//            List<SyJlbResponse> tiNos = this.baseMapper.getSyJlbByTitCode(titCode);
            k.setChildren(sons);
//            k.setJlb(tiNos);
        });
        return list;
    }

    @Override
    public List<String> getSyLxByRoleId(String roleId) {
        List<String> list = this.baseMapper.getAllocatedSy(roleId);
        return list;
    }

    @Override
    public List<SyJlbResponse> getSyLxDataByTitCode(String titCode) {
        List<SyJlbResponse> tiNos = this.baseMapper.getSyJlbByTitCode(titCode);
        return tiNos;
    }

    @Override
    public List<String> getSyLxJlbByRoleId(String roleId, String titCode) {
        List<String> result = new ArrayList();
        List<String> list = this.baseMapper.getSyLxJlbByRoleId(roleId, titCode);
        list.forEach(k -> {
            String[] split = k.split(",");
            for (String jlb : split) {
                result.add(jlb);
            }
        });
        return result;
    }

    @Override
    @Transactional
    public void saveSyLx(SyDpsYyUserAsTestItemType entity) {
        List<String> strings = Arrays.asList(entity.getTitCode().split(","));
        List<String> list = this.baseMapper.getAllocatedSy(entity.getRoleId());
        List<String> save = strings.stream()
                .filter(a -> !list.contains(a)).collect(Collectors.toList());
        List<String> delete = list.stream()
                .filter(b -> !strings.contains(b)).collect(Collectors.toList());
        List<SyDpsYyUserAsTestItemType> models = new ArrayList<>();
        save.forEach(k -> {
            SyDpsYyUserAsTestItemType userAsTestItemType = new SyDpsYyUserAsTestItemType();
            userAsTestItemType.setTitCode(k);
            userAsTestItemType.setRoleId(entity.getRoleId());
            models.add(userAsTestItemType);
        });
        this.saveBatch(models);
        delete.forEach(k -> {
            QueryWrapper<SyDpsYyUserAsTestItemType> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SyDpsYyUserAsTestItemType::getRoleId, entity.getRoleId()).eq(SyDpsYyUserAsTestItemType::getTitCode, k);
            this.remove(wrapper);
        });
    }

    @Override
    public void saveParameter(SyDpsYyUserAsTestItemType entity) {
        UpdateWrapper<SyDpsYyUserAsTestItemType> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(SyDpsYyUserAsTestItemType::getRoleId, entity.getRoleId())
                .eq(SyDpsYyUserAsTestItemType::getTitCode, entity.getTitCode())
                .set(SyDpsYyUserAsTestItemType::getTiNos, entity.getTiNos());
        this.update(wrapper);
    }
}
