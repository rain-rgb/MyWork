package com.trtm.iot.anquanfxgk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxfjgkBaseMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: anquan_fxfjgk_base
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Service
public class AnquanFxfjgkBaseServiceImpl extends ServiceImpl<AnquanFxfjgkBaseMapper, AnquanFxfjgkBase> implements IAnquanFxfjgkBaseService {
   @Autowired
   private AnquanFxfjgkBaseMapper anquanFxfjgkBaseMapper;

    @Override
    public Integer modifyFxfjgkBaseApprovalStatus(AnquanFxfjgkBase anquanFxfjgkBase) {
        return anquanFxfjgkBaseMapper.modifyFxfjgkBaseApprovalStatus(anquanFxfjgkBase.getStringList(),anquanFxfjgkBase.getApprovalStatus(),anquanFxfjgkBase.getApprovalOpinion());
    }

    @Override
    public AnquanFxfjgkBase getAnquanFxfjgkBaseByManage(String manageLocation) {
        return anquanFxfjgkBaseMapper.getAnquanFxfjgkBaseByManage(manageLocation);
    }
}
