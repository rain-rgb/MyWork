package com.trtm.sy.registertable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.registertable.mapper.SyFormReportMapper;
import com.trtm.sy.registertable.model.BaseForm;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyFormReport;
import com.trtm.sy.registertable.service.SyFormReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
//@DS("#header.dataSourcePoolName")
public class SyFormReportServiceImpl extends ServiceImpl<SyFormReportMapper, SyFormReport> implements SyFormReportService {

    @Override
    public SyForm getReportFormByBh(String reportNumber) {

        SyForm result = new SyForm();

        List<SyFormReport> syFormReport = this.list(
                Wrappers.lambdaQuery(new SyFormReport())
                        .eq(SyFormReport::getReportNumber, reportNumber)
                        .eq(SyFormReport::getDisplay, 1)
        );
        if (CollectionUtil.isEmpty(syFormReport)) {
            return result;
        }

        //将结构分组后按照ID排序 保证结构的顺序是正确的 不然返回给前端的结构会出现错位
        Collection<List<SyFormReport>> values = syFormReport
                .stream()
                .sorted(Comparator.comparingInt(SyFormReport::getId))
                .collect(Collectors.groupingBy(SyFormReport::getGroup, LinkedHashMap::new, Collectors.toList()))
                .values();
        //将分组和排序后的Collection转化为 List


        List<List<SyFormReport>> groupList = new ArrayList<>(values);
        List<List<BaseForm>> BaseFormsLists = new CopyOnWriteArrayList<>();
        groupList.forEach(n -> {
            List<BaseForm> baseFormsList = new CopyOnWriteArrayList<>();
            n.forEach(k -> {
                baseFormsList.add(new BaseForm(
                        k.getId(),
                        k.getKey(),
                        k.getLabel(),
                        k.getInputType(),
                        k.getColspan(),
                        k.getRowspan(),
                        k.getStyle(),
                        k.getDictType(), null, 0, 0, 0));
            });
            BaseFormsLists.add(baseFormsList);
        });
        result.setBaseFormLists(BaseFormsLists);
        return result;
    }
}
