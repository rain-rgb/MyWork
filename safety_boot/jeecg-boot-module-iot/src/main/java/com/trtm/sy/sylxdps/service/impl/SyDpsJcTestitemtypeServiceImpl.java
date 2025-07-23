package com.trtm.sy.sylxdps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.sylxdps.mapper.SyDpsJcTestitemtypeMapper;
import com.trtm.sy.sylxdps.service.ISyDpsJcTestitemtypeService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: sy_dps_jc_testitemtype
 * @Author: jeecg-boot
 * @Date: 2023-01-10
 * @Version: V1.0
 */
@Service
public class SyDpsJcTestitemtypeServiceImpl extends ServiceImpl<SyDpsJcTestitemtypeMapper, SyDpsJcTestitemtype> implements ISyDpsJcTestitemtypeService {

    @Autowired
    private SyDpsJcTestitemtypeMapper dpsJcTestitemtypeMapper;


    @Override
    public List<SyDpsJcTestitemtype> queryItemType(String type, Boolean isAll) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getId();
        List<String> roleIds = this.baseMapper.getRoleIds(userId);
        List<String> types = null;
        if (oConvertUtils.isNotEmpty(type)) {
            types = Arrays.asList(type.split(","));
        }
        List<SyDpsJcTestitemtype> list = this.baseMapper.queryItemByType(types, isAll, roleIds);
        List<SyDpsJcTestitemtype> itemTypeList = new ArrayList<>();
//        QueryWrapper<SyDpsJcTestitemtype> wrapper = new QueryWrapper<>();
//        if (type != null) {
//            wrapper.lambda().eq(SyDpsJcTestitemtype::getTitisdel, 0)
//                    .in(SyDpsJcTestitemtype::getTittype, type)
//                    .orderByAsc(SyDpsJcTestitemtype::getTitcode);
//        } else {
//            wrapper.lambda().eq(SyDpsJcTestitemtype::getTitisdel, 0)
//                    .orderByAsc(SyDpsJcTestitemtype::getTitcode);//查询状态正常的，type类型为0的数据
//        }

//        List<SyDpsJcTestitemtype> list = dpsJcTestitemtypeMapper.selectList(wrapper);
        for (SyDpsJcTestitemtype testitemtype : list) {
            if (testitemtype.getTitparentcode() == null || testitemtype.getTitparentcode().length() == 0) {
//                itemTypeList.add(testitemtype);//父idcode为空的话，为1级,加入到itemTypeList中响应前端
                List<SyDpsJcTestitemtype> children = new ArrayList<>();
                list.forEach(m->{
                    if(m.getTitparentcode() != null&&m.getTitparentcode().equals(testitemtype.getTitcode())){
                        children.add(m);
                    }
                });
                testitemtype.setChildren(children);
                itemTypeList.add(testitemtype);
            }
        }
//        for (int i = 0; i < itemTypeList.size(); i++) {//循环查询子集
//            QueryWrapper<SyDpsJcTestitemtype> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(SyDpsJcTestitemtype::getTitisdel, 0)
//                    .eq(SyDpsJcTestitemtype::getTittype, type)
//                    .eq(SyDpsJcTestitemtype::getTitparentcode, itemTypeList.get(i).getTitcode())
//                    .orderByAsc(SyDpsJcTestitemtype::getTitcode);
//            List<SyDpsJcTestitemtype> selectList = dpsJcTestitemtypeMapper.selectList(queryWrapper);
//            itemTypeList.get(i).setChildren(selectList);
//        }
        return itemTypeList;
    }
}
