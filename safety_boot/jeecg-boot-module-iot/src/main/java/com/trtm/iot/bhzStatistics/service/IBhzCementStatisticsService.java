package com.trtm.iot.bhzStatistics.service;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.bhzStatistics.controller.BhzCementStatisticsController;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站统计表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface IBhzCementStatisticsService extends IService<BhzCementStatistics> {

    BhzCementStatistics selectlimit(Date datanyr1,String projectName,String poureLocation,String jobLocation,String formulaNo,String strengthRank,String shebeiNo);

    List<BhzCementStatistics> selectbhzstaList(Integer curid, Integer istongji, Date datanyr1);

    int updateistongji(int id, Integer istongji);

    boolean updatestatisticsone(Integer id,Integer allsum ,Double sumfl,Integer cjsum,Integer allsums,Integer zjsum,Integer gjsum);

    List<Map<String, Object>> countList();

    /**
     * 添加一对多
     *
     */
    public void saveMain(BhzCementStatistics bhzCementStatistics, List<BhzCementDetailStatistics> bhzCementDetailStatisticsList);

    /**
     * 根据时间以及设备列表查询对应的统计数据
     * @param curid
     * @param shebeiNo
     * @param datanyr1
     * @return
     */
    List<BhzCementStatistics> selectBhzStatisticsList(Integer curid, List<String> shebeiNo, Date datanyr1);

    List<BhzCementStatistics> selectBhzStatisticsLists(Integer curid, List<String> shebeiNo);

    List<Map<Object,String>> selectOrgCode(String orgCode);

    List<Map<String, Object>> countLists(String orgCategorys, String orgCode, Integer date);

    List<BhzCementStatistics> selectjbzzones(String shebeino,Integer curid);

    List<BhzCementStatistics> selectbydays(String shebeiNo);

    BhzCementStatistics findRenwudanzs(List<String> sbs);

    Map getTongJiList(List<String> shebeilist, Integer time);

    List<BhzCementStatistics> getTongJiLists(String sysDepartOrgcode, Integer time);
}
