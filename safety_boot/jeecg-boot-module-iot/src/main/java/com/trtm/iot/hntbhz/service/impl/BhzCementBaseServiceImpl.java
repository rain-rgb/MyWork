package com.trtm.iot.hntbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.mapper.BhzCementOverHandlerMapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.mapper.BhzCementDetailMapper;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.vo.BhzCementBaseCbTongji;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.hntbhz.vo.BhzCementTongJi;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Service
public class BhzCementBaseServiceImpl extends ServiceImpl<BhzCementBaseMapper, BhzCementBase> implements IBhzCementBaseService {

    @Autowired
    private BhzCementBaseMapper bhzCementBaseMapper;
    @Autowired
    private BhzCementDetailMapper bhzCementDetailMapper;
    @Autowired
    private BhzCementOverHandlerMapper bhzCementOverHandlerMapper;

    @Override
    @Transactional
    public void saveMain(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList) {
        Integer stirDatetime = bhzCementBase.getStirDatetime();
        if (stirDatetime > 1000) {
            bhzCementBase.setShebeiNo("FEIQI");//当搅拌时间大于1000时给这个数据加上废弃的 设备类型
        }
        if ("sszwdqlc01".equals(bhzCementBase.getShebeiNo())&&!bhzCementBase.getProjectName().contains("申苏浙皖")) {
            bhzCementBase.setShebeiNo("sszwdqlc01sc");
            bhzCementBase.setAlertstate(1);
        }
        if ("lhjd20240811".equals(bhzCementBase.getShebeiNo())&&!bhzCementBase.getProjectName().contains("申苏浙皖")) {
            bhzCementBase.setShebeiNo("lhjd20240811sc");
            bhzCementBase.setAlertstate(1);
        }
        LambdaQueryWrapper<BhzCementBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BhzCementBase::getBatchNo, bhzCementBase.getBatchNo());
        BhzCementBase one = this.getOne(queryWrapper);
        if (one == null) {
            bhzCementBaseMapper.insert(bhzCementBase);
            if (bhzCementDetailList != null && bhzCementDetailList.size() > 0) {
                for (BhzCementDetail entity : bhzCementDetailList) {
                    //外键设置
                    entity.setBatchNo(bhzCementBase.getBatchNo());
                    bhzCementDetailMapper.insert(entity);
                }
            }
        } else {
            return;
        }

    }

    @Override
    @Transactional
    public void saveMains(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList, BhzCementOverHandler bhzCementOverHandler) {
        Integer stirDatetime = bhzCementBase.getStirDatetime();
        if (stirDatetime > 1000) {
            bhzCementBase.setShebeiNo("FEIQI");//当搅拌时间大于1000时给这个数据加上废弃的 设备类型
        }
        if ("sszwdqlc01".equals(bhzCementBase.getShebeiNo())&&!bhzCementBase.getProjectName().contains("申苏浙皖")) {
            bhzCementBase.setShebeiNo("sszwdqlc01sc");
            bhzCementBase.setAlertstate(1);
        }
        if ("lhjd20240811".equals(bhzCementBase.getShebeiNo())&&!bhzCementBase.getProjectName().contains("申苏浙皖")) {
            bhzCementBase.setShebeiNo("lhjd20240811sc");
            bhzCementBase.setAlertstate(1);
        }
        LambdaQueryWrapper<BhzCementBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BhzCementBase::getBatchNo, bhzCementBase.getBatchNo());
        BhzCementBase one = this.getOne(queryWrapper);
        if (one == null) {
            bhzCementBaseMapper.insert(bhzCementBase);
            if (bhzCementDetailList != null && bhzCementDetailList.size() > 0) {
                for (BhzCementDetail entity : bhzCementDetailList) {
                    //外键设置
                    entity.setBatchNo(bhzCementBase.getBatchNo());
                    bhzCementDetailMapper.insert(entity);
                }
            }
            if (bhzCementOverHandler != null) {
                bhzCementOverHandlerMapper.insert(bhzCementOverHandler);
            }
        } else {
            return;
        }

    }

    @Override
    @Transactional
    public void updateMain(BhzCementBase bhzCementBase, List<BhzCementDetail> bhzCementDetailList) {
        bhzCementBaseMapper.updateById(bhzCementBase);

        //1.先删除子表数据
        bhzCementDetailMapper.deleteByMainId(bhzCementBase.getBatchNo());

        //2.子表数据重新插入
        if (bhzCementDetailList != null && bhzCementDetailList.size() > 0) {
            for (BhzCementDetail entity : bhzCementDetailList) {
                //外键设置
                entity.setBatchNo(bhzCementBase.getBatchNo());
                bhzCementDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        bhzCementDetailMapper.deleteByMainId(id);
        bhzCementBaseMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            bhzCementDetailMapper.deleteByMainId(id.toString());
            bhzCementBaseMapper.deleteById(id);
        }
    }

    @Override
    public List<BhzCementBase> selecthntbhzone(Integer id, Integer alertstate) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementBase> selecthntbhzones(Integer id, Integer alertstate) {
//		try {
//			QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
//			queryWrapper.ge("id", id);
//			queryWrapper.eq("alertstate", alertstate);
//			queryWrapper.last("limit 1");
//			return this.list(queryWrapper);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return bhzCementBaseMapper.selecthntbhzones(id, alertstate);
    }

    @Override
    public List<BhzCementBase> selecthntbhzonesx(Integer id, Integer alertstate) {
        return bhzCementBaseMapper.selecthntbhzonesx(id, alertstate);
    }

    @Override
    public List<BhzCementBase> selecthntbhzonesstatistics(Integer id, Integer statistics) {

        return bhzCementBaseMapper.selecthntbhzonesstatistics(id, statistics);
    }

    @Override
    public List<BhzCementBase> selecthntbhzList1(Integer id, Integer renwudanstatus) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("renwudanstatus", renwudanstatus);
            queryWrapper.last("limit 500");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementBase> selecthntbhzList(Integer id, Integer alertstate, List<String> shebeiNo) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.in("shebei_no", shebeiNo);
            queryWrapper.last("limit 1000");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementBase> selecthntbhzChaobiaoList(Integer alertstate, Integer overLevel) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.gt("over_level", overLevel);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzCementBase> selecthntbhzcbList(Integer id, Integer alertstate, String shebeiNo, Integer overLevel) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.gt("over_level", overLevel);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.in("shebei_no", shebeiNo);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updatehntbhzone(String batch_no, Integer alertstate) {

        return bhzCementBaseMapper.updatealertsate(batch_no, alertstate);

    }

    @Override
    public int updatehntbhzonestatics(String batch_no, Integer statistics) {
        return bhzCementBaseMapper.updatehntbhzonestatics(batch_no, statistics);
    }

    @Override
    public int updatehntbhzrenwustatus(String batch_no, Integer renwudanstatus) {
        return bhzCementBaseMapper.updatehntbhzrenwustatus(batch_no, renwudanstatus);
    }

    @Override
    public List<Map> ycltjlist(Integer pageNo, Integer pageSize) {

        return bhzCementBaseMapper.selectycltjList(pageNo, pageSize);

    }

    @Override
    public List<Map> ycltjlists(String shebeilist, Integer pageNo, Integer pageSize) {
        return bhzCementBaseMapper.selectycltjLists(shebeilist, pageNo, pageSize);
    }

    @Override
    public List<Map> ycltjliststatic(String shebeilist, Integer pageNo, Integer pageSize) {
        return bhzCementBaseMapper.ycltjliststatic(shebeilist, pageNo, pageSize);
    }

    @Override
    public List<BhzCementBase> selectbhzList(Integer curid) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzCementBase queryone(String shebeiNo) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebei_no", shebeiNo);
            queryWrapper.orderByDesc("save_datetime");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> bulletin() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return bhzCementBaseMapper.bulletin(loginUser.getOrgCode());
    }

    @Override
    public List<BhzCementBase> selectLists(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("shebei_no", shebeiNo);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<BhzCementBase> selectLists1(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectLists1(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectLists2(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectLists2(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListskz4(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListskz4(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsyb(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListsyb(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsrcsms(String shebeiNo) {
        return bhzCementBaseMapper.selectListsrcsms(shebeiNo);
    }

    @Override
    public List<BhzCementBase> selectLists1b(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectLists1b(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListszt(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListszt(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListssutai(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListssutai(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListyjqs(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListyjqs(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsbhg(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListsbhg(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsbhgty(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListsbhgty(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsbhgbhty(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListsbhgbhty(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selectListsbhgsg(String shebeiNo, Integer id) {
        return bhzCementBaseMapper.selectListsbhgsg(shebeiNo, id);
    }

    @Override
    public List<BhzCementBase> selecthntbhzlists(Integer curid, int i, String strsToList) {
        return bhzCementBaseMapper.selecthntbhzlists(curid, i, strsToList);
    }

    @Override
    public BhzCementBase queryones(String shebeiNo) {
        return bhzCementBaseMapper.queryones(shebeiNo);
    }

    @Override
    public List<BhzCementBase> selectListdata(String shebeilist, Integer curid) {
        return bhzCementBaseMapper.selectListdata(shebeilist, curid);
    }

    @Override
    public List<BhzCementBase> selectListstbim(String shebeilist, Integer curid) {
        return bhzCementBaseMapper.selectListstbim(shebeilist, curid);
    }

    @Override
    public List<BhzCementBase> selectListToSHYJ(String shebeilist, Integer curid) {
        return bhzCementBaseMapper.selectListToSHYJ(shebeilist, curid);
    }

    @Override
    public List<BhzCementBase> selectHntbhzList(Integer curid, int i, String strsToList1) {
        return bhzCementBaseMapper.selectHntbhzList(curid, i, strsToList1);
    }

    @Override
    public List<BhzCementBase> selecthntbhzonesstatistics1(Integer curid, int statistics, Integer curdate) {
        return bhzCementBaseMapper.selecthntbhzonesstatistics1(curid, statistics, curdate);
    }

    @Override
    public List<Map<String, Object>> getList(String start, String end, List<String> list,String strengthRank ) {
        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("shebei_no", "project_name as projectName", "strength_rank as strength", "sum(estimate_number) as totalCount", "product_datetime as productTime")
                .in(list != null, "shebei_no", list)
                .ge(start != null, "product_datetime", start)
                .le(end != null, "product_datetime", end)
                .eq(StringUtils.isNotBlank(strengthRank),"strength_rank",strengthRank)
                .groupBy("project_name", "strength_rank")
                .last(",LEFT (product_datetime,10)");
        List<Map<String, Object>> maps = bhzCementBaseMapper.selectMaps(queryWrapper);
        return maps;
    }

    @Override
    public BhzCementWarnVO selectWranCount(String orgCode) {
        return bhzCementBaseMapper.selectWranCount(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode) {
        return bhzCementBaseMapper.selectWranCountByorgcde(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByshebeino(String sysOrgCode, int i) {
        return bhzCementBaseMapper.selectWranCountByshebeino(sysOrgCode, i);
    }

    @Override
    public List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode) {
        return bhzCementBaseMapper.selectBiaoduanByshebeino(sysOrgCode);
    }

    @Override
    public List<String> selectByCode(String orgCode) {
        return bhzCementBaseMapper.selectByCode(orgCode);
    }

    @Override
    public Integer selectBiheCount(String orgCode) {
        return bhzCementBaseMapper.selectBiheCount(orgCode);
    }

    @Override
    public List<BhzCementBaseRC> selectTongjiData(String shebeiList) {
        return bhzCementBaseMapper.selectTongjiData(shebeiList);
    }

    @Override
    public List<BhzCementBase> selectcailiao(Integer id, Integer statistics) {
        return bhzCementBaseMapper.selectcailiao(id, statistics);
    }

    @Override
    public void selectcailiaostatics(String batchNo, int i) {
        bhzCementBaseMapper.selectcailiaostatics(batchNo, i);
    }

    @Override
    public String selectName(String handlePerson) {
        return bhzCementBaseMapper.selectName(handlePerson);
    }

    @Override
    public BhzCementBaseCbTongji selectCbTongji(String shebeilist) {
        return bhzCementBaseMapper.selectCbTongji(shebeilist);
    }

    @Override
    public List<BhzCementBaseCbTongji> selectshebeiBybiaoduan(String sys_depart_orgcode) {
        return bhzCementBaseMapper.selectshebeiBybiaoduan(sys_depart_orgcode);
    }

    @Override
    public List<String> selectshebeiByCode(String sys_depart_orgcode) {
        return bhzCementBaseMapper.selectshebeiByCode(sys_depart_orgcode);
    }

    @Override
    public List<Map> bhzcbv(Integer result, List<String> shebeilist) {
        return bhzCementBaseMapper.bhzcbv(result, shebeilist);
    }

    /**
     * @param result
     * @param shebeilist
     * @return
     */
    @Override
    public List<Map> bhzcailiaoCount(Integer result, List<String> shebeilist) {
        return bhzCementBaseMapper.bhzcailiaoCount(result, shebeilist);
    }

    @Override
    public List<Map> getDataToInitPM(String code, Integer lastId) {
        return baseMapper.getDataToInitPM(code, lastId);
    }

    @Override
    public List<BhzCementBase> getBatchNoByOrgCode(String code, Integer lastId) {
        List<BhzCementBase> result = bhzCementBaseMapper.selectBatchNoByOrgCode(code, lastId);
//        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("batch_no");
//        queryWrapper.gt("id", lastId);
//        queryWrapper.likeRight("sys_org_code", code);
//        queryWrapper.last("limit 100");
//        List<String> batchList = new ArrayList<>();
//        List<BhzCementBase> result = baseMapper.selectList(queryWrapper);
//        if (result != null && result.size() > 0) {
//            for (BhzCementBase item : result) {
//                batchList.add(item.getBatchNo());
//            }
//        }
        return result;
    }

    @Override
    public List<BhzCementBase> selectHntbhzList2(Integer curid, int i, String shebeilist) {
        return bhzCementBaseMapper.selectHntbhzList2(curid, i, shebeilist);
    }

    @Override
    public List<BhzCementBase> getBHAllDataToUpload(Integer lastId, List<String> list) {
        QueryWrapper<BhzCementBase> qw = new QueryWrapper<>();
        qw.in("shebei_no", list);
        qw.gt("id", lastId);
        qw.isNotNull("work_no");
        qw.orderByAsc("id");
        qw.last("limit 30");
        return baseMapper.selectList(qw);
    }

    @Override
    public List<BhzCementBase> selecthntbhzRC(Integer curid, int i) {
        return bhzCementBaseMapper.selecthntbhzRC(curid, i);
    }

    @Override
    public List<BhzCementBase> selecthntbhzbhRC(int cbStatistics, int bhStatus) {
        return bhzCementBaseMapper.selecthntbhzbhRC(cbStatistics, bhStatus);
    }

    @Override
    public String selectNameByCode(String orgCode) {
        return bhzCementBaseMapper.selectNameByCode(orgCode);
    }

    @Override
    public List<BhzCementBase> selectBhzChao(int id, int overproofStatus, String format) {
        return bhzCementBaseMapper.selectBhzChao(id, overproofStatus, format);
    }

    @Override
    public List<BhzCementBase> selectBhzChao2(int overproofStatus, String format, int isorder, int alertstate) {
        return bhzCementBaseMapper.selectBhzChao2(overproofStatus, format, isorder, alertstate);
    }

    @Override
    public String selectbyorgcode(String sysOrgCode, int i) {
        return bhzCementBaseMapper.selectbyorgcode(sysOrgCode, i);
    }

    @Override
    public List<BhzCementBase> selectByClientNo(String danhao) {
        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_no", danhao);
        return bhzCementBaseMapper.selectList(queryWrapper);
    }

    @Override
    public Integer findXiangMuZSs(List<String> querySheBeiList, String time) {
        return bhzCementBaseMapper.findXiangMuZSs(querySheBeiList, time);
    }

    @Override
    public Integer findYuJingSs(List<String> querySheBeiList, String time) {
        return bhzCementBaseMapper.findYuJingSs(querySheBeiList, time);
    }

    @Override
    public Integer findBiHeSs(List<String> querySheBeiList, String time) {
        return bhzCementBaseMapper.findBiHeSs(querySheBeiList, time);
    }

    @Override
    public List<BhzCementBase> selectByCheFlag(String shebeilist, Integer cheflag, Integer alertstate, Integer curid) {
        return bhzCementBaseMapper.selectByCheFlag(shebeilist, cheflag, alertstate, curid);
    }

    @Override
    public List<BhzCementBase> selectRCJHJD(Integer curid, String shebeilist) {
        return bhzCementBaseMapper.selectRCJHJD(curid, shebeilist);
    }

    @Override
    public String getSbname(String sbjno) {
        return bhzCementBaseMapper.getSbname(sbjno);
    }

    @Override
    public List<BhzCementBase> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeilist, Integer overLevel) {
        return bhzCementBaseMapper.selectBHZUnifiedProcess(curid, alertstate, shebeilist, overLevel);
    }

    @Override
    public Map selectSYSdepartId(String sysOrgCode) {
        return bhzCementBaseMapper.selectSYSdepartId(sysOrgCode);
    }

    @Override
    public List<BhzCementBase> selectHntbhzListBySTFour(Integer curid, int i, String shebeilist) {
        return bhzCementBaseMapper.selectHntbhzListBySTFour(curid, i, shebeilist);
    }

    @Override
    public Integer getCarCount(BhzCementBase bhzCementBase) {
        QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(1) cheflag");
        queryWrapper.eq("formula_no", bhzCementBase.getFormulaNo());
        queryWrapper.eq("shebei_no", bhzCementBase.getShebeiNo());
        queryWrapper.eq("panhao", "1");
        queryWrapper.eq("poure_location", bhzCementBase.getPoureLocation());
        queryWrapper.le("product_datetime", bhzCementBase.getProductDatetime());
        BhzCementBase bhzCementBase1 = bhzCementBaseMapper.selectOne(queryWrapper);
        return bhzCementBase1.getCheflag();
    }

    @Override
    public List<BhzCementBase> selectSCData(Integer curid, String shebeilist) {
        List<BhzCementBase> bhzCementBaseList = bhzCementBaseMapper.selectSCData(curid, shebeilist);
        return bhzCementBaseList;
    }

    //    @Override
//    public List<Map<Object, Object>> selectCaiLiaoUse(String shebeiNo) {
//        return bhzCementBaseMapper.selectCaiLiaoUse(shebeiNo);
//    }
    @Override
    public List<BhzCementTongJi> selectCaiLiaoUse(String shebeiNo, String start, String end) {
        return bhzCementBaseMapper.selectCaiLiaoUse(shebeiNo, start, end);
    }

    @Override
    public List<BhzCementBase> queryByPhb(String code) {
        try {
            QueryWrapper<BhzCementBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("formula_no", code);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map selectwarnContent(String bhz_cement_base, String sbjno) {
        return bhzCementBaseMapper.selectwarnContent(bhz_cement_base, sbjno);
    }

    @Override
    public List<Map<String, Object>> getQddj(String sysDepartOrgcode) {
        return bhzCementBaseMapper.getQddj(sysDepartOrgcode);
    }

    @Override
    public List<BhzCementBase> selecerenwudanpsgx(List<String> strings, String productDatetime_begin, String productDatetime_end) {
        return bhzCementBaseMapper.selecerenwudanpsgx(strings,productDatetime_begin,productDatetime_end);
    }

    @Override
    public List<String> selecerenw(String shebeiNo, String productDatetime_begin, String productDatetime_end) {
        return bhzCementBaseMapper.selecerenw(shebeiNo,productDatetime_begin,productDatetime_end);
    }

    @Override
    public List<String> selecerenws(List<String> shebeiNolist, String productDatetime_begin, String productDatetime_end) {
        return bhzCementBaseMapper.selecerenws(shebeiNolist,productDatetime_begin,productDatetime_end);
    }

    @Override
    public List<BhzCementBase> selectListjhjd(String shebeilist) {
        return bhzCementBaseMapper.selectListjhjd(shebeilist);
    }

    @Override
    public String getLastPhb(List<String> shebeiNolist) {
        return bhzCementBaseMapper.getLastPhb(shebeiNolist);
    }
}
