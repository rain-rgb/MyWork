package com.trtm.iot.wzcailiaonamedictman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarComponentMaterial.mapper.RebarComponentMaterialMapper;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.mapper.WzcailiaonamedictManMapper;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Service
public class WzcailiaonamedictManServiceImpl extends ServiceImpl<WzcailiaonamedictManMapper, WzcailiaonamedictMan> implements IWzcailiaonamedictManService {

    @Autowired
    WzcailiaonamedictManMapper wzcailiaonamedictManMapper;
    @Autowired
    private RebarComponentMaterialMapper rebarComponentMaterialMapper;

    @Override
    public List<WzcailiaonamedictMan> arrayOnecailiaos() {
        return wzcailiaonamedictManMapper.arrayOnecailiaos();
    }

    @Override
    public Map selectqueryone(String sysOrgCode) {
        return wzcailiaonamedictManMapper.selectqueryone(sysOrgCode);
    }

    @Override
    public WzcailiaonamedictMan queryselectone1(String cailiaono) {
        try {
            QueryWrapper<WzcailiaonamedictMan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cailiaoNo", cailiaono);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String selectBylmcailiaolx(int i) {
        return wzcailiaonamedictManMapper.selectBylmcailiaolx(i);
    }
    @Override
    public List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code) {
        return wzcailiaonamedictManMapper.selectByYCCP(cprule,ycrule,code);
    }

    @Override
    public List<String> selectCailiaoNoByName(String cailiaoName) {
        return  wzcailiaonamedictManMapper.selectCailiaoNoByName(cailiaoName);
    }

    @Override
    public List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code) {
        return wzcailiaonamedictManMapper.selectListByYCCP(cprule,ycrule,code);
    }
    @Override
    public List<MaterialVo> queryMaterialByComponentId(String id) {
        List<MaterialVo> list = new ArrayList<>();
        List<WzcailiaonamedictMan> wzcailiaonamedictMEN = wzcailiaonamedictManMapper.queryMaterialByComponentId(id);
        if (wzcailiaonamedictMEN!=null){
            for (WzcailiaonamedictMan wzcailiaonamedictMAN : wzcailiaonamedictMEN) {
                MaterialVo materialVo = new MaterialVo();
                materialVo.setMaterialName(wzcailiaonamedictMAN.getCailiaoname());
                materialVo.setMaterialId(wzcailiaonamedictMAN.getCailiaono());
                materialVo.setGuigexinghao(wzcailiaonamedictMAN.getGuigexinghao());
                materialVo.setNodeType(wzcailiaonamedictMAN.getNodetype());
                String number = rebarComponentMaterialMapper.queryMaterialNumber(id,wzcailiaonamedictMAN.getCailiaono().toString());
                materialVo.setMaterialNumber(number);
                Double weight = wzcailiaonamedictMAN.getWeight();
                if (weight!=null){
                    materialVo.setWeight(weight);
                }
                list.add(materialVo);
            }
        }

        return list;
    }

    @Override
    public List<WzcailiaonamedictMan> queryMaterialByNodeType(String nodeType) {
        return wzcailiaonamedictManMapper.queryMaterialByNodeType(nodeType);
    }

    @Override
    public List<WzcailiaonamedictMan> queryMaterialList(String sys_depart_orgcode, String materialName, String startDate, String endDate) {
        return wzcailiaonamedictManMapper.queryMaterialList(sys_depart_orgcode, materialName, startDate, endDate);
    }
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IRebarComponentMaterialService rebarComponentMaterialService;
    @Override
    public List<MaterialVo> getMaterialList(MaterialVo materialVo, Integer pageNo, Integer pageSize, String sys_depart_orgcode) {


        //获取所有构件
        List<WzcailiaonamedictMan> wzcailiaonamedictMEN = wzcailiaonamedictManService.queryMaterialList(sys_depart_orgcode,
                materialVo.getMaterialName(),
                materialVo.getStartDate(),
                materialVo.getEndDate());
        if (wzcailiaonamedictMEN == null) {
            return null;
        }
//        String componentNumber= rebarComponentTaskService.getComponentNumberBycomponentId(componentId);
        List<MaterialVo> materialVos = new ArrayList<>();
        for (WzcailiaonamedictMan wzcailiaonamedictMan : wzcailiaonamedictMEN) {
            MaterialVo materialVo1 = new MaterialVo();
            String cailiaono = wzcailiaonamedictMan.getCailiaono();
            materialVo1.setMaterialModel(wzcailiaonamedictMan.getGuigexinghao());
            materialVo1.setMaterialName(wzcailiaonamedictMan.getCailiaoname());
            materialVo1.setMaterialId(cailiaono);
            materialVo1.setWeight(wzcailiaonamedictMan.getWeight());
            materialVo1.setOrgCode(wzcailiaonamedictMan.getSysOrgCode());

            //从构件列表查材料数量
            String materialNumber= rebarComponentMaterialService.getMaterialNumberByMaterialId(cailiaono);
            if (materialNumber!=null){
                materialVo1.setMaterialNumber(materialNumber);
                //材料总重量
                if (materialVo1.getWeight()!=null){
                    materialVo1.setTotalWeight(Double.parseDouble(materialNumber)*materialVo1.getWeight());
                }
                materialVos.add(materialVo1);
            }
        }
        List<MaterialVo> collect = materialVos.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return collect;
    }

    @Override
    public WzcailiaonamedictMan getWzcailiaonamedictManById(String id) {
        return wzcailiaonamedictManMapper.getWzcailiaonamedictManById(id);
    }
}
