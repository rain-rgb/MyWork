package com.trtm.iot.lqbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqBasesDayYL;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.entity.BhzLqWarnVO;
import com.trtm.iot.lqbhz.mapper.BhzLqCailiaoMapper;
import com.trtm.iot.lqbhz.mapper.BhzLqBasesMapper;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.vo.BhzLqCLDC;
import com.trtm.iot.lqbhz.vo.BhzLqTongJi;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date: 2021-02-22
 * @Version: V1.0
 */
@Service
public class BhzLqBasesServiceImpl extends ServiceImpl<BhzLqBasesMapper, BhzLqBases> implements IBhzLqBasesService {

    @Autowired
    private BhzLqBasesMapper bhzLqBasesMapper;
    @Autowired
    private BhzLqCailiaoMapper bhzLqCailiaoMapper;

    @Override
    @Transactional
    public void saveMain(BhzLqBases bhzLqBases, List<BhzLqCailiao> bhzLqCailiaoList) {
        bhzLqBasesMapper.insert(bhzLqBases);
        if (bhzLqCailiaoList != null && bhzLqCailiaoList.size() > 0) {
            for (BhzLqCailiao entity : bhzLqCailiaoList) {
                //外键设置
                entity.setBaseGuid(bhzLqBases.getGuid());
                bhzLqCailiaoMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(BhzLqBases bhzLqBases, List<BhzLqCailiao> bhzLqCailiaoList) {
        bhzLqBasesMapper.updateById(bhzLqBases);

        //1.先删除子表数据
        bhzLqCailiaoMapper.deleteByMainId(bhzLqBases.getGuid());

        //2.子表数据重新插入
        if (bhzLqCailiaoList != null && bhzLqCailiaoList.size() > 0) {
            for (BhzLqCailiao entity : bhzLqCailiaoList) {
                //外键设置
                entity.setBaseGuid(bhzLqBases.getGuid());
                bhzLqCailiaoMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        bhzLqCailiaoMapper.deleteByMainId(id);
        bhzLqBasesMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            bhzLqCailiaoMapper.deleteByMainId(id.toString());
            bhzLqBasesMapper.deleteById(id);
        }
    }

    /**
     * 根据条件查出对应的数据
     *
     * @param id
     * @param alertstate
     * @return
     */
    @Override
    public List<BhzLqBases> selectlqbhzone(Integer id, Integer alertstate) {
        try {
            QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件查出对应的数据
     *
     * @param id
     * @param alertstate
     * @return
     */
    @Override
    public List<BhzLqBases> selectlqbhzones(Integer id, Integer alertstate) {
//		try{
//			QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
//			queryWrapper.ge("id",id);
//			queryWrapper.eq("alertstate",alertstate);
//			queryWrapper.last("limit 1");
//			return this.list(queryWrapper);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
        return bhzLqBasesMapper.selectlqbhzones(id, alertstate);
    }

    @Override
    public List<BhzLqBases> selectlqbhzonesg(Integer id, Integer alertstate, String shebeiNo) {
        return bhzLqBasesMapper.selectlqbhzonesg(id, alertstate, shebeiNo);
    }

    @Override
    public int updatelqbhzone(String guid, Integer alertstate) {

        return bhzLqBasesMapper.updatealertaste(guid, alertstate);
    }


    /**
     * 根据信息修改沥青搅拌时间
     */
    @Override
    public void updateLqOverTime(int time, String guid) {
        bhzLqBasesMapper.updateLqOverime(time, guid);
    }

    /**
     * 根据主表guid查询子表材料信息
     */
    @Override
    public List<BhzLqCailiao> selectCailiaoList(String guid) {

        return bhzLqBasesMapper.selectCailiaoList(guid);
    }

    /**
     * 根据guid修改总产量
     */
    @Override
    public void updateZclByGuid(String guid, Double zongchanliang) {
        bhzLqBasesMapper.updateZclByGuid(guid, zongchanliang);
    }

    /**
     * 根据guid修改油石比
     *
     * @param guid
     */
    @Override
    public void updateYsbByGuid(String guid, String youshibi) {
        bhzLqBasesMapper.updateYsbByGuid(guid, youshibi);
    }

    /**
     * 根据guid将理论油石比写入数据库
     *
     * @param lilunpb
     * @param guid
     */
    @Override
    public void updateLlysbByGuid(String lilunpb, String guid) {
        bhzLqBasesMapper.updateLlysbByGuid(lilunpb, guid);
    }

    @Override
    public void updateGcmcByGuid(String gcmc, String guid) {
        bhzLqBasesMapper.updateGcmcByGuid(gcmc, guid);
    }

    @Override
    public void updatehunheliaoidByGuid(String hunheliaoid, String guid) {
        bhzLqBasesMapper.updatehunheliaoidByGuid(hunheliaoid, guid);
    }

    /**
     * 修改bhz_lq_bases的数据的状态
     *
     * @param id
     * @param alertstate
     */
    @Override
    public void updateBaseStatus(Integer id, int alertstate) {
        bhzLqBasesMapper.updateBaseStatus(id, alertstate);
    }

    @Override
    public List<BhzLqBases> selectlqbhzList(Integer id, Integer alertstate, String shebeiNo) {
        try {
            QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.in("shebeibianhao", shebeiNo);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzLqBases> selectlqbhzBysb(Integer id, List<String> shebeiNo) {
        try {
            QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.in("shebeibianhao", shebeiNo);
            queryWrapper.last("limit 50");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzLqBases> selectlqbhzchaobiaoList(Integer id, Integer alertstate, String shebeiNo) {
        try {
            QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", id);
            queryWrapper.eq("alertstate", alertstate);
            queryWrapper.in("shebeibianhao", shebeiNo);
            queryWrapper.ge("chaobiaodengji", 1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzLqBases queryone(String shebeiNo) {
        return bhzLqBasesMapper.queryone(shebeiNo);
    }

    @Override
    public BhzLqBases selectBysbno(List<String> SBList) {
        try {
            QueryWrapper<BhzLqBases> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("shebeibianhao", SBList);
            queryWrapper.orderByDesc("chuliaoshijian");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BhzLqWarnVO selectWranCount(String orgCode) {
        return bhzLqBasesMapper.selectWranCount(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode) {

        return bhzLqBasesMapper.selectWranCountByorgcde(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByshebeino(String code, int i) {
        return bhzLqBasesMapper.selectWranCountByshebeino(code, i);
    }

    @Override
    public List<BhzLqBases> selectList1(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectList1(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectListTY(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectListTY(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectListSG(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectListSG(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectList2(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectList2(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectListtoDJ(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectListtoDJ(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectUpdateList(String shebeiNo) {
        return bhzLqBasesMapper.selectUpdateList(shebeiNo);
    }

    @Override
    public List<BhzLqBases> selectListtoHC(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectListtoHC(shebeiNo, id);
    }

    @Override
    public List<BhzLqBases> selectcbList(String shebeiNo, Integer id) {
        return bhzLqBasesMapper.selectcbList(shebeiNo, id);
    }

    @Override
    public List<String> getPitchGraphData(BhzLqBases pitchBase, Integer code) {
        Map<String, String[]> map = new HashMap<>();
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(pitchBase, map);
        if (code < 50) {
            List<String> modifiedExclude = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                modifiedExclude.add(String.valueOf(i));
            }
            queryWrapper.notIn("hunheliaoid", modifiedExclude);
        } else {
            List<String> generalExclude = new ArrayList<>();
            for (int i = 50; i < 70; i++) {
                generalExclude.add(String.valueOf(i));
            }
            queryWrapper.notIn("hunheliaoid", generalExclude);
        }
        queryWrapper.orderByDesc("chuliaoshijian");
        queryWrapper.last("limit 0,15");
        List<BhzLqBases> result = bhzLqBasesMapper.selectList(queryWrapper);
        List<String> list = new ArrayList<>();
        if (result != null && result.size() > 0) {
            for (BhzLqBases item : result) {
                list.add(item.getChuliaowd() == null ? "" : item.getChuliaowd());
            }
        }
        return list;
    }

    @Override
    public List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode) {
        return bhzLqBasesMapper.selectBiaoduanByshebeino(sysOrgCode);
    }

    @Override
    public Integer selectBiheCount(String orgCode) {
        return bhzLqBasesMapper.selectBiheCount(orgCode);
    }

    @Override
    public Map selectwarnContent(String tableName, String sbjno) {
        return bhzLqBasesMapper.selectwarnContent(tableName, sbjno);
    }

    @Override
    public List<Map> selectProjectList(String sys_org_code, int i) {
        return bhzLqBasesMapper.selectProjectList(sys_org_code, i);
    }

    @Override
    public Double selectOverCount(List<String> shebeiList, String tableName, String sql) {
        return bhzLqBasesMapper.selectOverCount(shebeiList, tableName, sql);
    }

    @Override
    public List<BhzLqBases> selectLQBHZ(Integer curid, String shebeilist) {
        return bhzLqBasesMapper.selectLQBHZ(curid, shebeilist);
    }

    @Override
    public List<BhzLqBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel) {
        return bhzLqBasesMapper.selectBHZUnifiedProcess(curid, alertstate, shebeiList, overLevel);
    }

    @Override
    public List<BhzLqBases> selectLQBHZRoad(Integer curid, String shebeilist) {
        return bhzLqBasesMapper.selectLQBHZRoad(curid, shebeilist);
    }

    @Override
    public List<BhzLqBasesDayYL> getDosage(String formulaNo, String date) {
        return bhzLqBasesMapper.getDosage(formulaNo, date);
    }

    @Override
    public List<BhzCementBaseRC> selectTongjiData(String shebeilist) {
        return bhzLqBasesMapper.selectTongjiData(shebeilist);
    }

    @Override
    public List<BhzLqTongJi> selectCailiaoUse(String shebei, String start, String end) {
        return bhzLqBasesMapper.selectCailiaoUse(shebei, start, end);
    }

    @Override
    public List<BhzLqCLDC> selectdcdata(String shebeibianhao, String chuliaoshijian_begin, String chuliaoshijian_end, String projectName, String hunheliaoid) {
        return bhzLqBasesMapper.selectdcdata(shebeibianhao, chuliaoshijian_begin, chuliaoshijian_end, projectName, hunheliaoid);
    }
}
