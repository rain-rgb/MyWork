package com.trtm.sy.registertable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.api.tool.VersionUtil;
import com.trtm.sy.enumutil.DelFlagEnum;
import com.trtm.sy.enumutil.YesOrNotEnum;
import com.trtm.sy.registertable.mapper.EsSyFormMapper;
import com.trtm.sy.registertable.mapper.SyTableFormMapper;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyTableForm;
import com.trtm.sy.registertable.model.request.SyTableFormRequest;
import com.trtm.sy.registertable.service.SyTableFormService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import com.trtm.api.tool.VelocityUtils;


import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
//@DS("#header.dataSourcePoolName")
public class SyTableFormServiceImpl extends ServiceImpl<SyTableFormMapper, SyTableForm> implements SyTableFormService {

    @Autowired
    private EsSyFormMapper mapper;

    @Override
    public IPage<SyTableForm> queryFormList(SyTableFormRequest request) {
        Page<SyTableForm> page = new Page<>(request.getPageNo(), request.getPageSize());
        QueryWrapper<SyTableForm> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(request.getJlbbm()), SyTableForm::getJlbbm, request.getJlbbm())
                .like(StringUtils.isNotEmpty(request.getJlbmc()), SyTableForm::getJlbmc, request.getJlbmc())
                .eq(StringUtils.isNotEmpty(request.getType()), SyTableForm::getType, request.getType())
                .orderByDesc(SyTableForm::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void saveTableData(SyForm entity) {
        long start = System.currentTimeMillis();
        log.info("——————————————————开始保存数据到ES—————————— 开始时间：  " + start);
        entity.setId(entity.getSampleId() + entity.getReportNumber());
         mapper.save(entity);
        long end = System.currentTimeMillis();
        log.info("——————————————————结束保存数据到ES—————————— 结束时间：  " + end);
    }

    @Override
    public SyForm getTableData(String sampleId, String tableNumber) {
        long start = System.currentTimeMillis();
        log.info("——————————————————查询数据—————————— 开始时间：  " + start);
        Optional<SyForm> syFormOptional = mapper.findById(sampleId + tableNumber);
        SyForm syForm = syFormOptional.orElse(null);
        long end = System.currentTimeMillis();
        log.info("——————————————————查询数据—————————— 结束时间：  " + end);
        return syForm;
    }

    @Override
    public boolean addSyTableForm(SyTableForm request) {
        syjlbmcCodeCheck(request);
        request.setDelFlag(DelFlagEnum.N.getCode());
        return this.save(request);
    }

    @Override
    public boolean deleteSyTableForm(SyTableForm request) {
        SyTableForm getVersion = this.getById(request.getId());
        VersionUtil.checkVersion(request.getVersion(), getVersion.getVersion());
        return this.removeById(request.getId());
    }

    /**
     * 记录表编码为空检查
     *
     * @param request 入参
     */
    public void syjlbmcCodeCheck(SyTableForm request) {
        // 根据记录表编号查询记录表集合
        List<SyTableForm> list = this.list(Wrappers.lambdaQuery(new SyTableForm())
                .eq(StrUtil.isNotBlank(request.getJlbbm()), SyTableForm::getJlbbm, request.getJlbbm())
                .eq(SyTableForm::getDelFlag, YesOrNotEnum.N));
        if (CollectionUtil.isNotEmpty(list)) {
            throw new JeecgBootException("记录表编号已经存在!");
        }
    }


}
