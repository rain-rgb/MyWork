package com.trtm.iot.devicepipepilehistorydataone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.mapper.BhzCementStatisticsMapper;
import com.trtm.iot.bys.mapper.BysRealMapper;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.trtm.iot.deviceMixpileHistorydataOne.mapper.DeviceMixpileHistorydataOneMapper;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiDto;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiVo;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.devicepipepilehistorydataone.vo.DevicePipepileReportVo;
import com.trtm.iot.devicepipepilehistorydataone.vo.PartVo;
import com.trtm.iot.devicepipepilehistorydatapart.entity.DevicePipepileHistorydataPart;
import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.trtm.iot.gongyistatistic.mapper.GongyiStatisticMapper;
import com.trtm.iot.gongyistatistic.service.IGongyiStatisticService;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.trtm.iot.hc_constructionresults.mapper.HcConstructionresultsMapper;
import com.trtm.iot.hc_constructionresults_ny.mapper.HcConstructionresultsNyMapper;
import com.trtm.iot.hc_constructionresults_tp.mapper.HcConstructionresultsTpMapper;
import com.trtm.iot.hc_machine.entity.HcMachine;
import com.trtm.iot.hc_machine.mapper.HcMachineMapper;
import com.trtm.iot.hc_project.mapper.HcProjectMapper;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.mapper.HcTransportrecordsMapper;
import com.trtm.iot.hc_truck.mapper.HcTruckMapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import com.trtm.iot.lqbhz.mapper.BhzLqBasesMapper;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.mapper.BhzLqStatisticsMapper;
import com.trtm.iot.swbhzStatistics.entity.BhzSwStatistics;
import com.trtm.iot.swbhzStatistics.mapper.BhzSwStatisticsMapper;
import com.trtm.iot.syj.mapper.TSyjzbMapper;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;
import com.trtm.iot.trgangjinbhcm.mapper.TrGangjinbhcMMapper;
import com.trtm.iot.yajiangs.mapper.TrYajiangSMapper;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.trtm.iot.yj.mapper.TrYajiangMapper;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.mapper.TrZhanglaMMapper;
import com.trtm.iot.zhanglaxxb.mapper.TrZhanglaXxbMapper;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: device_pipepile_historydata_one
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Service
@Slf4j
public class DevicePipepileHistorydataOneServiceImpl extends ServiceImpl<DevicePipepileHistorydataOneMapper, DevicePipepileHistorydataOne> implements IDevicePipepileHistorydataOneService {

    @Autowired
    DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    private TrZhanglaMMapper trZhanglaMMapper;
    @Autowired
    private TrYajiangSMapper trYajiangSMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;
    @Autowired
    private TrYajiangMapper mapper;
    @Autowired
    private BhzCementStatisticsMapper bhzCementStatisticsMapper;
    @Autowired
    private BhzLqStatisticsMapper bhzLqStatisticsMapper;
    @Autowired
    private BhzSwStatisticsMapper bhzSwStatisticsMapper;
    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private TrZhanglaXxbMapper zhanglaXxbMapper;
    @Autowired
    private TSyjzbMapper syjzbMapper;
    @Autowired
    private BhzLqBasesMapper lqBasesMapper;
    @Autowired
    private BysRealMapper bysRealMapper;
    @Autowired
    private HcProjectMapper projectMapper;
    @Autowired
    private HcConstructionresultsMapper constructionresultsMapper;
    @Autowired
    private HcConstructionresultsTpMapper constructionresultsTpMapper;
    @Autowired
    private HcConstructionresultsNyMapper constructionresultsNyMapper;
    @Autowired
    private HcTruckMapper truckMapper;
    @Autowired
    private HcTransportrecordsMapper transportrecordsMapper;
    @Autowired
    GongyiStatisticMapper gongyiStatisticMapper;
    @Autowired
    private DeviceMixpileHistorydataOneMapper mixpileHistorydataOneMapper;
    @Autowired
    private TrGangjinbhcMMapper gangjinbhcMMapper;
    @Autowired
    private HcMachineMapper machineMapper;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IJtwbsService jtwbsService;
    @Autowired
    private ITokenrecodeService tokenrecodeService;
    @Autowired
    private IGongyiStatisticService gongyiStatisticService;


    @Override
    public List<PartVo> selectWorksta(String shebeino, String pileNo) {
        return devicePipepileHistorydataOneMapper.selectWorksta(shebeino, pileNo);
    }

    @Override
    public List<DevicePipepileReportVo> selectDeviceVo(String shebeino, String pileNo) {
        return devicePipepileHistorydataOneMapper.selectDeviceVo(shebeino, pileNo);
    }

    @Override
    public List<DevicePipepileHistorydataPart> selectbysp(String shebeino, String pileNo) {
        return devicePipepileHistorydataOneMapper.selectbysp(shebeino, pileNo);
    }

    @Override
    public List<DevicePipepileHistorydataOne> selectLists(String shebeino, Integer id) {
        return devicePipepileHistorydataOneMapper.selectLists(shebeino, id);
    }

    @Override
    public List<DevicePipepileHistorydataOne> selectjbzzones(Integer curid, int i) {
        return devicePipepileHistorydataOneMapper.selectjbzzones(curid, i);
    }

    @Override
    public List<Map<String, Object>> queryWarningStatus(String orgCode) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<String> sheBs = null;
        if (orgCode != null) {
            sheBs = shebeiInfoMapper.findSheBeis(orgCode);
        } else {
            sheBs = shebeiInfoMapper.findSBeis();
        }
        LambdaQueryWrapper<DevicePipepileHistorydataOne> queryWrapper = new QueryWrapper<DevicePipepileHistorydataOne>().lambda()
                .eq(DevicePipepileHistorydataOne::getChaobiaodengji, "1")
                .in(DevicePipepileHistorydataOne::getShebeino, sheBs);
        Integer dphoCount = devicePipepileHistorydataOneMapper.selectCount(queryWrapper);//查询DevicePipepileHistorydataOne超标等级不合格的数量
        Integer dphoZongShu = devicePipepileHistorydataOneMapper.findXiangMuZS(sheBs);//查询总数
        String dphoYuJingLv = getPercentStr(dphoCount, dphoZongShu);//预警率=不合格/总数
        LambdaQueryWrapper<DevicePipepileHistorydataOne> wrapper = new QueryWrapper<DevicePipepileHistorydataOne>().lambda()
                .eq(DevicePipepileHistorydataOne::getOverproofStatus, 20)
                .in(DevicePipepileHistorydataOne::getShebeino, sheBs);
        Integer dphoOverStatusCount = devicePipepileHistorydataOneMapper.selectCount(wrapper);//查询overstatus为20的数量
        String dphoBiHeLv = "";
        if (dphoCount == 0) {
            dphoBiHeLv = "-%";
        } else {
            dphoBiHeLv = getPercentStr(dphoOverStatusCount, dphoCount);//闭合率=overstatus为20的数量/预警数量
        }
        Map<String, Object> map1 = new HashMap<>();
        LambdaQueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<TrZhanglaM>().lambda()
                .in(TrZhanglaM::getShebeibianhao, sheBs)
                .and(qr -> qr.eq(TrZhanglaM::getHege, "0").or().eq(TrZhanglaM::getHege, "不合格"));
        Integer zhangLaCount = trZhanglaMMapper.selectCount(queryWrapper1); //查询TrZhanglaM超标等级不合格的数量
        Integer zhangLaZongShu = trZhanglaMMapper.findXiangMuZS(sheBs);//查询总数
        String zhangLaYuJingLv = getPercentStr(zhangLaCount, zhangLaZongShu);//预警率=不合格/总数
        LambdaQueryWrapper<TrZhanglaM> wrapper1 = new QueryWrapper<TrZhanglaM>().lambda().eq(TrZhanglaM::getOverproofStatus, 20)
                .in(TrZhanglaM::getShebeibianhao, sheBs);
        Integer zlOverStatusCount = trZhanglaMMapper.selectCount(wrapper1);//查询overstatus为20的数量
        String zlBiHeLv = "";
        if (zhangLaCount == 0) {
            zlBiHeLv = "-%";
        } else {
            zlBiHeLv = getPercentStr(zlOverStatusCount, zhangLaCount);//闭合率=overstatus为20的数量/预警数量
        }
        Map<String, Object> map2 = new HashMap<>();
        Integer yjCount = trYajiangSMapper.findCount(sheBs);
        Integer yjZongShu = trYajiangSMapper.findXiangMuZSs(sheBs);//查询总数
        String yjYuJingLv = getPercentStr(yjCount, yjZongShu);//预警率=不合格/总数
        Integer yjOverStatusCount = trYajiangSMapper.selectCo(sheBs);
        String yjBiHeLv = "";
        if (yjCount == 0) {
            yjBiHeLv = "-%";
        } else {
            yjBiHeLv = getPercentStr(yjOverStatusCount, yjCount);//闭合率=overstatus为20的数量/预警数量
        }
        map.put("yujingcishu", dphoCount);
        map.put("yujinglv", dphoYuJingLv);
        map.put("bihelv", dphoBiHeLv);
        map.put("mingcheng", "软基施工");
        map.put("type", 1);
        map1.put("yujingcishu", zhangLaCount);
        map1.put("yujinglv", zhangLaYuJingLv);
        map1.put("bihelv", zlBiHeLv);
        map1.put("mingcheng", "智能张拉");
        map1.put("type", 2);
        map2.put("yujingcishu", yjCount);
        map2.put("yujinglv", yjYuJingLv);
        map2.put("bihelv", yjBiHeLv);
        map2.put("mingcheng", "智能压浆");
        map2.put("type", 3);
        list.add(map);
        list.add(map1);
        list.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("yujingcishu", 0);
        map3.put("yujinglv", "0.00%");
        map3.put("bihelv", "-%");
        map3.put("mingcheng", "路基压实");
        map3.put("type", 4);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("yujingcishu", 0);
        map4.put("yujinglv", "0.00%");
        map4.put("bihelv", "-%");
        map4.put("mingcheng", "水稳摊铺碾压");
        map4.put("type", 5);
        Map<String, Object> map5 = new HashMap<>();
        map5.put("yujingcishu", 0);
        map5.put("yujinglv", "0.00%");
        map5.put("bihelv", "-%");
        map5.put("mingcheng", "沥青摊铺碾压");
        map5.put("type", 6);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        log.info("软基施工预警次数{}+预警率{}+闭合率{}", dphoCount, dphoYuJingLv, dphoBiHeLv);
        log.info("张拉预警次数{}+预警率{}+闭合率{}", zhangLaCount, zhangLaYuJingLv, zlBiHeLv);
        log.info("压浆预警次数{}+预警率{}+闭合率{}", yjCount, yjYuJingLv, yjBiHeLv);
        return list;
    }


    /**
     * 二级页面，1表示软基，2表示张拉，3表示压浆
     *
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> findWarningStatus(Integer type, String orgCode) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = null;
        if (orgCode != null) {
            listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
        } else {
            listMap = devicePipepileHistorydataOneMapper.selectSBNo();
        }
        if (null != type && type == 1) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {

                    zongShu = devicePipepileHistorydataOneMapper.findXiangMuZS(sbs);//查询所有设备在表中存在的数量
                    yuJingS = devicePipepileHistorydataOneMapper.findYuJingS(sbs);//根据所有的设备查询不合格的数量
                    biHeS = devicePipepileHistorydataOneMapper.findBiHeS(sbs);//根据所有的设备查询状态值20 的数量
                }
                String gongsi = devicePipepileHistorydataOneMapper.findGsMc(orgCode1);
                String yujinglv = getPercentStr(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(biHeS, yuJingS);//闭合率
                }
                map1.put("gongsi", gongsi);
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 2) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = trZhanglaMMapper.findXiangMuZS(sbs);//查询所有设备在表中存在的数量
                    yuJingS = trZhanglaMMapper.findYuJingS(sbs);//根据所有的设备查询不合格的数量
                    biHeS = trZhanglaMMapper.findBiHeS(sbs);//根据所有的设备查询状态值20 的数量
                }
                String gongsi = devicePipepileHistorydataOneMapper.findGsMc(orgCode1);
                String yujinglv = getPercentStr(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(biHeS, yuJingS);//闭合率
                }
                map1.put("gongsi", gongsi);
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 3) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                List<String> ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (ids != null && ids.size() > 0) {
                    zongShu = trYajiangSMapper.findXiangMuZS(ids);//查询所有设备在表中存在的数量
                    yuJingS = trYajiangSMapper.findYuJingS(ids);//根据所有的设备查询不合格的数量
                    biHeS = trYajiangSMapper.findBiHeS(ids);//根据所有的设备查询状态值20 的数量
                }
                String gongsi = devicePipepileHistorydataOneMapper.findGsMc(orgCode1);
                String yujinglv = getPercentStr(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(biHeS, yuJingS);//闭合率
                }
                map1.put("gongsi", gongsi);
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        }
        return sj;
    }

    /**
     * @return
     */
    @Override
    public List<Map<String, Object>> findWarningSta(String orgCode) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = null;
        if (orgCode != null) {
            listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
        } else {
            listMap = devicePipepileHistorydataOneMapper.selectSBNo();
        }
        for (Map<String, Object> map : listMap) {
            Map<String, Object> map1 = new HashMap<>();
            String orgCode1 = map.get("org_code").toString();
            String departName = map.get("depart_name").toString();
            List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);

            if (sbs.size() == 0) {
                continue;
            }
            String szhu = "%柱";
            String sliang = "%梁";
            Double zhuhegelv = 0.00;
            Double lianghegelv = 0.00;
            Integer zhu = gangjinbhcMMapper.selecbyzhid(sbs, szhu);
            Integer liang = gangjinbhcMMapper.selecbyzhid(sbs, sliang);
            Integer zhuhg = gangjinbhcMMapper.selecbyzhidbhg(sbs, szhu);
            Integer lianghg = gangjinbhcMMapper.selecbyzhidbhg(sbs, sliang);

            if (zhu != 0) {
                zhuhegelv = getPercentStrss(zhuhg, zhu);//合格率
            }
            if (liang != 0) {
                lianghegelv = getPercentStrss(lianghg, liang);//合格率
            }
            map1.put("departName", departName);
            map1.put("zhuhegelv", zhuhegelv);
            map1.put("lianghegelv", lianghegelv);
            sj.add(map1);
        }
        return sj;
    }

    @Override
    public String getSbjName(String sheBeiNo) {
        ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(sheBeiNo);
        if (selectshebeione != null) {
            return selectshebeione.getSbname();
        } else {
            return sheBeiNo;
        }
    }

    @Override
    public String getDepartName(String orgCode) {
        try {
            SysDepart depart1 = devicePipepileHistorydataOneMapper.getSysDepart(orgCode);//通过部门编号获取到部门的父id
            SysDepart depart = devicePipepileHistorydataOneMapper.getDepart(depart1.getParentId());//id=父id找到上一级部门信息
            while (!"4".equals(depart.getOrgCategory())) {//判断部门的机构类别是否为项目级(category=4)
                depart = devicePipepileHistorydataOneMapper.getDepart(depart.getParentId());
            }
            return depart.getDepartName();
        } catch (Exception e) {
            SysDepart depart = devicePipepileHistorydataOneMapper.getSysDepart(orgCode);
            return depart.getDepartName();
        }
    }

    @Override
    public List<DevicePipepileHistorydataOne> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel) {
        return devicePipepileHistorydataOneMapper.selectUnifiedProcess(curid,alertstate,shebeiList,overLevel);
    }

    @Override
    public List<Map<String, Object>> findjhtjStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            for (int i = 1; i < 4; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 0;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
//                        zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
//                        yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
//                        biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        String dateNowStr1 = dateNowStr + "%";
                        BhzCementStatistics bhzCementStatistics = bhzCementStatisticsMapper.selectsum(sbs, dateNowStr1);
                        if (bhzCementStatistics != null) {
                            zongShu = bhzCementStatistics.getAllDish();
                            yuJingS = bhzCementStatistics.getAllOverproofDish();
                            biHeS = bhzCementStatistics.getAllHandleDish();
                        }
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else if (i == 2) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 1;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        String dateNowStr1 = dateNowStr + "%";
                        BhzLqStatistics bhzLqStatistics = bhzLqStatisticsMapper.selectsum(sbs, dateNowStr1);
                        if (bhzLqStatistics != null) {
                            zongShu = bhzLqStatistics.getAllDish();
                            yuJingS = bhzLqStatistics.getAllOverproofDish();
                            biHeS = bhzLqStatistics.getAllHandleDish();
                        }
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 2;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        String dateNowStr1 = dateNowStr + "%";
                        BhzSwStatistics bhzSwStatistics = bhzSwStatisticsMapper.selectsum(sbs, dateNowStr1);
                        if (bhzSwStatistics != null) {
                            zongShu = bhzSwStatistics.getAllDish();
                            yuJingS = bhzSwStatistics.getAllOverproofDish();
                            biHeS = bhzSwStatistics.getAllHandleDish();
                        }
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                }
            }
            list.add(sj);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findjhtjzlyjStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            for (int i = 1; i < 3; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 9;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 10;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = trYajiangSMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = trYajiangSMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = trYajiangSMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                }
            }
            list.add(sj);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findjhtjsybyStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            for (int i = 1; i < 3; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 3;
                    int sbtypes1 = 4;
                    int sbtypes2 = 12;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    List<String> sbs1 = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes1);
                    List<String> sbs2 = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes2);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs.size() > 0) {
                        int i1 = syjzbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        int i2 = syjzbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        int i3 = syjzbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        yuJingS += i2;
                        biHeS += i3;
                        zongShu += i1;
                    }
                    if (sbs1.size() > 0){
                        int i1 = syjzbMapper.findXiangMuZStime(sbs1, dateNowStr);//查询所有设备在表中存在的数量
                        int i2 = syjzbMapper.findYuJingStime(sbs1, dateNowStr);//根据所有的设备查询不合格的数量
                        int i3 = syjzbMapper.findBiHeStime(sbs1, dateNowStr);//根据所有的设备查询状态值20 的数量
                        yuJingS += i2;
                        biHeS += i3;
                        zongShu += i1;
                    }
                    if (sbs2.size() > 0){
                        int i1 = syjzbMapper.findXiangMuZStime(sbs2, dateNowStr);//查询所有设备在表中存在的数量
                        int i2 = syjzbMapper.findYuJingStime(sbs2, dateNowStr);//根据所有的设备查询不合格的数量
                        int i3 = syjzbMapper.findBiHeStime(sbs2, dateNowStr);//根据所有的设备查询状态值20 的数量
                        yuJingS += i2;
                        biHeS += i3;
                        zongShu += i1;
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size()+sbs1.size()+sbs2.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 11;
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = bysRealMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = bysRealMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                      //  biHeS = bysRealMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        biHeS = yuJingS;
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                }
            }
            list.add(sj);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findjhtyyStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            for (int i = 1; i < 4; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 47;
                    List<String> sbs = projectMapper.fingbhzSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        String dateNowStr1 = dateNowStr + "%";
                        List<String> sbs1 = constructionresultsMapper.selectsum(sbs, dateNowStr1);
                        if (sbs1.size() > 0){
                            zongShu = constructionresultsTpMapper.selectsum(sbs1);
                        }
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else if (i == 2) {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 48;
                    List<String> sbs = projectMapper.fingbhzSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        String dateNowStr1 = dateNowStr + "%";
                        List<String> sbs1 = constructionresultsMapper.selectsum(sbs, dateNowStr1);
                        if (sbs1.size() > 0){
                            zongShu = constructionresultsNyMapper.selectsum(sbs1);
                        }
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 49;
                    List<String> sbs = truckMapper.fingbhzSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        String dateNowStr1 = dateNowStr + "%";
                        zongShu = transportrecordsMapper.selectsum(sbs, dateNowStr1);
                    }
                    map1.put("sbtypes", sbtypes);
                    map1.put("sbs", sbs.size());
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("bhs", biHeS);
                    sj.add(map1);
                }
            }
            list.add(sj);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findjhtyysStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            for (int i = 1; i < 6; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<HcMachine> sum1 = machineMapper.fingsum1(orgCode);
                    if(sum1.size() > 0){
                        for (HcMachine sum :sum1){
                            String pjid = sum.getPjid();
                            String sectionid = sum.getSectionid();
                            List<HcConstructionresults> selectpjidmid = constructionresultsMapper.selectpjidmid(pjid, sectionid);
                            if (selectpjidmid.size() > 0){
                                for (HcConstructionresults record :selectpjidmid){
                                    String startstation = record.getStartstation();
                                    String endstation = record.getEndstation();
                                    String startstation1 = getStartstation(startstation);
                                    String endstation1 = getEndstation(endstation);

                                    record.setStartstation(startstation1);
                                    record.setEndstation(endstation1);
                                }
                            }
                            map1.put("xq",selectpjidmid);
                        }
                    }else {
                        List<HcConstructionresults> hcConstructionresults = new ArrayList<>();
                        map1.put("xq",hcConstructionresults);
                    }
                    map1.put("sbtypes", i);//压路机
                    map1.put("zxs", sum1.size());
                    sj.add(map1);
                } else if (i == 2) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<HcMachine> sum1 = machineMapper.fingsum(orgCode);
                    if(sum1.size() > 0){
                        for (HcMachine sum :sum1){
                            String pjid = sum.getPjid();
                            String sectionid = sum.getSectionid();
                            List<HcConstructionresults> selectpjidmid = constructionresultsMapper.selectpjidmid(pjid, sectionid);
                            if (selectpjidmid.size() > 0){
                                for (HcConstructionresults record :selectpjidmid){
                                    String startstation = record.getStartstation();
                                    String endstation = record.getEndstation();
                                    String startstation1 = getStartstation(startstation);
                                    String endstation1 = getEndstation(endstation);

                                    record.setStartstation(startstation1);
                                    record.setEndstation(endstation1);
                                }
                            }
                            map1.put("xq",selectpjidmid);
                        }
                    }else {
                        List<HcConstructionresults> hcConstructionresults = new ArrayList<>();
                        map1.put("xq",hcConstructionresults);
                    }
                    map1.put("sbtypes", i);//摊铺机
                    map1.put("zxs", sum1.size());
                    sj.add(map1);
                } else if (i == 3){
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = truckMapper.fingbhzSheBeiNo(orgCode);
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<HcTransportrecords> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("truckId",sbs);
                        queryWrapper.last("limit 3");
                        List<HcTransportrecords> hcTransportrecords = transportrecordsMapper.selectList(queryWrapper);
                        map1.put("xq",hcTransportrecords);
                    }else {
                        List<HcTransportrecords> hcConstructionresults = new ArrayList<>();
                        map1.put("xq",hcConstructionresults);
                    }
                    map1.put("sbtypes", i);//运输车
                    map1.put("zxs", sbs.size());
                    sj.add(map1);
                }else if (i == 4){
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 0;//混凝土
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("shebei_no",sbs);
                        queryWrapper.last("limit 3");
                        List<BhzCementBase> bhzCementBases = bhzCementBaseMapper.selectList(queryWrapper);
                        map1.put("xq",bhzCementBases);
                    }else {
                        List<BhzCementBase> hcConstructionresults = new ArrayList<>();
                        map1.put("xq",hcConstructionresults);
                    }
                    map1.put("sbtypes", i);
                    map1.put("zxs", sbs.size());
                    sj.add(map1);
                }else {
                    Map<String, Object> map1 = new HashMap<>();
                    int sbtypes = 27;//地磅
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingbhzSheBeiNo(orgCode,sbtypes);
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("shebeibianhao",sbs);
                        queryWrapper.last("limit 3");
                        List<Wzycljinchanggb> wzycljinchanggbs = wzycljinchanggbService.list(queryWrapper);
                        map1.put("xq",wzycljinchanggbs);
                    }else {
                        List<Wzycljinchanggb> hcConstructionresults = new ArrayList<>();
                        map1.put("xq",hcConstructionresults);
                    }
                    map1.put("sbtypes", i);
                    map1.put("zxs", sbs.size());
                    sj.add(map1);
                }
            }
            list.add(sj);
        }
        return sj;
    }

    public String getStartstation(String startstation){
        String startsta = null;
        if (startstation != null){
            if(startstation.length() == 2){
                startsta = "K0+0" + startstation;
            }else if (startstation.length() == 3){
                startsta = "K0+" + startstation;
            }else if (startstation.length() > 3){
                String substring = startstation.substring(0, startstation.length() - 3);
                String substring1 = startstation.substring(startstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }

    public String getEndstation(String endstation){
        String startsta = null;
        if (endstation != null){
            if(endstation.length() == 2){
                startsta = "K0+0" + endstation;
            }else if (endstation.length() == 3){
                startsta = "K0+" + endstation;
            }else if (endstation.length() > 3){
                String substring = endstation.substring(0, endstation.length() - 3);
                String substring1 = endstation.substring(endstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }
    /**
     * 设备工艺
     *
     * @param orgCode
     * @return
     */
    @Override
    public List<Object> findgxStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            Integer yuJingSs = 0;
            Integer biHeSs = 0;
            Integer zongShus = 0;
            for (int i = 1; i < 9; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        Map<String, Long> xiangMutimes = bhzCementBaseMapper.findXiangMutimes(sbs, dateNowStr);
                        zongShu= Math.toIntExact(xiangMutimes.get("zongs"));
                        yuJingS = Math.toIntExact(xiangMutimes.get("yujings"));
                         biHeS = Math.toIntExact(xiangMutimes.get("bihes"));
//                        zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
//                        yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
//                        biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "混凝土拌和");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 2) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        Map<String, Long> xiangMutimes = zhanglaXxbMapper.findXiangMutime(sbs, dateNowStr);
                        zongShu= Math.toIntExact( xiangMutimes.get("zongs"));
                        yuJingS = Math.toIntExact(xiangMutimes.get("yujings"));
                        biHeS = Math.toIntExact(xiangMutimes.get("bihes"));
//                        zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
//                        yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
//                        biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "智能张拉");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 3) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    List<String> ids = new ArrayList<>();
//                    if(sbs.size() > 0){
//                        ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
//                    }
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        Map<String, Long> xiangMutimes = trYajiangSMapper.findXiangMutime(sbs, dateNowStr);
                        zongShu= Math.toIntExact(xiangMutimes.get("zongs"));
                        yuJingS =Math.toIntExact( xiangMutimes.get("yujings"));
                        biHeS=Math.toIntExact(xiangMutimes.get("bihes"));
//
//                        zongShu = trYajiangSMapper.findXiangMuZStime(ids, dateNowStr);//查询所有设备在表中存在的数量
//                        yuJingS = trYajiangSMapper.findYuJingStime(ids, dateNowStr);//根据所有的设备查询不合格的数量
//                        biHeS = trYajiangSMapper.findBiHeStime(ids, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "智能压浆");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 4) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = lqBasesMapper.findXiangMuZSs(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = lqBasesMapper.findYuJingSs(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = lqBasesMapper.findBiHeSs(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "沥青拌合");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 5) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
//                    if (sbs != null && sbs.size() > 0) {
//                        zongShu = devicePipepileHistorydataOneMapper.findXiangMuZS(sbs);//查询所有设备在表中存在的数量
//                        yuJingS = devicePipepileHistorydataOneMapper.findYuJingS(sbs);//根据所有的设备查询不合格的数量
//                        biHeS = devicePipepileHistorydataOneMapper.findBiHeS(sbs);//根据所有的设备查询状态值20 的数量
//                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "摊铺碾压");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 6) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "预应力管桩");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 7) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                        queryWrapper.likeRight("pile_time",dateNowStr);
                        queryWrapper.in("shebeino",sbs);
                        queryWrapper.ne("piletype",1);
                        queryWrapper.ne("piletype",3);
                        queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                        zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.ne("piletype",1);
                        queryWrapper1.ne("piletype",3);
                        queryWrapper1.eq("chaobiaodengji",1);
                        queryWrapper1.likeRight("pile_time",dateNowStr);
                        queryWrapper1.in("shebeino",sbs);
                        queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                        yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.ne("piletype",1);
                        queryWrapper2.ne("piletype",3);
                        queryWrapper2.eq("chaobiaodengji",1);
                        queryWrapper2.eq("handlestate",20);
                        queryWrapper2.likeRight("pile_time",dateNowStr);
                        queryWrapper2.in("shebeino",sbs);
                        queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                        biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "水泥搅拌桩");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                        queryWrapper.ne("piletype",0);
                        queryWrapper.ne("piletype",2);
                        queryWrapper.likeRight("pile_time",dateNowStr);
                        queryWrapper.in("shebeino",sbs);
                        queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                        zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.ne("piletype",0);
                        queryWrapper1.ne("piletype",2);
                        queryWrapper1.eq("chaobiaodengji",1);
                        queryWrapper1.likeRight("pile_time",dateNowStr);
                        queryWrapper1.in("shebeino",sbs);
                        queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                        yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.ne("piletype",0);
                        queryWrapper2.ne("piletype",2);
                        queryWrapper2.eq("chaobiaodengji",1);
                        queryWrapper2.eq("handlestate",20);
                        queryWrapper2.likeRight("pile_time",dateNowStr);
                        queryWrapper2.in("shebeino",sbs);
                        queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                        biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                    }
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100";
                        }
                    }
                    String yujinglv = null;
                    if (zongShu == 0) {
                        continue;
                    } else {
                        yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    }
                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "高压喷旋桩");
                    map1.put("zs", zongShu);
                    map1.put("yjl", yujinglv);
                    map1.put("yjs", yuJingS);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                }
            }
            String yujinglv = getPercentStrs(yuJingSs, zongShus);//总预警率
            String bihelvs = getPercentStrs(biHeSs, yuJingSs);//总闭合率
            if (Double.parseDouble(bihelvs) > 100) {
                bihelvs = "100";
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("yjcs", yuJingSs);
            map.put("yjl", yujinglv);
            map.put("zbhs", biHeSs);
            map.put("zgl", bihelvs);
            map.put("detail", sj);
            list.add(map);
        }
        return list;
    }

    /**
     * 混凝土
     *
     * @param orgCode
     * @return
     */
    @Override
    public List<Object> findhntStatus(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        if (null != orgCode) {
            List<SysDepart> lists = devicePipepileHistorydataOneMapper.fandxmordcode(orgCode+"%",4);
            if(lists.size() > 0){
                for (SysDepart l :lists){

                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(l.getOrgCode());
                    if (sbs.size() > 0){
                        HashMap<String, Object> map = new HashMap<>();
                        QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
                        queryWrapper.select("sum(estimate_number) estimate_number,sum(primary_dish) primary_dish,sum(middle_dish) middle_dish,sum(advanced_dish) advanced_dish,sum(all_dish) all_dish");
                        queryWrapper.in("shebei_no",sbs);
                        queryWrapper.likeRight("statistics_time",dateNowStr);
                        BhzCementStatistics bhzCementStatistics = bhzCementStatisticsMapper.selectOne(queryWrapper);
                        if (bhzCementStatistics != null && bhzCementStatistics.getAllDish() != null){
                            map.put("zl", bhzCementStatistics.getEstimateNumber());
                            map.put("czl", getPercentStrss(bhzCementStatistics.getPrimaryDish(), bhzCementStatistics.getAllDish()));
                            map.put("zzl", getPercentStrss(bhzCementStatistics.getMiddleDish(), bhzCementStatistics.getAllDish()));
                            map.put("gzl", getPercentStrss(bhzCementStatistics.getAdvancedDish(), bhzCementStatistics.getAllDish()));
                            map.put("departname", l.getDepartName());
                            list.add(map);
                        }
                    }
                }
            }

        }
        return list;
    }
    /**
     * 混凝土
     *
     * @param orgCode
     * @return
     */
    @Override
    public List<Object> findhntczgStatus( String orgCode) {
        List<Object> list = new ArrayList<>();
        if (null != orgCode) {
            List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
            if (sbs.size() > 0){
                for (String sb :sbs){
                    ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(sb);
                    HashMap<String, Object> map = new HashMap<>();
                    QueryWrapper<BhzCementStatistics> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("sum(estimate_number) estimate_number,sum(primary_dish) primary_dish,sum(middle_dish) middle_dish,sum(advanced_dish) advanced_dish,sum(all_dish) all_dish");
                    queryWrapper.eq("shebei_no",sb);
                    BhzCementStatistics bhzCementStatistics = bhzCementStatisticsMapper.selectOne(queryWrapper);
                    if (bhzCementStatistics != null && bhzCementStatistics.getAllDish() != null){
                        map.put("zl", bhzCementStatistics.getEstimateNumber());
                        map.put("czl", getPercentStrss(bhzCementStatistics.getPrimaryDish(), bhzCementStatistics.getAllDish()));
                        map.put("zzl", getPercentStrss(bhzCementStatistics.getMiddleDish(), bhzCementStatistics.getAllDish()));
                        map.put("gzl", getPercentStrss(bhzCementStatistics.getAdvancedDish(), bhzCementStatistics.getAllDish()));
                        map.put("departname", selectshebeione.getSbname());
                        list.add(map);
                    }
                }

            }

        }
        return list;
    }
    /**
     * 设备工艺
     *
     * @param dateNowStr
     * @return
     */
    @Override
    public List<Object> findgxyears(String dateNowStr, String orgCode) {
        List<Object> list = new ArrayList<>();
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            Double yuJingSs = 0.0;
            Double biHeSs = 0.0;
            Double zongShus = 0.0;
            for (int i = 1; i < 7; i++) {
                if (i == 1) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNos(orgCode);
                    Double yuJingS = 0.0;
                    Double biHeS = 0.0;
                    Double zongShu = 0.0;
                    Integer countNumber = bhzCementStatisticsMapper.selectCountNumber(sbs, dateNowStr);//瑞苍项目生产混凝土量
                    if (sbs != null && sbs.size() > 0) {
                        GongyiStatistic gongyiStatistics = gongyiStatisticService.selectCountSumsss(orgCode, dateNowStr);
                        zongShu = Double.valueOf(gongyiStatistics.getZongshu());
                        yuJingS = Double.valueOf(gongyiStatistics.getYujings());
                        biHeS = Double.valueOf(gongyiStatistics.getBihes());

//                        zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs,dateNowStr);//查询所有设备在表中存在的数量
//                        yuJingS = bhzCementBaseMapper.findYuJingStime(sbs,dateNowStr);//根据所有的设备查询不合格的数量
//                        biHeS = bhzCementBaseMapper.findBiHeStime(sbs,dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = String.valueOf(getPercentStrss2(yuJingS, zongShu));//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = String.valueOf(getPercentStrss2(biHeS, yuJingS));//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "混凝土拌和");
                    map1.put("zs", countNumber);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 2) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "智能张拉");
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 3) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = trYajiangSMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = trYajiangSMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = trYajiangSMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "智能压浆");
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 4) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                        yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                        biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "预应力管桩");
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else if (i == 5) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                        queryWrapper.likeRight("pile_time",dateNowStr);
                        queryWrapper.in("shebeino",sbs);
                        queryWrapper.ne("piletype",1);
                        queryWrapper.ne("piletype",3);
                        queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                        zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.ne("piletype",1);
                        queryWrapper1.ne("piletype",3);
                        queryWrapper1.eq("chaobiaodengji",1);
                        queryWrapper1.likeRight("pile_time",dateNowStr);
                        queryWrapper1.in("shebeino",sbs);
                        queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                        yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.ne("piletype",1);
                        queryWrapper2.ne("piletype",3);
                        queryWrapper2.eq("chaobiaodengji",1);
                        queryWrapper2.eq("handlestate",20);
                        queryWrapper2.likeRight("pile_time",dateNowStr);
                        queryWrapper2.in("shebeino",sbs);
                        queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                        biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "水泥搅拌桩");
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    Integer zongShu = 0;
                    if (sbs != null && sbs.size() > 0) {
                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                        queryWrapper.ne("piletype",0);
                        queryWrapper.ne("piletype",2);
                        queryWrapper.likeRight("pile_time",dateNowStr);
                        queryWrapper.in("shebeino",sbs);
                        queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                        zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.ne("piletype",0);
                        queryWrapper1.ne("piletype",2);
                        queryWrapper1.eq("chaobiaodengji",1);
                        queryWrapper1.likeRight("pile_time",dateNowStr);
                        queryWrapper1.in("shebeino",sbs);
                        queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                        yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                        QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.ne("piletype",0);
                        queryWrapper2.ne("piletype",2);
                        queryWrapper2.eq("chaobiaodengji",1);
                        queryWrapper2.eq("handlestate",20);
                        queryWrapper2.likeRight("pile_time",dateNowStr);
                        queryWrapper2.in("shebeino",sbs);
                        queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                        List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                        biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100) {
                            bihelv = "100.0";
                        }
                    }
                    if (zongShu == 0) {
                        continue;
                    }

                    yuJingSs += yuJingS;
                    biHeSs += biHeS;
                    zongShus += zongShu;
                    map1.put("gymc", "高压喷旋桩");
                    map1.put("zs", zongShu);
                    map1.put("yjs", yuJingS);
                    map1.put("yjl", yujinglv);
                    map1.put("type", i);
                    map1.put("zgl", bihelv);
                    sj.add(map1);
                }
            }
            String yujinglv = String.valueOf(getPercentStrss2(yuJingSs, zongShus));//总预警率
            String bihelvs = String.valueOf(getPercentStrss2(biHeSs, yuJingSs));//总闭合率
            if (Double.parseDouble(bihelvs) > 100) {
                bihelvs = "100.0";
            }
            if (biHeSs > yuJingSs) {
                biHeSs = yuJingSs;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("yjcs", yuJingSs);
            map.put("yjl", yujinglv);
            map.put("zgls", biHeSs);
            map.put("zgl", bihelvs);
            map.put("detail", sj);
            list.add(map);
        }
        return list;
    }

    /**
     * 详情
     *
     * @param type
     * @param dateNowStr
     * @param orgCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findgxtypeyears(Integer type, String dateNowStr, String orgCode) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = null;
        if (orgCode != null) {
            listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
        } else {
            listMap = devicePipepileHistorydataOneMapper.selectSBNo();
        }
        if (null != type && type == 1) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 2) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 3) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                List<String> ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (ids != null && ids.size() > 0) {
                    zongShu = trYajiangSMapper.findXiangMuZStime(ids, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = trYajiangSMapper.findYuJingStime(ids, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = trYajiangSMapper.findBiHeStime(ids, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 4) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 5) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                    queryWrapper.likeRight("pile_time",dateNowStr);
                    queryWrapper.in("shebeino",sbs);
                    queryWrapper.ne("piletype",1);
                    queryWrapper.ne("piletype",3);
                    queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                    zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.ne("piletype",1);
                    queryWrapper1.ne("piletype",3);
                    queryWrapper1.eq("chaobiaodengji",1);
                    queryWrapper1.likeRight("pile_time",dateNowStr);
                    queryWrapper1.in("shebeino",sbs);
                    queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                    yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.ne("piletype",1);
                    queryWrapper2.ne("piletype",3);
                    queryWrapper2.eq("chaobiaodengji",1);
                    queryWrapper2.eq("handlestate",20);
                    queryWrapper2.likeRight("pile_time",dateNowStr);
                    queryWrapper2.in("shebeino",sbs);
                    queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                    biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        } else if (null != type && type == 6) {
            for (Map<String, Object> map : listMap) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                String departName = map.get("depart_name").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                    queryWrapper.ne("piletype",0);
                    queryWrapper.ne("piletype",2);
                    queryWrapper.likeRight("pile_time",dateNowStr);
                    queryWrapper.in("shebeino",sbs);
                    queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                    zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.ne("piletype",0);
                    queryWrapper1.ne("piletype",2);
                    queryWrapper1.eq("chaobiaodengji",1);
                    queryWrapper1.likeRight("pile_time",dateNowStr);
                    queryWrapper1.in("shebeino",sbs);
                    queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                    yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.ne("piletype",0);
                    queryWrapper2.ne("piletype",2);
                    queryWrapper2.eq("chaobiaodengji",1);
                    queryWrapper2.eq("handlestate",20);
                    queryWrapper2.likeRight("pile_time",dateNowStr);
                    queryWrapper2.in("shebeino",sbs);
                    queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                    biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("biHeS", biHeS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("orgCode", orgCode1);
                sj.add(map1);
            }
        }
        return sj;
    }

    /**
     * 详情
     *
     * @param type
     * @param orgCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findgxtypeyearss(Integer type, String orgCode, String beginTime, String
            endTime) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = null;
        if (orgCode != null) {
            listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
        } else {
            listMap = devicePipepileHistorydataOneMapper.selectSBNo();
        }
        if (null != type && type == 1) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = bhzCementBaseMapper.findXiangMuZStimes(sbs,beginTime,endTime);//查询所有设备在表中存在的数量
                        yuJingS = bhzCementBaseMapper.findYuJingStimes(sbs,beginTime,endTime);//根据所有的设备查询不合格的数量
                        biHeS = bhzCementBaseMapper.findBiHeStimes(sbs,beginTime,endTime);//根据所有的设备查询状态值20 的数量
//                        BhzCementStatistics bhzCementStatistics = bhzCementBaseMapper.findzsyjsbhs(sbs, beginTime, endTime);
//                        if (bhzCementStatistics != null) {
//                            zongShu = bhzCementStatistics.getAllDish();
//                            yuJingS = bhzCementStatistics.getAllOverproofDish();
//                            biHeS = bhzCementStatistics.getAllHandleDish();
//                        }
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }

                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = bhzCementBaseMapper.findXiangMuZStimes(sbs,beginTime,endTime);//查询所有设备在表中存在的数量
                    yuJingS = bhzCementBaseMapper.findYuJingStimes(sbs,beginTime,endTime);//根据所有的设备查询不合格的数量
                    biHeS = bhzCementBaseMapper.findBiHeStimes(sbs,beginTime,endTime);//根据所有的设备查询状态值20 的数量
//                    BhzCementStatistics bhzCementStatistics = bhzCementBaseMapper.findzsyjsbhs(sbs, beginTime, endTime);
//                    if (bhzCementStatistics != null) {
//                        zongShu = bhzCementStatistics.getAllDish();
//                        yuJingS = bhzCementStatistics.getAllOverproofDish();
//                        biHeS = bhzCementStatistics.getAllHandleDish();
//                    }
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);

                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 2) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = zhanglaXxbMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = zhanglaXxbMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = zhanglaXxbMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }

                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = zhanglaXxbMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = zhanglaXxbMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = zhanglaXxbMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 3) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    List<String> ids = null;
                    if(sbs.size() > 0){
                        ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                    }
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0 && ids.size() > 0) {
                        zongShu = trYajiangSMapper.findXiangMuZStimes(ids, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = trYajiangSMapper.findYuJingStimes(ids, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = trYajiangSMapper.findBiHeStimes(ids, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }

                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                List<String> ids = null;
                if(sbs.size() > 0){
                    ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                }
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (ids != null && ids.size() > 0 && ids.size() > 0) {
                    zongShu = trYajiangSMapper.findXiangMuZStimes(ids, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = trYajiangSMapper.findYuJingStimes(ids, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = trYajiangSMapper.findBiHeStimes(ids, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 4) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = devicePipepileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = devicePipepileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }

                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = devicePipepileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = devicePipepileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 5) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = mixpileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = mixpileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = mixpileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }

                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = mixpileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = mixpileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = mixpileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 6) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = mixpileHistorydataOneMapper.findXiangMuZSstimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = mixpileHistorydataOneMapper.findYuJingSstimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = mixpileHistorydataOneMapper.findBiHeSstimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                        if (Double.parseDouble(bihelv) > 100){
                            bihelv = "100";
                        }
                    }

                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = mixpileHistorydataOneMapper.findXiangMuZSstimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = mixpileHistorydataOneMapper.findYuJingSstimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = mixpileHistorydataOneMapper.findBiHeSstimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        }
        return sj;
    }

    /**
     * 详情
     *
     * @param type
     * @param orgCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findgxtypeyearssex(Integer type, String orgCode, String beginTime, String
            endTime) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = null;
        if (orgCode != null) {
            listMap = devicePipepileHistorydataOneMapper.selectSheBeiNo(orgCode);
        } else {
            listMap = devicePipepileHistorydataOneMapper.selectSBNo();
        }
        if (null != type && type == 1) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
//                        zongShu = bhzCementBaseMapper.findXiangMuZStimes(sbs,beginTime,endTime);//查询所有设备在表中存在的数量
//                        yuJingS = bhzCementBaseMapper.findYuJingStimes(sbs,beginTime,endTime);//根据所有的设备查询不合格的数量
//                        biHeS = bhzCementBaseMapper.findBiHeStimes(sbs,beginTime,endTime);//根据所有的设备查询状态值20 的数量
                        BhzCementStatistics bhzCementStatistics = bhzCementBaseMapper.findzsyjsbhs(sbs, beginTime, endTime);
                        if (bhzCementStatistics != null) {
                            zongShu = bhzCementStatistics.getAllDish();
                            yuJingS = bhzCementStatistics.getAllOverproofDish();
                            biHeS = bhzCementStatistics.getAllHandleDish();
                        }
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
//                    zongShu = bhzCementBaseMapper.findXiangMuZStimes(sbs,beginTime,endTime);//查询所有设备在表中存在的数量
//                    yuJingS = bhzCementBaseMapper.findYuJingStimes(sbs,beginTime,endTime);//根据所有的设备查询不合格的数量
//                    biHeS = bhzCementBaseMapper.findBiHeStimes(sbs,beginTime,endTime);//根据所有的设备查询状态值20 的数量
                    BhzCementStatistics bhzCementStatistics = bhzCementBaseMapper.findzsyjsbhs(sbs, beginTime, endTime);
                    if (bhzCementStatistics != null) {
                        zongShu = bhzCementStatistics.getAllDish();
                        yuJingS = bhzCementStatistics.getAllOverproofDish();
                        biHeS = bhzCementStatistics.getAllHandleDish();
                    }
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 2) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = zhanglaXxbMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = zhanglaXxbMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = zhanglaXxbMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = zhanglaXxbMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = zhanglaXxbMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = zhanglaXxbMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 3) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    List<String> ids = null;
                    if(sbs.size() > 0){
                        ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                    }
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0 && ids.size() > 0) {
                        zongShu = trYajiangSMapper.findXiangMuZStimes(ids, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = trYajiangSMapper.findYuJingStimes(ids, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = trYajiangSMapper.findBiHeStimes(ids, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }

                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                List<String> ids = null;
                if(sbs.size() > 0){
                    ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                }
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (ids != null && ids.size() > 0 && ids.size() > 0) {
                    zongShu = trYajiangSMapper.findXiangMuZStimes(ids, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = trYajiangSMapper.findYuJingStimes(ids, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = trYajiangSMapper.findBiHeStimes(ids, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 4) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = devicePipepileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = devicePipepileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = devicePipepileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = devicePipepileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 5) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = mixpileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = mixpileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = mixpileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = mixpileHistorydataOneMapper.findXiangMuZStimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = mixpileHistorydataOneMapper.findYuJingStimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = mixpileHistorydataOneMapper.findBiHeStimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        } else if (null != type && type == 6) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                if (departName.contains("杭绍甬高速") || departName.contains("杭金衢联络线PPP项目")|| departName.contains("新浦互通")|| departName.contains("杭甬复线宁波一期")) {
                    continue;
                }
                List<Map<String, Object>> sj1 = new ArrayList<>();
                Map<String, Object> map1 = new HashMap<>();
                // 项目下子数据名
                String id = map.get("id").toString();
                String orgCode1 = map.get("org_code").toString();
                List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode1);
                for (Map<String, Object> listOrgCode : listOrgCodes) {
                    Map<String, Object> map2 = new HashMap<>();
                    String orgCode2 = listOrgCode.get("org_code").toString();
                    String departName1 = listOrgCode.get("depart_name").toString();
                    List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                    Integer zongShu = 0;
                    Integer yuJingS = 0;
                    Integer biHeS = 0;
                    if (sbs != null && sbs.size() > 0) {
                        zongShu = mixpileHistorydataOneMapper.findXiangMuZSstimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                        yuJingS = mixpileHistorydataOneMapper.findYuJingSstimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                        biHeS = mixpileHistorydataOneMapper.findBiHeSstimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                    }
                    String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    String bihelv = "";
                    if (yuJingS == 0) {
                        bihelv = "-";
                    } else {
                        bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    }
                    map2.put("xiangmu", departName1);
                    map2.put("zongshu", zongShu);
                    map2.put("yujingshu", yuJingS);
                    map2.put("yujinglv", yujinglv);
                    map2.put("bihelv", bihelv);
                    sj1.add(map2);
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = mixpileHistorydataOneMapper.findXiangMuZSstimes(sbs, beginTime, endTime);//查询所有设备在表中存在的数量
                    yuJingS = mixpileHistorydataOneMapper.findYuJingSstimes(sbs, beginTime, endTime);//根据所有的设备查询不合格的数量
                    biHeS = mixpileHistorydataOneMapper.findBiHeSstimes(sbs, beginTime, endTime);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("detail", sj1);
                sj.add(map1);
            }
        }
        return sj;
    }

    @Override
    public Integer findXiangMuZSsb(List<String> querySheBeiList, String time) {
        return devicePipepileHistorydataOneMapper.findXiangMuZSsb(querySheBeiList, time);
    }

    @Override
    public Integer findYuJingSsb(List<String> querySheBeiList, String time) {
        return devicePipepileHistorydataOneMapper.findYuJingSsb(querySheBeiList, time);
    }

    @Override
    public Integer findBiHeSsb(List<String> querySheBeiList, String time) {
        return devicePipepileHistorydataOneMapper.findBiHeSsb(querySheBeiList, time);
    }

    @Override
    public List<Map<String, Object>> findgxYearxmtj(String orgCode, String dateNowStr) {
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);
            for (Map<String, Object> listOrgCode : listOrgCodes) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode2 = listOrgCode.get("org_code").toString();
                String departName1 = listOrgCode.get("depart_name").toString();
                Integer yuJingSs = 0;
                Integer biHeSs = 0;
                Integer zongShus = 0;
                for (int i = 1; i < 8; i++) {
                    if (i == 1) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                            biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else if (i == 2) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                            biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else if (i == 3) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        List<String> ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (ids != null && ids.size() > 0) {
                            zongShu = trYajiangSMapper.findXiangMuZStime(ids, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = trYajiangSMapper.findYuJingStime(ids, dateNowStr);//根据所有的设备查询不合格的数量
                            biHeS = trYajiangSMapper.findBiHeStime(ids, dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else if (i == 4) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            zongShu = lqBasesMapper.findXiangMuZSs(sbs, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = lqBasesMapper.findYuJingSs(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                            biHeS = lqBasesMapper.findBiHeSs(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else if (i == 5) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                            biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else if (i == 6) {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                            queryWrapper.likeRight("pile_time",dateNowStr);
                            queryWrapper.in("shebeino",sbs);
                            queryWrapper.ne("piletype",1);
                            queryWrapper.ne("piletype",3);
                            queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                            zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                            queryWrapper1.ne("piletype",1);
                            queryWrapper1.ne("piletype",3);
                            queryWrapper1.eq("chaobiaodengji",1);
                            queryWrapper1.likeRight("pile_time",dateNowStr);
                            queryWrapper1.in("shebeino",sbs);
                            queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                            yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                            queryWrapper2.ne("piletype",1);
                            queryWrapper2.ne("piletype",3);
                            queryWrapper2.eq("chaobiaodengji",1);
                            queryWrapper2.eq("handlestate",20);
                            queryWrapper2.likeRight("pile_time",dateNowStr);
                            queryWrapper2.in("shebeino",sbs);
                            queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                            biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    } else {
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                            queryWrapper.ne("piletype",0);
                            queryWrapper.ne("piletype",2);
                            queryWrapper.likeRight("pile_time",dateNowStr);
                            queryWrapper.in("shebeino",sbs);
                            queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                            zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                            queryWrapper1.ne("piletype",0);
                            queryWrapper1.ne("piletype",2);
                            queryWrapper1.eq("chaobiaodengji",1);
                            queryWrapper1.likeRight("pile_time",dateNowStr);
                            queryWrapper1.in("shebeino",sbs);
                            queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                            yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                            QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                            queryWrapper2.ne("piletype",0);
                            queryWrapper2.ne("piletype",2);
                            queryWrapper2.eq("chaobiaodengji",1);
                            queryWrapper2.eq("handlestate",20);
                            queryWrapper2.likeRight("pile_time",dateNowStr);
                            queryWrapper2.in("shebeino",sbs);
                            queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                            List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                            biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                        }
                        zongShus += zongShu;
                        biHeSs += biHeS;
                        yuJingSs += yuJingS;
                    }
                }
                double yujinglv = 0.00;
                if (zongShus == 0) {
                    yujinglv = 0.00;
                } else {
                    yujinglv = getPercentStrss(yuJingSs, zongShus);//预警率
                    if (yujinglv > 100) {
                        yujinglv = 100;
                    }
                }

                double bihelv = 0.00;
                if (yuJingSs == 0) {
                    bihelv = 0.00;
                } else {
                    bihelv = getPercentStrss(biHeSs, yuJingSs);//闭合率
                    if (bihelv > 100) {
                        bihelv = 100;
                    }
                }
                map1.put("typeName", departName1);
                map1.put("zongShus", zongShus);
                map1.put("yuJingSs", yuJingSs);
                map1.put("yuJinglv", yujinglv);//
                map1.put("biHeSs", biHeSs);
                map1.put("biHelv", bihelv);//
                sj.add(map1);
            }
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findgxYearxmtjzg(String orgCode, String dateNowStr) {
        List<Map<String, Object>> sj = new ArrayList<>();
        if (null != orgCode) {
            List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);
            for (Map<String, Object> listOrgCode : listOrgCodes) {
                String orgCode2 = listOrgCode.get("org_code").toString();
                String departName1 = listOrgCode.get("depart_name").toString();
                String depart_name_abbr = listOrgCode.get("depart_name_abbr").toString();
                for (int i = 1; i < 3; i++) {
                    if (i == 1) {
                        Map<String, Object> map1 = new HashMap<>();
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (sbs != null && sbs.size() > 0) {
                            zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                            //  biHeS = zhanglaXxbMapper.findBiHeStime(sbs,dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        map1.put("departName", depart_name_abbr);
                        map1.put("typeName", departName1);
                        map1.put("leix", "预应力张拉");//
                        map1.put("zongShus", zongShu);
                        map1.put("yuJingSs", yuJingS);
                        sj.add(map1);
                    } else {
                        Map<String, Object> map1 = new HashMap<>();
                        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                        List<String> ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                        Integer yuJingS = 0;
                        Integer biHeS = 0;
                        Integer zongShu = 0;
                        if (ids != null && ids.size() > 0) {
                            zongShu = trYajiangSMapper.findXiangMuZStime(ids, dateNowStr);//查询所有设备在表中存在的数量
                            yuJingS = trYajiangSMapper.findYuJingStime(ids, dateNowStr);//根据所有的设备查询不合格的数量
                            // biHeS = trYajiangSMapper.findBiHeStime(ids,dateNowStr);//根据所有的设备查询状态值20 的数量
                        }
                        map1.put("departName", depart_name_abbr);
                        map1.put("typeName", departName1);
                        map1.put("leix", "智能压浆");//
                        map1.put("zongShus", zongShu);
                        map1.put("yuJingSs", yuJingS);
                        sj.add(map1);
                    }
                }
            }
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findgxYearxmbhztj(String orgCode, String dateNowStr) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> sj1 = new ArrayList<>();
//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//        String dateNowStr1 = sdf.format(d);
//        String dateNowStr2 = dateNowStr1 + "%";
        if (null != orgCode) {
            List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);
            List<String> sbs1 = devicePipepileHistorydataOneMapper.fingAllSheBeiNos(orgCode);
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String dateNowStrs = sdf.format(d);
            Integer countNumber = bhzCementStatisticsMapper.selectCountNumber(sbs1, dateNowStr);//瑞苍项目生产混凝土量
            if (countNumber == null){
                countNumber = 0;
            }
           // BhzCementStatistics bhzCementStatistics = bhzCementStatisticsMapper.selectsum(sbs1, dateNowStr2);
            Double yuJingSs = 0.0;
            Double biHeSs = 0.0;
            Double zongShus = 0.0;
//            if (bhzCementStatistics != null) {
//                zongShus = bhzCementStatistics.getAllDish();
//                yuJingSs = bhzCementStatistics.getAllOverproofDish();
//                biHeSs = bhzCementStatistics.getAllHandleDish();
//            }
            if (sbs1 != null && sbs1.size() > 0) {
                GongyiStatistic gongyiStatistics = gongyiStatisticService.selectCountSumsss(orgCode, dateNowStr);
                zongShus = Double.valueOf(gongyiStatistics.getZongshu());
                yuJingSs = Double.valueOf(gongyiStatistics.getYujings());
                biHeSs = Double.valueOf(gongyiStatistics.getBihes());
//
//                zongShus = bhzCementBaseMapper.findXiangMuZStime(sbs1, dateNowStr);//查询所有设备在表中存在的数量
//                yuJingSs = bhzCementBaseMapper.findYuJingStime(sbs1, dateNowStr);//根据所有的设备查询不合格的数量
//                biHeSs = bhzCementBaseMapper.findBiHeStime(sbs1, dateNowStr);//根据所有的设备查询状态值20 的数量
            }
            Map<String, Object> map = new HashMap<>();
            for (Map<String, Object> listOrgCode : listOrgCodes) {
                Map<String, Object> map1 = new HashMap<>();
                String orgCode2 = listOrgCode.get("org_code").toString();
                String departName1 = listOrgCode.get("depart_name").toString();
                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode2);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode2);
                if (sbs.size() == 0) {
                    continue;
                }
                Integer countNumber1 = bhzCementStatisticsMapper.selectCountNumber(sbs, dateNowStr);//瑞苍项目下标段生产混凝土量
                if (countNumber1 == null){
                    countNumber1 = 0;
                }
                Integer yuJingS = 0;
                Integer biHeS = 0;
                Integer zongShu = 0;
                if (sbs.size() > 0) {
                    zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                    // 初中高
                    for (int i = 1; i < 4; i++) {
                        Integer yuJingSxms = bhzCementBaseMapper.findYuJingSxmtime(sbs, dateNowStr, i);//根据i超标等级 查询不合格的数量
                        //  Integer biHeSxms = bhzCementBaseMapper.findBiHeSxmtime(sbs,dateNowStr,i);//根据i超标等级 查询状态值20 的数量
                        double yujinglv = 0.00;
                        if (zongShu == 0) {
                            yujinglv = 0.00;
                        } else {
                            yujinglv = getPercentStrss(yuJingSxms, zongShu);//预警率
                        }
                        if (i == 1) {
                            map1.put("cjyujinglv", yujinglv);
                        } else if (i == 2) {
                            map1.put("zjyujinglv", yujinglv);
                        } else {
                            map1.put("gjyujinglv", yujinglv);
                        }
                    }
                }
                double yujinglv = 0.00;
                if (zongShu == 0) {
                    yujinglv = 0.00;
                } else {
                    yujinglv = getPercentStrss(yuJingS, zongShu);//预警率
                }
                double bihelv = 0.00;
                if (yuJingS == 0) {
                    bihelv = 0.00;
                } else {
                    bihelv = getPercentStrss(biHeS, yuJingS);//闭合率
                }
                if (bihelv > 100) {
                    bihelv = 100;
                }
                map1.put("countNumber1", countNumber1);
                map1.put("bihelv", bihelv);
                map1.put("departName", departName1);
                map1.put("departNameztyjl", yujinglv);
                map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/hntbhzcbcx?username="+username);
                sj1.add(map1);
            }
            double yujinglv = 0.00;
            if (zongShus == 0) {
                yujinglv = 0.00;
            } else {
                yujinglv = getPercentStrss2(yuJingSs, zongShus);//预警率
            }

            Double bihelv = 0.00;
            if (yuJingSs == 0) {
                bihelv = 0.00;
            } else {
                bihelv = getPercentStrss2(biHeSs, yuJingSs);//闭合率
            }
            if (bihelv > 100) {
                map.put("bihelv", "100.00");
            }else {
                map.put("bihelv", bihelv);
            }
            map.put("scl", countNumber);
            map.put("yjl", yujinglv);
            map.put("detail", sj1);
            sj.add(map);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> findgxYearxmbhzyjbgt(String orgCode, Integer detatime, Integer grade) {
        List<Map<String, Object>> sj1 = new ArrayList<>();
        if (null != orgCode) {
            List<Map<String, Object>> listOrgCodes = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);

            for (Map<String, Object> listOrgCode : listOrgCodes) {

                String orgCode2 = listOrgCode.get("org_code").toString();
                String departName1 = listOrgCode.get("depart_name").toString();

                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode2);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNos(orgCode2);
                Integer yuJingS = 0;
                Integer biHeS = 0;
                Integer zongShu = 0;
                if (sbs.size() > 0) {
                    if (detatime == 0) {
                        //查询预警数最多的五条设备
                        List<String> sbss = bhzCementBaseMapper.findbyshebeilistzy(sbs);
                        for (String sb : sbss) {
                            zongShu = bhzCementBaseMapper.findXiangMuZSzy(sb, grade);//查询所有设备在表中存在的数量
                            yuJingS = bhzCementBaseMapper.findYuJingSzy(sb, grade);//根据所有的设备查询不合格的数量
                            biHeS = bhzCementBaseMapper.findBiHeSzy(sb, grade);//根据所有的设备查询状态值20 的数量
                            double yujinglv = 0.00;
                            if (zongShu == 0) {
                                yujinglv = 0.00;
                            } else {
                                yujinglv = getPercentStrss(yuJingS, zongShu);//预警率
                            }
                            ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(sb);
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("departName", departName1);
                            map1.put("departNameztyjl", yujinglv);
                            map1.put("shebei", selectshebeione.getSbname());
                            map1.put("detail", "本周总生产盘数" + zongShu + "盘，预警盘数" + yuJingS + "盘，闭合盘数" + biHeS + "盘");
                            map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/hntbhzcbcx?username="+username);
                            sj1.add(map1);
                        }
                    } else {
                        List<String> sbss = bhzCementBaseMapper.findbyshebeilistyz(sbs);
                        for (String sb : sbss) {
                            zongShu = bhzCementBaseMapper.findXiangMuZSyz(sb, grade);//查询所有设备在表中存在的数量
                            yuJingS = bhzCementBaseMapper.findYuJingSyz(sb, grade);//根据所有的设备查询不合格的数量
                            biHeS = bhzCementBaseMapper.findBiHeSyz(sb, grade);//根据所有的设备查询状态值20 的数量
                            double yujinglv = 0.00;
                            if (zongShu == 0) {
                                yujinglv = 0.00;
                            } else {
                                yujinglv = getPercentStrss(yuJingS, zongShu);//预警率
                            }
                            ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(sb);
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("departName", departName1);
                            map1.put("departNameztyjl", yujinglv);
                            map1.put("shebei", selectshebeione.getSbname());
                            map1.put("detail", "本月总生产盘数" + zongShu + "盘，预警盘数" + yuJingS + "盘，闭合盘数" + biHeS + "盘");
                            map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/hntbhzcbcx?username="+username);
                            sj1.add(map1);
                        }
                    }
                }
            }
        }
        return sj1;
    }

    @Override
    public List<Map<String, Object>> findgxYearxmxq(String orgCode, String dateNowStr, Integer type) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);
        if (null != type && type == 1) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();

                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode1);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = bhzCementBaseMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = bhzCementBaseMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = bhzCementBaseMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/hntbhzcbcx?username="+username);
                sj.add(map1);
            }
        } else if (null != type && type == 2) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode1);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = zhanglaXxbMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = zhanglaXxbMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = zhanglaXxbMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);

                map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/zlcbcxkb?username="+username);
                sj.add(map1);
            }
        } else if (null != type && type == 3) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode1);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                List<String> ids = trYajiangSMapper.findSheBeiIds(sbs);//根据设备编号查询压浆数据表的syjid
                Integer yuJingS = 0;
                Integer biHeS = 0;
                Integer zongShu = 0;
                if (ids != null && ids.size() > 0) {
                    zongShu = trYajiangSMapper.findXiangMuZStime(ids, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = trYajiangSMapper.findYuJingStime(ids, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = trYajiangSMapper.findBiHeStime(ids, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }
                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/yjcbcxkb?username="+username);
                sj.add(map1);
            }
        } else if (null != type && type == 7) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = lqBasesMapper.findXiangMuZSs(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = lqBasesMapper.findYuJingSs(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = lqBasesMapper.findBiHeSs(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                sj.add(map1);
            }
        } else if (null != type && type == 4) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                //根据标段查用户名称
                List<Tokenrecode> tokenrecodes = tokenrecodeService.seleycogcod(orgCode1);
                String username = "";
                if (tokenrecodes.size() > 0){
                    Tokenrecode tokenrecode = tokenrecodes.get(0);
                    username = tokenrecode.getUsername();
                }
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                map1.put("url", "http://47.96.161.157/jeecg-boot/sys/systems/ssoredirect/yylgzcxkb?username="+username);
                sj.add(map1);
            }
        } else if (null != type && type == 5) {
            for (Map<String, Object> map : listMap) {
                String departName = map.get("depart_name").toString();
                Map<String, Object> map1 = new HashMap<>();
                String orgCode1 = map.get("org_code").toString();
                List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (sbs != null && sbs.size() > 0) {
                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper = new QueryWrapper<>();
                    queryWrapper.likeRight("pile_time",dateNowStr);
                    queryWrapper.in("shebeino",sbs);
                    queryWrapper.ne("piletype",1);
                    queryWrapper.ne("piletype",3);
                    queryWrapper.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes = mixpileHistorydataOneMapper.selectList(queryWrapper);
                    zongShu = deviceMixpileHistorydataOnes.size();//查询所有设备在表中存在的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.ne("piletype",1);
                    queryWrapper1.ne("piletype",3);
                    queryWrapper1.eq("chaobiaodengji",1);
                    queryWrapper1.likeRight("pile_time",dateNowStr);
                    queryWrapper1.in("shebeino",sbs);
                    queryWrapper1.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes1 = mixpileHistorydataOneMapper.selectList(queryWrapper1);
                    yuJingS = deviceMixpileHistorydataOnes1.size();//根据所有的设备查询不合格的数量

                    QueryWrapper<DeviceMixpileHistorydataOne> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.ne("piletype",1);
                    queryWrapper2.ne("piletype",3);
                    queryWrapper2.eq("chaobiaodengji",1);
                    queryWrapper2.eq("handlestate",20);
                    queryWrapper2.likeRight("pile_time",dateNowStr);
                    queryWrapper2.in("shebeino",sbs);
                    queryWrapper2.groupBy("shebeino,pile_no,pile_mileage");
                    List<DeviceMixpileHistorydataOne> deviceMixpileHistorydataOnes2 = mixpileHistorydataOneMapper.selectList(queryWrapper2);
                    biHeS = deviceMixpileHistorydataOnes2.size();//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                String bihelv = "";
                if (yuJingS == 0) {
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                    if (Double.parseDouble(bihelv) > 100) {
                        bihelv = "100";
                    }
                }

                map1.put("xiangmu", departName);
                map1.put("zongshu", zongShu);
                map1.put("yujingshu", yuJingS);
                map1.put("yujinglv", yujinglv);
                map1.put("bihelv", bihelv);
                sj.add(map1);
            }
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> soqyorgcode(String orgCode) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<Map<String, Object>> listMap = devicePipepileHistorydataOneMapper.selectorgcodebyid(orgCode);
        for (Map<String, Object> map : listMap) {
            String departName = map.get("depart_name").toString();
            if (departName.equals("TJ01")) {
                continue;
            }
            Map<String, Object> map1 = new HashMap<>();
            String orgCode1 = map.get("org_code").toString();
            List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode1);
            Integer zongShu = 0;
            Integer yuJingS = 0;
            Integer biHeS = 0;
            Integer hegeshu = 0;
            Integer findzzyc = 0;
            Integer findqjyc = 0;
            if (sbs != null && sbs.size() > 0) {
                zongShu = devicePipepileHistorydataOneMapper.findXiangMuZStime(sbs, "2");//查询所有设备在表中存在的数量
                yuJingS = devicePipepileHistorydataOneMapper.findYuJingStime(sbs, "2");//根据所有的设备查询不合格的数量
                biHeS = devicePipepileHistorydataOneMapper.findBiHeStime(sbs, "2");//根据所有的设备查询状态值20 的数量
                findzzyc = devicePipepileHistorydataOneMapper.findzzyc(sbs);
                findqjyc = devicePipepileHistorydataOneMapper.findqjyc(sbs);
            }
            String bihelv = "";
            if (yuJingS == 0) {
                bihelv = "0.00";
            } else {
                bihelv = getPercentStrs(biHeS, yuJingS);//闭合率
                if (Double.parseDouble(bihelv) > 100) {
                    bihelv = "100";
                }
            }

            hegeshu = zongShu - yuJingS;
            String hegelv = getPercentStrs(hegeshu, zongShu);//预警率
            map1.put("xiangmu", departName);
            map1.put("zongshu", zongShu);
            map1.put("hegeshu", hegeshu);
            map1.put("hegelv", hegelv);
            map1.put("bihelv", bihelv);
            map1.put("findzzyc", findzzyc);
            map1.put("findqjyc", findqjyc);
            sj.add(map1);
        }
        return sj;
    }


    /**
     * 三级页面，1表示软基，2表示张拉，3表示压浆
     *
     * @param
     * @param orgCode
     * @param type
     * @return
     */
    @Override
    public List<GongYiDto> queryWarningStatusByType(String orgCode, Integer type) {
        List<GongYiDto> gongYiDtoList = new ArrayList<>();
        if (null != type && type == 1) {
            Integer allZongshu = 0;
            Integer allyujingshu = 0;
            List<GongYiVo> gongYiVos = devicePipepileHistorydataOneMapper.queryCountZy(7, orgCode);
            for (GongYiVo yiVo : gongYiVos) {
                allZongshu = allZongshu + yiVo.getAllDish();
                allyujingshu = allyujingshu + yiVo.getAllWarnDish();
                GongYiDto gongYiDto1 = new GongYiDto();
                gongYiDto1.setName(yiVo.getSectionName());
                gongYiDto1.setZongshu(yiVo.getAllDish());
                gongYiDto1.setChaobiaolv(getPercentStr(yiVo.getAllWarnDish(), yiVo.getAllDish()));
                List<GongYiVo> vos = devicePipepileHistorydataOneMapper.queryCountBySheBei(7, yiVo.getSysOrgCode());
                for (GongYiVo map1 : vos) {
                    String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                    String bihelv = "";
                    if (map1.getAllWarnDish() == 0) {
                        bihelv = "-%";
                    } else {
                        bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                    }
                    map1.setAllWarnLv(yujnglv);
                    map1.setBiheLv(bihelv);
                }
                gongYiDto1.setRecords(vos);
                gongYiDtoList.add(gongYiDto1);
            }
            GongYiDto gongYiDto = new GongYiDto();
            gongYiDto.setName("总数");
            gongYiDto.setZongshu(allZongshu);
            gongYiDto.setChaobiaolv(getPercentStr(allyujingshu, allZongshu));
            List<GongYiVo> countZy = devicePipepileHistorydataOneMapper.queryCountBySheBei(4, orgCode);
            for (GongYiVo map1 : countZy) {
                String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                String bihelv = "";
                if (map1.getAllWarnDish() == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                }
                map1.setAllWarnLv(yujnglv);
                map1.setBiheLv(bihelv);
            }
            gongYiDto.setRecords(countZy);
            gongYiDtoList.add(0, gongYiDto);
        } else if (null != type && type == 2) {
            Integer allZongshu = 0;
            Integer allyujingshu = 0;
            List<GongYiVo> gongYiVos = trZhanglaMMapper.queryCountZy(7, orgCode);
            for (GongYiVo yiVo : gongYiVos) {
                allZongshu = allZongshu + yiVo.getAllDish();
                allyujingshu = allyujingshu + yiVo.getAllWarnDish();
                GongYiDto gongYiDto1 = new GongYiDto();
                gongYiDto1.setName(yiVo.getSectionName());
                gongYiDto1.setZongshu(yiVo.getAllDish());
                gongYiDto1.setChaobiaolv(getPercentStr(yiVo.getAllWarnDish(), yiVo.getAllDish()));
                List<GongYiVo> vos = trZhanglaMMapper.queryCountBySheBei(7, yiVo.getSysOrgCode());
                for (GongYiVo map1 : vos) {
                    String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                    String bihelv = "";
                    if (map1.getAllWarnDish() == 0) {
                        bihelv = "-%";
                    } else {
                        bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                    }
                    map1.setAllWarnLv(yujnglv);
                    map1.setBiheLv(bihelv);
                }
                gongYiDto1.setRecords(vos);
                gongYiDtoList.add(gongYiDto1);
            }
            GongYiDto gongYiDto = new GongYiDto();
            gongYiDto.setName("总数");
            gongYiDto.setZongshu(allZongshu);
            gongYiDto.setChaobiaolv(getPercentStr(allyujingshu, allZongshu));
            List<GongYiVo> countZy = trZhanglaMMapper.queryCountBySheBei(4, orgCode);
            for (GongYiVo map1 : countZy) {
                String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                String bihelv = "";
                if (map1.getAllWarnDish() == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                }
                map1.setAllWarnLv(yujnglv);
                map1.setBiheLv(bihelv);
            }
            gongYiDto.setRecords(countZy);
            gongYiDtoList.add(0, gongYiDto);
        } else if (null != type && type == 3) {
            Integer allZongshu = 0;
            Integer allyujingshu = 0;
            List<GongYiVo> gongYiVos = trYajiangSMapper.queryCountZy(7, orgCode);
            for (GongYiVo yiVo : gongYiVos) {
                allZongshu = allZongshu + yiVo.getAllDish();
                allyujingshu = allyujingshu + yiVo.getAllWarnDish();
                GongYiDto gongYiDto1 = new GongYiDto();
                gongYiDto1.setName(yiVo.getSectionName());
                gongYiDto1.setZongshu(yiVo.getAllDish());
                gongYiDto1.setChaobiaolv(getPercentStr(yiVo.getAllWarnDish(), yiVo.getAllDish()));
                List<GongYiVo> vos = trYajiangSMapper.queryCountBySheBei(7, yiVo.getSysOrgCode());
                for (GongYiVo map1 : vos) {
                    String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                    String bihelv = "";
                    if (map1.getAllWarnDish() == 0) {
                        bihelv = "-%";
                    } else {
                        bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                    }
                    map1.setAllWarnLv(yujnglv);
                    map1.setBiheLv(bihelv);
                }
                gongYiDto1.setRecords(vos);
                gongYiDtoList.add(gongYiDto1);
            }
            GongYiDto gongYiDto = new GongYiDto();
            gongYiDto.setName("总数");
            gongYiDto.setZongshu(allZongshu);
            gongYiDto.setChaobiaolv(getPercentStr(allyujingshu, allZongshu));
            List<GongYiVo> countZy = trYajiangSMapper.queryCountBySheBei(4, orgCode);
            for (GongYiVo map1 : countZy) {
                String yujnglv = getPercentStr(map1.getAllWarnDish(), map1.getAllDish());
                String bihelv = "";
                if (map1.getAllWarnDish() == 0) {
                    bihelv = "-%";
                } else {
                    bihelv = getPercentStr(map1.getBiheCount(), map1.getAllWarnDish());
                }
                map1.setAllWarnLv(yujnglv);
                map1.setBiheLv(bihelv);
            }
            gongYiDto.setRecords(countZy);
            gongYiDtoList.add(0, gongYiDto);
        }

        return gongYiDtoList;
    }

    @Override
    public Map<String, Object> queryRateOfPass(String orgCode) {
        List<String> sheBs = null;
        if (orgCode != null) {
            sheBs = shebeiInfoMapper.findSheBeis(orgCode);
        } else {
            sheBs = shebeiInfoMapper.findSBeis();
        }

        LambdaQueryWrapper<DevicePipepileHistorydataOne> queryWrapper = new QueryWrapper<DevicePipepileHistorydataOne>().lambda()
                .eq(DevicePipepileHistorydataOne::getChaobiaodengji, "0")
                .in(DevicePipepileHistorydataOne::getShebeino, sheBs);
        Integer dphoCount = devicePipepileHistorydataOneMapper.selectCount(queryWrapper);//查询DevicePipepileHistorydataOne超标等级合格的数量
        Integer dphoZongShu = devicePipepileHistorydataOneMapper.findXiangMuZS(sheBs);//查询总数
        LambdaQueryWrapper<DevicePipepileHistorydataOne> wrapper = new QueryWrapper<DevicePipepileHistorydataOne>().lambda()
                .eq(DevicePipepileHistorydataOne::getOverproofStatus, 20).in(DevicePipepileHistorydataOne::getShebeino, sheBs);
        Integer dphoOverStatusCount = devicePipepileHistorydataOneMapper.selectCount(wrapper);//查询overstatus为20的数量

        LambdaQueryWrapper<TrZhanglaM> queryWrapper1 = new QueryWrapper<TrZhanglaM>().lambda()
                .in(TrZhanglaM::getShebeibianhao, sheBs)
                .and(qr -> qr.eq(TrZhanglaM::getHege, "1").or().eq(TrZhanglaM::getHege, "合格"));
        Integer zhangLaCount = trZhanglaMMapper.selectCount(queryWrapper1); //查询TrZhanglaM超标等级合格的数量
        Integer zhangLaZongShu = trZhanglaMMapper.findXiangMuZS(sheBs);//查询总数
        LambdaQueryWrapper<TrZhanglaM> wrapper1 = new QueryWrapper<TrZhanglaM>().lambda().eq(TrZhanglaM::getOverproofStatus, 20)
                .in(TrZhanglaM::getShebeibianhao, sheBs);
        Integer zlOverStatusCount = trZhanglaMMapper.selectCount(wrapper1);//查询overstatus为20的数量


        Integer yjCount = trYajiangSMapper.findHeGeCount(sheBs);
        Integer yjZongShu = trYajiangSMapper.findXiangMuZSs(sheBs);//查询总数
        Integer yjOverStatusCount = trYajiangSMapper.selectCo(sheBs);
        Integer zongShu = dphoZongShu + zhangLaZongShu + yjZongShu;
        Integer heGeZongShu = dphoCount + zhangLaCount + yjCount;
        Integer biHeZongShu = dphoOverStatusCount + zlOverStatusCount + yjOverStatusCount;
        Integer zongBuHeGeShu = zongShu - heGeZongShu;
        String heGeLv = getPercentStr(heGeZongShu, zongShu);
        String biHeLv = getPercentStr(biHeZongShu, zongBuHeGeShu);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("hegelv", heGeLv);
        hashMap.put("bihelv", biHeLv);
        return hashMap;

    }

    @Override
    public List<DevicePipepileHistorydataOne> stadate(String stadate) {
        return devicePipepileHistorydataOneMapper.stadate(stadate);
    }


    /**
     * 排序集合，截取长度
     *
     * @param list
     * @return
     */
    @Nullable
    private List<Map<String, Object>> getList(List<Map<String, Object>> list) {
        if (list != null) {
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Double a1 = Double.valueOf((o1.get("yujinglv").toString().substring(0, o1.get("yujinglv").toString().length() - 1)));
                    Double a2 = Double.valueOf((o2.get("yujinglv").toString().substring(0, o2.get("yujinglv").toString().length() - 1)));
                    return a2.compareTo(a1);
                }
            });
        }
        if (list.size() > 10) {
            List<Map<String, Object>> mapList = list.subList(0, 10);
            return mapList;
        }
        return list;
    }


    /**
     * 得到百分比的字符串，比如传参 1,3，返回33.33
     */
    private String getPercentStr(Integer diff, Integer sum) {
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        if (sum == 0) {
            return "0.00%";
        }
        float num = (float) (diff * 100) / sum;
        String str = df.format(num);
        return str + "%";
    }

    /**
     * 得到百分比的字符串，比如传参 1,3，返回33.33
     */
    private String getPercentStrs(Integer diff, Integer sum) {
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数
        if (sum == 0) {
            return "0.0";
        }
        float num = (float) (diff * 100) / sum;
        String str = df.format(num);
        return str;
    }

    /**
     * 得到百分比的字符串，比如传参 1,3，返回33.33
     */
    private Double getPercentStrss(Integer diff, Integer sum) {
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数
        if (sum == 0) {
            return 0.00;
        }
        float num = (float) (diff * 100) / sum;
        Double str = Double.valueOf(df.format(num));

        return str;
    }

    /**
     * 得到百分比的字符串，比如传参 1,3，返回33.33
     */
    private Double getPercentStrss2(Double diff, Double sum) {
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数
        if (sum == 0) {
            return 0.00;
        }
        float num = (float) ((float) (diff * 100) / sum);
        Double str = Double.valueOf(df.format(num));

        return str;
    }
}
