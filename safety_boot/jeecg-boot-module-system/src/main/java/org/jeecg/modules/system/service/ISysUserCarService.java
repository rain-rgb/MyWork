package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysUserCar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 司机用户关联车牌
 * @Author: jeecg-boot
 * @Date:   2021-06-25
 * @Version: V1.0
 */
public interface ISysUserCarService extends IService<SysUserCar> {

    List<SysUserCar> selectsysusercar(String userid);
}
