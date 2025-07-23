package com.trtm.iot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 设备审核表
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
@Service
public class ShebeiInfoServiceImpl extends ServiceImpl<ShebeiInfoMapper, ShebeiInfo> implements IShebeiInfoService {

    @Autowired
    private ShebeiInfoMapper ShebeiInfoMapper;

    @Override
    public List<ShebeiInfo> arrayOneshebei(String sys_org_code) {
        LambdaQueryWrapper<ShebeiInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShebeiInfo::getShebeiStatus, 1);
        String[] split = sys_org_code.split(",");
        for (int i = 0; i < split.length; i++) {
            lambdaQueryWrapper.likeRight(ShebeiInfo::getSysOrgCode, split[i]);
            if (i < split.length - 1) {
                lambdaQueryWrapper.or();
            }
        }
        return this.list(lambdaQueryWrapper);
        //return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().like("sys_org_code",sys_org_code).like("shebei_status",1));
    }

    @Override
    public List<ShebeiInfo> usershebeiList(String sys_org_code, List sbtype) {
        if (sys_org_code.equals("A05A01A12A13")) {
            return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_org_code).eq("shebei_status", 1).in("sbtype", sbtype).orderByDesc("sys_org_code"));
        }else {
            return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_org_code).eq("shebei_status", 1).ne("sys_org_code", "A05A01A12A13").in("sbtype", sbtype).orderByDesc("sys_org_code"));
        }
    }

    @Override
    public List<ShebeiInfo> usershebeiListByname(String sys_org_code, List sbtype) {
        return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_org_code).eq("shebei_status", 1).in("sbtype", sbtype).orderByAsc("sbname"));
    }

    @Override
    public List<ShebeiInfo> usershebeiListByname(String sys_org_code, List sbtype, String name) {
        return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_org_code).like(StringUtils.isNotBlank(name),"sbname",name).eq("shebei_status", 1).in("sbtype", sbtype).orderByAsc("sbname"));

    }

    @Override
    public List<ShebeiInfo> arrayOneshebeis() {
        return ShebeiInfoMapper.arrayOneshebeis();
    }

    @Override
    public List<ShebeiInfo> arrayOneshebeis(List<String> shebei, Integer sbtype) {
        QueryWrapper<ShebeiInfo> lambdaQueryWrapper = new QueryWrapper<>();
        lambdaQueryWrapper.eq("shebei_status", 1);
        lambdaQueryWrapper.eq("sbtype", sbtype);
        lambdaQueryWrapper.in("sbjno", shebei);
//        String[] split = shebei.split(",");
//        for(int i=0;i<split.length;i++){
//            lambdaQueryWrapper.eq("sbjno",split[i]);
//            if(i<split.length-1){
//                lambdaQueryWrapper.or();
//            }
//        }
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public ShebeiInfo selectshebeione(String sbjno) {
        try {
            QueryWrapper<ShebeiInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("sbjno",sbjno);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ShebeiInfo> SBlist() {
        return ShebeiInfoMapper.SBList();
    }

    /**
     * 根据设备编号查询设备信息
     *
     * @param sbjno
     * @return
     */
    @Override
    public ShebeiInfo SBJWD(String sbjno) {
        return ShebeiInfoMapper.SBJWD(sbjno);
    }

    /**
     * 根据设备编号修改设备信息状态
     *
     * @param sbjno
     * @param status
     * @return
     */
    @Override
    public Boolean updatestatus1(String sbjno, Integer status) {

        return ShebeiInfoMapper.updatestatus1(sbjno, status);
    }
    @Override
    public Boolean updatestatus(String sbjno, Integer status) {
        return ShebeiInfoMapper.updatestatus(sbjno, status);
    }

    @Override
    public List<ShebeiInfo> selectshebei() {
        return ShebeiInfoMapper.selectshebei();
    }

    @Override
    public List<ShebeiInfo> selectbhzone(Integer sbtype) {
//        try {
//            QueryWrapper<ShebeiInfo> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("sbtype",sbtype);
//            queryWrapper.eq("shebei_status",1);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return ShebeiInfoMapper.selectbhzone(sbtype);
    }

    @Override
    public List<ShebeiInfo> shebeilist(int sblx, String sysOrgCode) {
        try {
            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sbtype", sblx);
            queryWrapper.likeRight("sys_org_code", sysOrgCode);
            queryWrapper.eq("shebei_status", 1);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ShebeiInfo> arrayshebei(String sys_org_code) {
        return ShebeiInfoMapper.arrayshebei(sys_org_code);
    }

    @Override
    public Map<String, Object> selectshebeiones(String shebeibianhao) {
        try {
            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("a.sbjno as sbjno,b.depart_name as departName");
            queryWrapper.last("a left join sys_depart b on a.sys_org_code = b.org_code where a.sbjno = '" + shebeibianhao + "'");
            return this.getMap(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ShebeiInfo> selectbhzonelist() {
        return ShebeiInfoMapper.selectbhzonelist();
    }

    @Override
    public List<ShebeiInfo> usershebeiLists(String sys_depart_orgcode) {
        if (sys_depart_orgcode.equals("A05A01A12A13")) {
            return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_depart_orgcode).eq("shebei_status", 1).orderByDesc("sys_org_code"));
        } else{
            return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_depart_orgcode).eq("shebei_status", 1).ne("sys_org_code","A05A01A12A13").orderByDesc("sys_org_code"));
        }
    }

    @Override
    public List<ShebeiInfo> usershebeiLists(String sys_depart_orgcode, String name) {
        return ShebeiInfoMapper.selectList(new QueryWrapper<ShebeiInfo>().likeRight("sys_org_code", sys_depart_orgcode).like(StringUtils.isNotBlank(name),"sbname",name).eq("shebei_status", 1).orderByDesc("sys_org_code"));
    }

    @Override
    public List<ShebeiInfo> shebeilists(int sbtype, String orgcode) {
        return ShebeiInfoMapper.shebeilists(sbtype, orgcode);
    }

    @Override
    public List<ShebeiInfo> selectBhzshebei() {
        return ShebeiInfoMapper.selectBhzshebei();
    }

    @Override
    public List<ShebeiInfo> selectLists(String orgcode, String curid) {
        return ShebeiInfoMapper.selectLists(orgcode, curid);
    }

    @Override
    public List<String> selectShebeiList(String orgcode, int sbtype) {
        return ShebeiInfoMapper.selectShebeiList(orgcode, sbtype);
    }

    @Override
    public List<String> selectSbjnoListLikeOrgcode(String orgcode, int sbtype) {
        return ShebeiInfoMapper.selectSbjnoListLikeOrgcode(orgcode, sbtype);
    }

    @Override
    public List<String> selectShebeiNoList(String orgCode) {
        return ShebeiInfoMapper.selectShebeNoiList(orgCode);
    }


    public List<Map> getDataToInitPM(String code, int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sys_org_code");
        queryWrapper.in("sbtype", list);
        queryWrapper.likeRight("sys_org_code", code);
        queryWrapper.groupBy("sys_org_code");
        List<ShebeiInfo> deviceList = baseMapper.selectList(queryWrapper);

        List<String> orgCodeList = new ArrayList<>();
        if (deviceList != null && deviceList.size() > 0) {
            for (ShebeiInfo item : deviceList) {
                orgCodeList.add(item.getSysOrgCode());
            }
        }
        List<Map> data = baseMapper.getDataByOrgCode(orgCodeList);
        return data;
    }

    @Override
    public String selectSbnameBySbno(String shebeibianhao) {
        try {
            QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sbjno", shebeibianhao);
            ShebeiInfo one = this.getOne(queryWrapper);
            if (one != null) {
                return one.getSbname();
            } else {
                return shebeibianhao;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ShebeiInfo> getDataToInitCrew(String code, int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sys_org_code", "sbjno", "sbname", "create_by");
        queryWrapper.in("sbtype", list);
        queryWrapper.likeRight("sys_org_code", code);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ShebeiInfo> selectbyorgcode(String orgCode) {
        return ShebeiInfoMapper.selectbyorgcode(orgCode);
    }

    @Override
    public void upadteTuisongid(String sbid, Integer tsid) {
        ShebeiInfo shebeiInfo = new ShebeiInfo();
        shebeiInfo.setId(sbid);
        shebeiInfo.setTuisongid(tsid);
        baseMapper.updateById(shebeiInfo);

    }

    @Override
    public List<String> selectSBListByCodeList(String strsToList1) {
        return ShebeiInfoMapper.selectSBListByCodeList(strsToList1);
    }

    @Override
    public List<ShebeiInfo> selecegcsb(int sbtype, String orgcode) {
        QueryWrapper<ShebeiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("sys_org_code",orgcode);
        queryWrapper.eq("sbtype",sbtype);
        queryWrapper.eq("status",3);
        return ShebeiInfoMapper.selectList(queryWrapper);
    }

    @Override
    public String getEquipType(Integer sbtype) {
        return ShebeiInfoMapper.getEquipType(sbtype);
    }
}
