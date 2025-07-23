package com.trtm.iot.zhilianggongxu.service;

import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhiliangtaizuocfg.vo.tjVO;

import java.util.List;

/**
 * @Description: 制梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-22
 * @Version: V1.0
 */
public interface IZhiliangGongxuService extends IService<ZhiliangGongxu> {

    List<ZhiliangGongxu> selectByMainId(String id);

    public boolean updateones(String uuid);

    ZhiliangGongxu selectuuid(String uuid, int xuhao);

    List<ZhiliangGongxu> selectgongxu(String uuid);

    List<ZhiliangGongxu> selectgongxuPinmin(String uuid);

    List<ZhiliangGongxu> selectuuidlist(String uuid, Integer xuhao);

}
