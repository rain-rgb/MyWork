package com.trtm.iot.ycltd.service;

import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 原材料试验详情Service
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
public interface IYclTestDetailService extends IService<YclTestDetail> {
    YclTestDetail getByILN(String iTN);

    YclTestDetail getByILNBHG(String iTN);

    boolean updateJYStateByPici(Integer jianyanstate, String pici);

    boolean updateCJStateByPici(Integer choujianstate, String pici);

    YclTestDetail selectBySampleNumber(String inspectionLotNumber, String sapmleNumber);

    boolean updateBySampleNumber(String storageId, String inspectionLotNumber, String testName, Date samplingTime,
                                 Date testTime, Integer jianyanType, String conclusion, Integer testStatus, String tester,
                                 String report, String createBy, Date createTime, String updateTime, String sampleNumber);
}
