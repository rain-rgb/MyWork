package com.trtm.iot.syj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.syj.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Description: t_syjzb
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
public interface ITSyjzbService extends IService<TSyjzb> {
    /**
     * 对压力机进行数据查询
     *
     * @param pageNo     起始页
     * @param pageSize   每页所显示的数据量
     * @param sbbh       设备名称
     * @param pdjg       是否合格
     * @param syrq_begin 实验日期范围
     * @param syrq_end   结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperAnd(Integer pageNo, Integer pageSize, String sbbh, String
            pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx);

    /**
     * 对压力机超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperService(Integer pageNo, Integer pageSize, String sbbh_dictText, String
            pdjg, Date syrq_begin, Date syrq_end, String shebei, String shebeis);


    /**
     * 对抗压抗折数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    IPage<TSyjzb> defaultBtester(Integer pageNo, Integer pageSize, String sbbh_dictText,
                                 String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx);


    /**
     * 对抗压抗折超标数据查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param syrq_begin    实验日期范围
     * @param syrq_end      结束实验日期范围
     * @return
     */
    IPage<TSyjzb> defaultBtester2(Integer pageNo, Integer pageSize, String sbbh_dictText
            , Date syrq_begin, Date syrq_end, String shebeis);

    IPage<TSyjzb> queryWnjPageList(Integer pageNo, Integer pageSize, String sbbhDictText, String pdjg, Date syrq_begin, Date syrq_end, String shebeis, String sylx);


    IPage<TSyjzb> queryWnjChaoBiaoPageList(Integer pageNo, Integer pageSize, String sbbh, String pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    IPage<TSyjzb> queryWnjChaoBiaoCLPageList(Integer pageNo, Integer pageSize, String sbbh, String pdjg, Date syrq_begin, Date syrq_end, String shebeis);


    /**
     * 对恒应力一体机机进行数据查询
     *
     * @param pageNo     起始页
     * @param pageSize   每页所显示的数据量
     * @param sbbh       设备名称
     * @param pdjg       是否合格
     * @param syrq_begin 实验日期范围
     * @param syrq_end   结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperhylytjList(Integer pageNo, Integer pageSize, String sbbh, String
            pdjg, Date syrq_begin, Date syrq_end, String shebeis);

    /**
     * 对恒应力一体机机进行超标查询
     *
     * @param pageNo        起始页
     * @param pageSize      每页所显示的数据量
     * @param sbbh_dictText 设备名称
     * @param pdjg          是否合格
     * @param syrqee        实验日期范围
     * @param sywcsjs       结束实验日期范围
     * @return
     */
    IPage<TSyjzb> queryMapperhylytjList1(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg, Date syrqee, Date sywcsjs, String shebeis);

    /**
     * 试验机当月超标统计
     *
     * @return
     */
    List<Map<String, Object>> countList(String[] categorys);

    IPage<TSyjzb> queryMapperServicecl(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg, Date syrqee, Date sywcsjs, String shebei, String shebeis);

    IPage<TSyjzb> defaultBtesterdeal(Integer pageNo, Integer pageSize, String sbbh_dictText, Date syrqee, Date sywcsjs, String shebeis);

    IPage<TSyjzb> queryMapperhylytjdealList(Integer pageNo, Integer pageSize, String sbbh_dictText, String pdjg, Date syrqee, Date sywcsjs, String shebeis);

    public void saveMainWNJ(TSyjzb tSyjzb, List<fswangnj> fsWangnjList);

    public void saveMain(TSyjzb tSyjzb, List<FsYaliji> fYalijiList);

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

    List<TSyjzb> selectList(String shebei, Integer id);

    List<TSyjzb> selectsyjonesstatistics(Integer id, Integer statistics);

    int updateSyjzbOneStatistics(String syjid, int statistics);

    TSyjzb queryone(String shebeiNo);

    List<TSyjzb> selectLists(String shebeino, Integer id);

    List<TSyjzb> selectSyjListzlpz(String shebeino);

    List<TSyjzb> selectSyjListytwnd(String shebeino);

    List<TSyjzb> selectListData(String shebeilist);

    TSyjzb getsjbhData(String sjbh, String syjid, String sbbh);

    List<TSyjzb> selectListone(String shebeilist);

    List<TSyjzb> selectSyjList(String split, Integer curid);

    List<TSyjzb> selectSyjylList(String split, Integer curid);

    List<TSyjzb> selectSyjwnList(String split, Integer curid);

    List<TSyjzb> selectSyjListbl(String split, Integer curid);

    List<TSyjzb> selectStsqList(String split, Integer curid);

    List<TSyjzb> selectStsqList1(String split, Integer curid);

    List<TSyjzb> selectSTlist(String shebeilist, Integer curid);

    List<TSyjzb> selectYJQSlist(String shebeilist, Integer curid);

    List<TSyjzb> selectzj(String shebeilist, Integer curid);

    TSyjzb selectBySyjid(String syjid);

    List<SyjzbVo> queryLists(String sylx, String shebeilist, Integer curid);

    List<String> querySylx(Integer sbtype, String sysorgcode);

    List<TSyjzb> getCountWarning();

    List<TSyjzb> getCountBH();

    List<TSyjzb> getCountWarningByOC(String orgCode);

    List<TSyjzb> getCountBHByOC(String orgCode);

    List<String> getDayFromToday();

    String getDepartName(String sysOrgCode);

    String getSbtypeName(Integer sbtype);

    List<TSyjzb> selectListSy(Integer curid, List<String> strsToList1);

    String selectSylxName(String sylx);

    List<TSyjzb> selectSYJData(Integer curid, String shebeilist);

    List<fswangnj> selectWnjList(String syjid);

    List<FsYaliji> selectYljList(String syjid);

    List<TSyjzb> selectSYJYuJingData(Integer curid, String shebeilist);

    Map getPsPicBySyjid(Integer id);

    List<bzc> getBzcList(Integer pageNo, Integer pageSize,String shebei,String lq,String sjqd,String stadate,String enddate);

    int getBzcListcount(String shebei,String lq,String sjqd,String stadate,String enddate);

    List<TSyjzb> selectListToJTJT(String shebeilist, Integer curid);

    List<TSyjzb> seletListPanan351(String shebeilist);

    String getQrcode(String qrcode);
}
