package com.trtm.iot.kongjingbasicinfo.service;

import com.trtm.iot.kongjinganalysisdata.entity.KongjingAnalysisdata;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.kongjingpointsdata.entity.KongjingPointsdata;
import com.trtm.iot.kongjingtongdao.entity.KongjingTongdao;

import java.util.List;
import java.util.Map;

/**
 * @Description: 孔径基本信息数据
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
public interface IKongjingBasicinfoService extends IService<KongjingBasicinfo> {

    int saveMain(KongjingBasicinfo kongjingBasicinfo, List<KongjingTongdao> kongjingTongdaoList, List<KongjingPointsdata> kongjingPointsdataList, List<KongjingAnalysisdata> kongjingAnalysisdataList);

    Map statisticsLevelZB(List<String> shebeilist,Integer date);

    List tongjiKJLevel(List<String> shebeilist, Integer date);
}
