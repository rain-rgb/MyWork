package com.trtm.iot.zhanglam.service;

import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 张拉信息主表
 * @Author: jeecg-boot
 * @Date: 2021-08-31
 * @Version: V1.0
 */
public interface ITrZhanglaMService extends IService<TrZhanglaM> {

    List<TrZhanglaM> selectmList(String syjid);

    List<Map<String, Object>> countList();

    int updateoverproofStatus(Integer overproofStatus, String syjid);

    List<TrZhanglaXxbMStatistics> findBysyjid(String syjid);


    List<TrZhanglaM> selectmnotList(String syjid);

    List<TrZhanglaM> selectListbl(String shebeibianhao);

    void updateSbjById(String sbjno,Integer id);
}
