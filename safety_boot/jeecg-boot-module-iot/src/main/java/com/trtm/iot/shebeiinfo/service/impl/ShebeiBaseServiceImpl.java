package com.trtm.iot.shebeiinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.shebeiinfo.entity.ShebeiBase;
import com.trtm.iot.shebeiinfo.mapper.ShebeiBaseMapper;
import com.trtm.iot.shebeiinfo.service.IShebeiBaseService;
import com.trtm.sy.syshrwd.entity.Syshrwd;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: shebei_base
 * @Author: jeecg-boot
 * @Date:   2024-11-13
 * @Version: V1.0
 */
@Service
public class ShebeiBaseServiceImpl extends ServiceImpl<ShebeiBaseMapper, ShebeiBase> implements IShebeiBaseService {

    @Override
    public ShebeiBase getByShebeino(String shebeino) {
        QueryWrapper<ShebeiBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino", shebeino);
        return this.getOne(queryWrapper);

    }
}
