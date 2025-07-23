package com.trtm.iot.hntbhz.service;

import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.hntbhz.vo.BhzCementDetailRC;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站子表材料信息
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
public interface IBhzCementDetailService extends IService<BhzCementDetail> {

    public List<BhzCementDetail> selectByMainId(String mainId);

    public List querySysCementPageByUserId(Integer pageNo, Integer pageSize);


    /**
     * 根据唯一id查询砼拌合站的材料
     *
     * @param batchNo
     * @return
     */
    List<BhzCementDetail> selectcementlist(String batchNo);

    int updateBy(BhzCementDetail bhzCementDetail);

    List<BhzCementDetailRC> selectcementlists(String batchNo);

    List<BhzCementDetail> selectcementlist1(String batchNo);

    List<Map> selectByBatchList(List<String> list);

    List<BhzCementDetail> selectByBatchNo(String batchNo);
}
