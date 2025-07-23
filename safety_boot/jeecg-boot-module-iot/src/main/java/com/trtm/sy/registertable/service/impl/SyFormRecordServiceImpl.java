package com.trtm.sy.registertable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.registertable.mapper.SyFormRecordMapper;
import com.trtm.sy.registertable.model.BaseForm;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyFormRecord;
import com.trtm.sy.registertable.service.SyFormRecordService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
//@DS("#header.dataSourcePoolName")
public class SyFormRecordServiceImpl extends ServiceImpl<SyFormRecordMapper, SyFormRecord> implements SyFormRecordService {


    @Override
    public SyForm getRecordFormByBh(String bh , String sampleId, String type) {

        //根据报告编号获取对应的 表格结构
        QueryWrapper<SyFormRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SyFormRecord::getReportNumber, bh)
                .orderByAsc(SyFormRecord::getId);
        List<SyFormRecord> syFormRecord = this.list(wrapper);

        Collection<List<SyFormRecord>> values = syFormRecord
                .stream()
                .sorted(Comparator.comparingInt(SyFormRecord::getId))
                .collect(
                        Collectors.groupingBy(SyFormRecord::getGroup, LinkedHashMap::new, Collectors.toList())
                ).values();

        List<List<SyFormRecord>> syFormRecordLists = new ArrayList<>(values);

        SyForm forms = new SyForm();
        List<List<BaseForm>> RecordFormLists = new CopyOnWriteArrayList<>();

        syFormRecordLists.forEach(k -> {
            List<BaseForm> list = new CopyOnWriteArrayList<>();
            k.forEach(n -> {
                BaseForm LabelForms = new BaseForm(n.getId(), n.getKey(), n.getValue(), n.getInputType(), n.getColspan(), n.getRowspan(), n.getStyle(), n.getDictType(), n.getRequired(), n.getLength(), n.getAbeam(), n.getEmpty());
                list.add(LabelForms);
            });
            RecordFormLists.add(list);
        });

        forms.setBaseFormLists(RecordFormLists);
        forms.set标题(bh);
        //forms.set检测单位(new TokenContext().getToken().getTenantName());
        forms.set编号("");
        forms.set复核人("");
        forms.set监理单位("");
        forms.set施工单位("");
        forms.set日期("");

        forms.setType("");
        forms.setSampleId("");
        return forms;

    }




}
