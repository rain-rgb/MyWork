package com.trtm.iot.yclud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.yclud.entity.NodeTypePc;
import com.trtm.iot.yclud.entity.NodeTypePcGg;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 原材料使用详情service
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
public interface IYclUsageDetailService extends IService<YclUsageDetail> {
    YclUsageDetail getByILN(String iLN);

    void add(YclUsageDetail yclUsageDetail);
    void addRebarCaiLiao(YclUsageDetail yclUsageDetail);

    List<Map> getYclUsa(String treeId, String nodeType);

    List<String> getInspection(String treeId, String orgCode, String nodeType);

    List getAllSy(String treeId);

    List getSNSy(String treeId, String nodeType, String guigexinghao, String pici);

    List<String> getWbsIdByPiCi(String pici, String orgCode);

    IPage getSNSys(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type);

    List<String> getGgXh(String nodeType, String orgCode);

    List<NodeTypePc> getNodeTypePc(String orgCode, String treeId, String type);

    List<NodeTypePcGg> getNodeTypePcGg(String orgCode, String treeId, String type);

    List<NodeTypePcGg> getNodeTypePcGgwbs(String orgCode, String treeId, String type);

    List<String> getPic(String treeId);

    List<String> getCailiaoByNodetype(String nodetype);

    IPage getDetails(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type);

    String getTreeIdOrgCode(String treeId);

    List<Map> getNodeTypeByUsa(String orgCode);

    IPage getDetailsWbs(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type);

    IPage getDetailsLc(String treeId, String nodeType, String guigexinghao, String pici, String orgCode, Integer pageNo, Integer pageSize, String startTime, String endTime, String type);

    String getSumUse(String pici);
}
