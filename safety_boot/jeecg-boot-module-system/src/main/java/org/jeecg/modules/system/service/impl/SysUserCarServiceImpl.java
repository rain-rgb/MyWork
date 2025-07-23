package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import org.jeecg.modules.system.entity.SysUserCar;
import org.jeecg.modules.system.mapper.SysUserCarMapper;
import org.jeecg.modules.system.service.ISysUserCarService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 司机用户关联车牌
 * @Author: jeecg-boot
 * @Date:   2021-06-25
 * @Version: V1.0
 */
@Service
public class SysUserCarServiceImpl extends ServiceImpl<SysUserCarMapper, SysUserCar> implements ISysUserCarService {
    @Override
    public List<SysUserCar> selectsysusercar(String userid) {
        try {
            QueryWrapper<SysUserCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
