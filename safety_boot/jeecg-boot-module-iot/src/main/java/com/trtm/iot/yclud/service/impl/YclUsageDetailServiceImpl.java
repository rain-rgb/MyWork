package com.trtm.iot.yclud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.wzcailiaonamedict.mapper.WzcailiaonamedictMapper;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.mapper.WzcailiaonamedictManMapper;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.yclud.entity.*;
import com.trtm.iot.yclud.mapper.YclUsageDetailMapper;
import com.trtm.iot.yclud.service.IYclUsageDetailService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 原材料使用详情实现类
 * @Author: jeecg-boot
 * @Date: 2022-11-18
 * @Version: V1.0
 */
@Service
public class YclUsageDetailServiceImpl extends ServiceImpl<YclUsageDetailMapper, YclUsageDetail> implements IYclUsageDetailService {

    @Autowired
    private WzliaocangMapper wzliaocangMapper;
    @Autowired
    private YclUsageDetailMapper usageDetailMapper;
    @Autowired
    private WztaizhangMapper wztaizhangMapper;
    @Autowired
    private WzcailiaonamedictMapper wzcailiaonamedictMapper;
    @Autowired
    private WzcailiaonamedictManMapper manMapper;


    /**
     * 跟据检验批编号查询使用详情
     *
     * @param iLN
     * @return
     */
    @Override
    public YclUsageDetail getByILN(String iLN) {
        QueryWrapper<YclUsageDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_lot_number", iLN);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional
    public void add(YclUsageDetail yclUsageDetail) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        Double uses = Double.valueOf(yclUsageDetail.getUses()) / 1000;
        Wzliaocang wzliaocang = wzliaocangMapper.selectOne(new QueryWrapper<Wzliaocang>().eq("guid", yclUsageDetail.getStorageId()).last("for update"));
        if (((wzliaocang.getLjjinchang() - wzliaocang.getLjshiyong() + wzliaocang.getLjxiuzheng())) < uses) {
            throw new JeecgBootException("使用量不能超过库存量");
        }
        yclUsageDetail.setUses(String.valueOf(uses));
        yclUsageDetail.setDosingOrderNumber("LY-" + DateUtils.getDate("yyyyMMdd-HHmmss"));
        yclUsageDetail.setCreateBy(loginUser.getRealname());
        yclUsageDetail.setCreateTime(DateUtils.getDate());
        Map map = usageDetailMapper.getDepartProject(yclUsageDetail.getCode());
        yclUsageDetail.setTreeid(map.get("id").toString());
        yclUsageDetail.setProjectPart(map.get("depart_name").toString());
//        yclUsageDetail.setSysOrgCode(oConvertUtils.isNotEmpty(map.get("org_codes")) ? map.get("org_codes").toString() : loginUser.getOrgCode());
        if (oConvertUtils.isNotEmpty(yclUsageDetail.getInspectionLotNumber())) {
            Map noName = wztaizhangMapper.getNoName(yclUsageDetail.getStorageId(), yclUsageDetail.getInspectionLotNumber());
            yclUsageDetail.setStorageName(noName.get("cailiaoName").toString());
            yclUsageDetail.setCailiaono(noName.get("cailiaoNo").toString());
            yclUsageDetail.setGuigexinghao(noName.get("guigexinghao").toString());
            yclUsageDetail.setSysOrgCode(noName.get("sys_org_code").toString());
        }
        this.save(yclUsageDetail);

//        wzliaocang.setKucunxh(wzliaocang.getKucunxh() + new DecimalFormat("0.000").format(Double.valueOf(yclUsageDetail.getUses()) / 1000));
        wzliaocang.setLjshiyong(wzliaocang.getLjshiyong() + (Double.valueOf(yclUsageDetail.getUses()) / 1000));
        wzliaocangMapper.updateById(wzliaocang);
    }
    @Override
    @Transactional
    public void addRebarCaiLiao(YclUsageDetail yclUsageDetail) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//        Double uses = Double.valueOf(yclUsageDetail.getUses()) / 1000;
        Double uses = Double.valueOf(yclUsageDetail.getUses());
        Wzliaocang wzliaocang = wzliaocangMapper.selectOne(new QueryWrapper<Wzliaocang>().eq("guid", yclUsageDetail.getStorageId()).last("for update"));
        if (((wzliaocang.getLjjinchang() - wzliaocang.getLjshiyong() + wzliaocang.getLjxiuzheng())) < uses) {
            throw new JeecgBootException("使用量不能超过库存量");
        }
        yclUsageDetail.setUses(String.valueOf(uses));
        yclUsageDetail.setDosingOrderNumber("LY-" + DateUtils.getDate("yyyyMMdd-HHmmss"));
        yclUsageDetail.setCreateBy(loginUser.getRealname());
        yclUsageDetail.setCreateTime(DateUtils.getDate());
        yclUsageDetail.setShebeiNo("rebar");//钢筋加工场领料标识
        Map map = usageDetailMapper.getDepartProject(yclUsageDetail.getCode());//部位
        if (map!=null){
            yclUsageDetail.setTreeid(map.get("id").toString());
            yclUsageDetail.setProjectPart(map.get("depart_name").toString());
        }
//        yclUsageDetail.setSysOrgCode(oConvertUtils.isNotEmpty(map.get("org_codes")) ? map.get("org_codes").toString() : loginUser.getOrgCode());
        if (oConvertUtils.isNotEmpty(yclUsageDetail.getInspectionLotNumber())) {
            Map noName = wztaizhangMapper.getNoName(yclUsageDetail.getStorageId(), yclUsageDetail.getInspectionLotNumber());
            yclUsageDetail.setStorageName(noName.get("cailiaoName").toString());
            yclUsageDetail.setCailiaono(noName.get("cailiaoNo").toString());
            yclUsageDetail.setGuigexinghao(noName.get("guigexinghao").toString());
            yclUsageDetail.setSysOrgCode(noName.get("sys_org_code").toString());
        }
        this.save(yclUsageDetail);

//        wzliaocang.setKucunxh(wzliaocang.getKucunxh() + new DecimalFormat("0.000").format(Double.valueOf(yclUsageDetail.getUses()) / 1000));
        wzliaocang.setLjshiyong(wzliaocang.getLjshiyong() + (Double.valueOf(yclUsageDetail.getUses()) / 1000));
        wzliaocangMapper.updateById(wzliaocang);
    }

    @Override
    public List<Map> getYclUsa(String treeId, String nodeType) {
        List<String> cLs = null;
        if (oConvertUtils.isNotEmpty(nodeType)) {
            cLs = wzcailiaonamedictMapper.getcailiaoNoList(nodeType);
        }
        return usageDetailMapper.getYclUsa(treeId, cLs);
    }

    @Override
    public List<String> getInspection(String treeId, String orgCode, String nodeType) {
        List<String> cLs = null;
        if (oConvertUtils.isNotEmpty(nodeType)) {
            cLs = wzcailiaonamedictMapper.getcailiaoNoList(nodeType);
        }
        return usageDetailMapper.getInspection(treeId, orgCode, cLs);
    }

    @Override
    public List getAllSy(String treeId) {
        return usageDetailMapper.getYclSy(treeId, null, null, null);
    }

    @Override
    public List getSNSy(String treeId, String nodeType, String guigexinghao, String pici) {
        if (oConvertUtils.isEmpty(nodeType)) {
            nodeType = "6";
        }
        List<String> list = wzcailiaonamedictMapper.getcailiaoNoList(nodeType);
        return usageDetailMapper.getYclSy(treeId, list, guigexinghao, pici);
    }

    @Override
    public List<String> getWbsIdByPiCi(String pici, String orgCode) {
        return usageDetailMapper.getWbsIdByPiCi(pici, orgCode);
    }

    @Override
    public IPage getSNSys(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type) {
        Page<Map> page = new Page<>(pageNo, pageSize);
        List<String> list = null;

        if (oConvertUtils.isNotEmpty(type)) {
            Integer value = Integer.valueOf(type);
            List types = getNodeType(value);
            list = manMapper.getcailiaoNo(types);
        }
        if (oConvertUtils.isNotEmpty(nodeType)) {
            list = wzcailiaonamedictMapper.getcailiaoNoList(nodeType);
        }

        IPage<Map> mapIPage = usageDetailMapper.getYclSys(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime);
        for (Map record : mapIPage.getRecords()) {
            String uses = record.get("uses").toString();
            if (oConvertUtils.isNotEmpty(record.get("guobangleibie"))) {
                String guobangleibie = record.get("guobangleibie").toString();
                record.put("uses", uses + guobangleibie);
            } else {
                record.put("uses", uses + "t");
            }
        }
        return mapIPage;
    }

    private List getNodeType(Integer status) {
        List<String> yuancaiList = new ArrayList<>();
        List<String> chanpinList = new ArrayList<>();
//        QueryWrapper<WzcailiaonamedictMan> wrapper = new QueryWrapper<>();
//        wrapper.select("nodeType").isNotNull("nodeType").groupBy("nodeType");
        List<WzcailiaonamedictMan> manList = manMapper.getNodeType();
        for (WzcailiaonamedictMan man : manList) {
            String nodeType = man.getNodetype();
            if (oConvertUtils.isNotEmpty(nodeType) && Integer.valueOf(nodeType) <= 100) {
                yuancaiList.add(nodeType);
            }
            if (oConvertUtils.isNotEmpty(nodeType) && Integer.valueOf(nodeType) > 100) {
                chanpinList.add(nodeType);
            }
        }
        if (status == 1) { //原材
            return yuancaiList;
        }
        if (status == 2) { //产品
            return chanpinList;
        }
        return null;
    }

    @Override
    public List<String> getGgXh(String nodeType, String orgCode) {
        return wzcailiaonamedictMapper.getGgXh(nodeType, orgCode);
    }

    @Override
    public List<NodeTypePc> getNodeTypePc(String orgCode, String treeId, String type) {
        List<NodeTypePc> list = usageDetailMapper.queryNodeType(orgCode);
        if (oConvertUtils.isNotEmpty(list) && list.size() > 0) {
            for (NodeTypePc nodeTypePc : list) {
                List<String> cLs = null;
                if (oConvertUtils.isNotEmpty(nodeTypePc.getNodeType())) {
                    cLs = wzcailiaonamedictMapper.getcailiaoNoList(nodeTypePc.getNodeType());
                }
                List<String> pici = usageDetailMapper.getInspection(null, orgCode, cLs);
                nodeTypePc.setPici(pici);
            }
        }
        return list;
    }

    @Override
    public List<NodeTypePcGg> getNodeTypePcGg(String orgCode, String treeId, String type) {
        List<NodeTypePcGg> list = null;
        if (oConvertUtils.isEmpty(type)) {
            list = usageDetailMapper.queryNodeType1(orgCode);
        } else {
            if (Integer.valueOf(type) == 1) {
                list = usageDetailMapper.queryNodeTypeLt100Gg(orgCode);
            } else if (Integer.valueOf(type) == 2) {
                list = usageDetailMapper.queryNodeTypeGt100Gg(orgCode);
            }
        }
        if (oConvertUtils.isNotEmpty(list) && list.size() > 0) {
            for (NodeTypePcGg nodeTypePcGg : list) {
                String nodeType = nodeTypePcGg.getNodeType();
                List<GuiGeXingHao> guiGeXingHaos = usageDetailMapper.queryGg(nodeType, orgCode);
                for (GuiGeXingHao xingHao : guiGeXingHaos) {
                    if (oConvertUtils.isNotEmpty(xingHao)) {
                        List<String> pici = usageDetailMapper.queryPiCiGe(nodeType, orgCode, xingHao.getGuiGeXingHao());
                        xingHao.setPici(pici);
                    }
                }
                nodeTypePcGg.setGgXh(guiGeXingHaos);
            }
        }
        return list;
    }

    @Override
    public List<NodeTypePcGg> getNodeTypePcGgwbs(String orgCode, String treeId, String type) {
        List<NodeTypePcGg> list = null;
        if (oConvertUtils.isEmpty(type)) {
            list = usageDetailMapper.queryNodeTypewbs(orgCode);
        } else {
            if (Integer.valueOf(type) == 1) {
                list = usageDetailMapper.queryNodeTypeLt100Ggwbs(orgCode);
            } else if (Integer.valueOf(type) == 2) {
                list = usageDetailMapper.queryNodeTypeGt100Ggwbs(orgCode);
            }
        }
        if (oConvertUtils.isNotEmpty(list) && list.size() > 0) {
            for (NodeTypePcGg nodeTypePcGg : list) {
                String nodeType = nodeTypePcGg.getNodeType();
                List<GuiGeXingHao> guiGeXingHaos = usageDetailMapper.queryGgwbs(nodeType, orgCode);
                for (GuiGeXingHao xingHao : guiGeXingHaos) {
                    if (oConvertUtils.isNotEmpty(xingHao)) {
                        List<String> pici = usageDetailMapper.queryPiCiGewbs(nodeType, orgCode, xingHao.getGuiGeXingHao());
                        xingHao.setPici(pici);
                    }
                }
                nodeTypePcGg.setGgXh(guiGeXingHaos);
            }
        }
        return list;
    }

    @Override
    public List<String> getPic(String treeId) {
        String orgCode = "";
        List<Map> maps = usageDetailMapper.getOrgCode(treeId);
        for (Map map : maps) {
            if (!map.get("id").toString().contains("rc")) {
                orgCode = map.get("orgCode").toString();
            }
        }
        List<String> pici = usageDetailMapper.getPc(orgCode);
        return pici;
    }

    @Override
    public List<String> getCailiaoByNodetype(String nodetype) {
        return usageDetailMapper.getCailiaoByNodetype(nodetype);
    }

    @Override
    public IPage getDetails(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type) {
        Page<DetailVo> page = new Page<>(pageNo, pageSize);
        List<String> list = null;

//        if (oConvertUtils.isNotEmpty(nodeType)) {
//            list = wzcailiaonamedictMapper.getcailiaoNoList(nodeType);
//        }
        if (Integer.valueOf(type) == 1) {
            return usageDetailMapper.getDetails(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        } else if (Integer.valueOf(type) == 2) {
            return usageDetailMapper.getDetails1(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        }
        return null;
    }



    @Override
    public IPage getDetailsWbs(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type) {
        Page<DetailVo> page = new Page<>(pageNo, pageSize);
        List<String> list = null;

        if (Integer.valueOf(type) == 1) {
            return usageDetailMapper.getDetailswbs(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        } else if (Integer.valueOf(type) == 2) {
            return usageDetailMapper.getDetails1wbs(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        }
        return null;
    }

    @Override
    public IPage getDetailsLc(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type) {
        Page<DetailVo> page = new Page<>(pageNo, pageSize);
        List<String> list = null;
        if (Integer.valueOf(type) == 1) {
            return usageDetailMapper.getDetailsLc(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        } else if (Integer.valueOf(type) == 2) {
            return usageDetailMapper.getDetails1Lc(treeId, list, guigexinghao, pici, orgCode, page, startTime, endTime, nodeType);
        }
        return null;
    }

    @Override
    public String getSumUse(String pici) {
        return usageDetailMapper.getSumUse(pici);
    }

    @Override
    public String getTreeIdOrgCode(String treeId) {
        return usageDetailMapper.getTreeIdOrgCode(treeId);
    }

    @Override
    public List<Map> getNodeTypeByUsa(String orgCode) {
        return usageDetailMapper.getNodeTypeByUsa(orgCode);
    }


}





