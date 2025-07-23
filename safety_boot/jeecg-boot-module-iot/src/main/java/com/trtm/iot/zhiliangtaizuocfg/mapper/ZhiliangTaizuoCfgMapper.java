package com.trtm.iot.zhiliangtaizuocfg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 制梁台座配置表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
public interface ZhiliangTaizuoCfgMapper extends BaseMapper<ZhiliangTaizuoCfg> {

    ZhiliangTaizuoCfg selectzltaizuo(String taizuono);
}
