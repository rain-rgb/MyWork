package com.trtm.iot.syj.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.attachlist.entity.Attachlist;
import com.trtm.iot.syj.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.*;

import java.util.Date;


import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Mapper
public interface TSyjzbMapper extends BaseMapper<TSyjzb> {
    /**
     * 对压力机进行数据查询
     *
     * @param page
     * @param sbbh   设备名称
     * @param pdjg   是否合格
     * @param SYRQ   实验日期范围
     * @param SYWCSJ 结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperAnd(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                 @Param("pdjg") String pdjg, @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebeis, String sylx);

    /**
     * 对压力机进行超标数据查询
     *
     * @param page
     * @param sbbh   设备名称
     * @param pdjg   是否合格
     * @param SYRQ   实验日期范围
     * @param SYWCSJ 结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperService(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                     @Param("pdjg") String pdjg, @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebei, String shebeis);

    /**
     * 对压力机进行超标数据处理
     *
     * @param page
     * @param sbbh   设备名称
     * @param pdjg   是否合格
     * @param SYRQ   实验日期范围
     * @param SYWCSJ 结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperServicecl(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                       @Param("pdjg") String pdjg, @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebei, String shebeis);

    /**
     * 查询抗压抗折数据查询
     *
     * @param page
     * @param sbbh
     * @param SYRQ
     * @param SYWCSJ
     * @return
     */
    IPage<TSyjzb> defaultBtester(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                 @Param("pdjg") String pdjg, @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebeis, String sylx);

    /**
     * 查询抗压抗折超标数据查询
     *
     * @param page
     * @param sbbh
     * @param SYRQ
     * @param SYWCSJ
     * @return
     */
    IPage<TSyjzb> defaultBtester2(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                  @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebeis);

    /**
     * 查询抗压抗折超标数据查询
     *
     * @param page
     * @param sbbh
     * @param SYRQ
     * @param SYWCSJ
     * @return
     */
    IPage<TSyjzb> defaultBtesterdeal(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                     @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebeis);

    /**
     * 万能机分页查询
     *
     * @param page
     * @return
     */
    IPage<TSyjzb> queryWnjPageList(Page<TSyjzb> page, @Param("sbbh") String sbbh, @Param("pdjg") String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx);

    IPage<TSyjzb> queryWnjChaoBiaoPageList(Page<TSyjzb> page, @Param("sbbh") String sbbh, @Param("pdjg") String pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    IPage<TSyjzb> queryWnjChaoBiaoCLPageList(Page<TSyjzb> page, @Param("sbbh") String sbbh, @Param("pdjg") String pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    /**
     * 对恒应力一体机机进行数据查询
     *
     * @param page
     * @param sbbh   设备名称
     * @param pdjg   是否合格
     * @param SYRQ   实验日期范围
     * @param SYWCSJ 结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperhylytjList(Page<TSyjzb> page, @Param("sbbh") String sbbh,
                                        @Param("pdjg") String pdjg, @Param("SYRQ") Date SYRQ, @Param("SYWCSJ") Date SYWCSJ, String shebeis);

    IPage<TSyjzb> queryMapperhylytjList1(Page<TSyjzb> page, String sbbh_dictText, String pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    IPage<TSyjzb> queryMapperhylytjdealList(Page<TSyjzb> page, String sbbh_dictText, String pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    List<Map<String, Object>> countList(@Param("orgCode") String orgCode, @Param("categorys") String[] categorys);

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    Map stsPageLists(String shebeis);

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    Map stsPageLists1(String shebeis);

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     *
     * @return
     */
    Map stsPageLists2(String shebeis);


    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计/处置数
     *
     * @return
     */
    Map stsPageLists3(String shebeis);

    Map stsPageLists4(String shebeis);

    Map stsPageLists5(String shebeis);

    List<TSyjzb> selectsyjonesstatistics(Integer id, Integer statistics);

    List<TSyjzb> selectLists(String shebeiNo, Integer id);

    @Update("update t_syjzb set statistics=#{statistics} where syjid=#{syjid}")
    int updateSyjzbOneStatistics(String syjid, int statistics);

    TSyjzb queryone(String shebeiNo);

    List<TSyjzb> selectListData(String shebeilist);

    TSyjzb getsjbhData(String sjbh, String syjid, String sbbh);

    List<TSyjzb> selectListone(String shebeilist);

    List<TSyjzb> selectSyjList(String shebeilist, Integer curid);

    List<TSyjzb> selectSyjylList(String shebeilist, Integer curid);

    List<TSyjzb> selectSyjwnList(String shebeilist, Integer curid);

    List<TSyjzb> selectSyjListbl(String shebeilist, Integer curid);

    List<TSyjzb> selectStsqList(String shebeilist, Integer curid);

    List<TSyjzb> selectStsqList1(String shebeilist, Integer curid);

    List<TSyjzb> selectSTlist(String shebeilist, Integer curid);

    List<TSyjzb> selectYJQSlist(String shebeilist, Integer curid);

    List<TSyjzb> selectSyjListzlpz(String shebeilist);

    List<TSyjzb> selectSyjListytwnd(String shebeilist);

    List<TSyjzb> selectzj(String shebeilist, Integer curid);

    @Select("select * from t_syjzb where syjid = #{syjid}")
    TSyjzb selectBySyjid(String syjid);

    List<SyjzbVo> queryLists(String sylx, String shebeilist, Integer curid);

    List<String> querySylx(Integer sbtype, String sysorgcode);

    List<TSyjzb> getCountWarningByOC(String orgCode);

    List<TSyjzb> getCountBHByOC(String orgCode);

    List<TSyjzb> getCountWarning();

    List<TSyjzb> getCountBH();

    @Select("select parent_id from sys_depart where org_code = #{sysOrgCode}")
    String getDepartParentId(String sysOrgCode);

    @Select("select * from sys_depart where id = #{departParentId}")
    SysDepart getDepart(String departParentId);

    List<TSyjzb> selectListSy(Integer curid, List<String> strsToList1);

    @Select("select item_text from sys_dict_item where dict_id = (select id from sys_dict where dict_code = 'SYLX')  and item_value = #{sylx}")
    String selectSylxName(String sylx);

    int findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    int findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    int findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    List<TSyjzb> selectSYJData(Integer curid, String shebeilist);

    @Select("select * from f_wangnj where syjid = #{syjid}")
    List<fswangnj> selectWnjList(String syjid);

    @Select("select * from f_yaliji where syjid = #{syjid}")
    List<FsYaliji> selectYljList(String syjid);

    List<TSyjzb> selectSYJYuJingData(Integer curid, String shebeilist);

    @Select("select * from f_yaliji where syjid = #{syjid} and sjxh like concat('%',#{sjxh},'%')")
    FsYaliji selectYlj(String syjid, String sjxh);

    @Select("select * from attachlist where objectid = #{spic}")
    Attachlist getAttach(String spic);

    @Select("select facePic from t_syjzb where syjid = #{syjid}")
    String getFacePic(String syjid);

    @Select("select * from f_yaliji where id = #{id}")
    FYaliji getYljById(Integer id);

    List<bzc> getBzcList(Integer pageNo, Integer pageSize,String shebei,String lq,String sjqd,String stadate,String enddate);

    int getBzcListcount(String shebei,String lq,String sjqd,String stadate,String enddate);

    List<TSyjzb> selectListToJTJT(String shebeilist, Integer curid);

    List<TSyjzb> seletListPanan351(String shebeilist);
}

