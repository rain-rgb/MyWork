package com.trtm.iot.yclud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.yclud.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: 原材料使用详情Mapper
 * @Author: lis1
 * @Date:   2022-11-18
 * @Version: V1.0
 */
public interface YclUsageDetailMapper extends BaseMapper<YclUsageDetail> {

    Map getDepartProject(String code);

    List<Map> getYclUsa(String treeId, List<String> cLs);

    List<String> getInspection(String treeId, String orgCode, List<String> cLs);

    List<Map> getYclSy(String treeId, List<String> cailiaoNos, String guigexinghao, String pici);

    List<String> getWbsIdByPiCi(String pici, String orgCode);


    IPage<Map> getYclSys(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<Map> page, String startTime, String endTime);
    IPage<DetailVo> getDetails(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);
    IPage<DetailVo> getDetailsLc(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);

    List<NodeTypePc> getNodeType();
    List<NodeTypePc> getNodeType1();

    List<NodeTypePcGg> getNodeTypePG();
    List<NodeTypePcGg> getNodeType1PG();

    List<Map> getOrgCode(String treeId);

    List<String> getPc(String orgCode);

    List<String> getByGuiGeXH(String guiGeXingHao, String orgCode);

    List<String> getcailiaono(String nodeType);

    List<Map> getPiCi(List<String> cailiaonos, String orgCode);

    @Select("select cailiaoNo from ycl_cailiaodict where nodeType = #{nodetype}")
    List<String> getCailiaoByNodetype(String nodetype);

    IPage<DetailVo> getDetails1(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);
    IPage<DetailVo> getDetails1Lc(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);

    @Select("select sys_org_code from jt_tunnel_code where treeid = #{treeId}")
    String getTreeIdOrgCode(String treeId);





    List<Map> queryPiCi(String nodeType, String orgCode);

    List<NodeTypePcGg> queryNodeTypeLt100Gg(String orgCode);

    List<NodeTypePcGg> queryNodeTypeGt100Gg(String orgCode);

    List<GuiGeXingHao> queryGg(String nodeType, String orgCode);

    List<String> queryPiCiGe(String nodeType, String orgCode, String xingHao);

    List<NodeTypePc> queryNodeType(String orgCode);

    List<NodeTypePcGg> queryNodeType1(String orgCode);

    List<Map> getNodeTypeByUsa(String orgCode);

    @Select("select sum(uses) from ycl_usage_detail where inspection_lot_number = #{pici}")
    String getSumUse(String pici);

    IPage getDetailswbs(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);

    IPage getDetails1wbs(String treeId, List<String> list, String guigexinghao, String pici, String orgCode, Page<DetailVo> page, String startTime, String endTime, String nodeType);

    List<NodeTypePcGg> queryNodeTypewbs(String orgCode);

    List<NodeTypePcGg> queryNodeTypeLt100Ggwbs(String orgCode);

    List<NodeTypePcGg> queryNodeTypeGt100Ggwbs(String orgCode);

    List<GuiGeXingHao> queryGgwbs(String nodeType, String orgCode);

    List<String> queryPiCiGewbs(String nodeType, String orgCode, String xingHao);
}
