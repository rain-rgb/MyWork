package com.trtm.sy.tableform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.sylxqxgl.service.SyDpsYyUserAsTestItemTypeService;
import com.trtm.sy.tableform.mapper.TableFormMapper;
import com.trtm.sy.tableform.model.TableForm;
import com.trtm.sy.tableform.service.TableFormService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TableFormServiceImpl extends ServiceImpl<TableFormMapper, TableForm> implements TableFormService {

    @Autowired
    private SyDpsYyUserAsTestItemTypeService userAsTestItemTypeService;

    @Override
    public List<SyDpsJcTestitemtype> getExperimentType(String roleId) {
        List<String> titCodes = userAsTestItemTypeService.getSyLxByRoleId(roleId);
        if (oConvertUtils.isEmpty(titCodes)) {
            return null;
        }
        List<SyDpsJcTestitemtype> itemTypes = this.baseMapper.selectParentItemType(titCodes);
        itemTypes.forEach(k -> {
            List<SyDpsJcTestitemtype> sonsItemTypes = this.baseMapper.selectSonsItemType(k.getTitcode());
            k.setChildren(sonsItemTypes);
        });
        return itemTypes;
    }

    @Override
    public List<TableForm> getExperimentChridList(String roleId, String titcode) {
        String titcodes = this.baseMapper.getExperimentChridList(roleId, titcode);
        if (oConvertUtils.isEmpty(titcodes)) {
            return null;
        }
        List<String> codelist = Arrays.asList(titcodes.split(","));
        List<TableForm> tableForms = new ArrayList<>();
        codelist.forEach(k -> {
            TableForm tableForm  = this.baseMapper.selectTableForm(k);
            tableForms.add(tableForm);
        });
        return tableForms;
    }
}
