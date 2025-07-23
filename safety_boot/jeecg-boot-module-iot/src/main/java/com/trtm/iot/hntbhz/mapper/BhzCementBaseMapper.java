package com.trtm.iot.hntbhz.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.vo.BhzCementBaseCbTongji;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.hntbhz.vo.BhzCementTongJi;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站主表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
public interface BhzCementBaseMapper extends BaseMapper<BhzCementBase> {


    @Update("update bhz_cement_base set alertstate=#{alertstate} where batch_no=#{batch_no}")
    int updatealertsate(String batch_no, Integer alertstate);

    @Update("update bhz_cement_base set statistics=#{statistics} where batch_no=#{batch_no}")
    int updatehntbhzonestatics(String batch_no, Integer statistics);

    @Select("SELECT bhz_cement_detail.materiale_name,sum( bhz_cement_detail.reality_number ) AS realityNumber FROM bhz_cement_base JOIN bhz_cement_detail ON bhz_cement_base.batch_no = bhz_cement_detail.batch_no where shebei_no in ('A05A03A01A01_BHZ_0055','A05A03A01A01_BHZ_0056','A05A03A01A01_BHZ_0057') GROUP BY bhz_cement_detail.materiale_type")
    List<Map> selectycltjList(Integer pageNo, Integer pageSize);

    @Select("SELECT bhz_cement_detail.materiale_name,sum( bhz_cement_detail.reality_number ) AS realityNumber FROM bhz_cement_base JOIN bhz_cement_detail ON bhz_cement_base.batch_no = bhz_cement_detail.batch_no where shebei_no in (${shebeilist}) GROUP BY bhz_cement_detail.materiale_type")
    List<Map> selectycltjLists(String shebeilist,Integer pageNo, Integer pageSize);

    @Select("select bhz_cement_detail_statistics.materiale_name materiale_name," +
            "sum( bhz_cement_detail_statistics.reality_number ) AS realityNumber," +
            "bhz_cement_detail_statistics.materiale_type materiale_type" +
            " from bhz_cement_statistics join bhz_cement_detail_statistics on bhz_cement_statistics.id=bhz_cement_detail_statistics.cs_id" +
            " where bhz_cement_statistics.shebei_no IN (${shebeilist})" +
            " GROUP BY bhz_cement_detail_statistics.materiale_type ORDER BY bhz_cement_statistics.id desc")
    List<Map> ycltjliststatic(String shebeilist,Integer pageNo, Integer pageSize);

    @Update("update bhz_cement_base set renwudanstatus=#{renwudanstatus} where batch_no=#{batch_no}")
    int updatehntbhzrenwustatus(String batch_no, Integer renwudanstatus);

    Map<String,Object> bulletin(@Param("orgCode") String orgCode);
    List<Map> bhzcbv(@Param("result") Integer result,@Param("shebei") List<String> shebei);

    /**
     *
     * @param result
     * @param shebei
     * @return
     */
    List<Map> bhzcailiaoCount(@Param("result") Integer result,@Param("shebei") List<String> shebei);


    List<BhzCementBase> selecthntbhzonesstatistics(Integer id, Integer statistics);

    List<BhzCementBase> selectLists1(String shebeiNo, Integer id);

    List<BhzCementBase> selectLists2(String shebeiNo, Integer id);

    List<BhzCementBase> selectListskz4(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsyb(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsrcsms(String shebeiNo);

    List<BhzCementBase> selectLists1b(String shebeiNo, Integer id);

    List<BhzCementBase> selectListszt(String shebeiNo, Integer id);

    List<BhzCementBase> selectListssutai(String shebeiNo, Integer id);

    List<BhzCementBase> selectListyjqs(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhg(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgty(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgbhty(String shebeiNo, Integer id);

    List<BhzCementBase> selectListsbhgsg(String shebeiNo, Integer id);

    List<BhzCementBase> selecthntbhzlists(Integer curid, int i, String strsToList);

    List<BhzCementBase> selecthntbhzones(Integer id, Integer alertstate);

    List<BhzCementBase> selecthntbhzonesx(Integer id, Integer alertstate);

    BhzCementBase queryones(String shebeiNo);

    List<BhzCementBase> selectListdata(String shebeilist, Integer curid);

    List<BhzCementBase> selectListstbim(String shebeilist, Integer curid);

    List<BhzCementBase> selectListToSHYJ(String shebeilist, Integer curid);

    List<BhzCementBase> selectHntbhzList(Integer curid, int i, String strsToList1);

    List<BhzCementBase> selecthntbhzonesstatistics1(Integer curid, int statistics, Integer curdate);

    BhzCementWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO>  selectWranCountByorgcde(String orgCode);

    String selectbyorgcode(String sysOrgCode, int i);
    String selectbyorgcode2(String sysOrgCode, int i);

    List<BhzCementWarnVO> selectWranCountByshebeino(String sysOrgCode, int i);

    String selectbysysorgcode(String sysOrgCode);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    List<String> selectByCode(String orgCode);

    Integer selectBiheCount(String orgCode);

    @SqlParser(filter = true)
    List<BhzCementBaseRC> selectTongjiData(String shebeiList);

    List<BhzCementBase> selectcailiao(Integer id, Integer statistics);

    @Update("update bhz_cement_base set statistics1=#{statistics} where batch_no=#{batch_no}")
    void selectcailiaostatics(String batch_no, Integer statistics);

    @Select("select realname from sys_user where username = #{handlePerson}")
    String selectName(String handlePerson);


    BhzCementBaseCbTongji selectCbTongji(String shebeilist);

    List<BhzCementBaseCbTongji> selectshebeiBybiaoduan(String orgcode);

    List<String> selectshebeiByCode(String orgcode);

    List<Map> getDataToInitPM(@Param("code") String code,@Param("lastId") Integer lastId);

    List<BhzCementBase> selectHntbhzList2(Integer curid, int i, String shebeilist);

    List<BhzCementBase> selecthntbhzRC(Integer curid, int i);

    List<BhzCementBase> selecthntbhzbhRC(int cbStatistics, int bhStatus);

    String selectNameByCode(String orgCode);

    List<BhzCementBase> selectBatchNoByOrgCode(@Param("code") String orgCode,@Param("id") Integer id);

    @Select("select * from bhz_cement_base where id > #{id} and over_level != 0 and collect_datetime like #{format} and overproof_status != #{overproofStatus}")
    List<BhzCementBase> selectBhzChao(int id,int overproofStatus, String format);

    @Select("select * from bhz_cement_base where  over_level > 1 and product_datetime like #{format} and overproof_status = #{overproofStatus} and isorder=#{isorder} and alertstate=#{alertstate} limit 10")
    List<BhzCementBase> selectBhzChao2(int overproofStatus, String format, int isorder, int alertstate);

    // Integer findYuJingS(List<String> querySheBeiList);

   // Integer findBiHeS(List<String> querySheBeiList);

  //   Integer findXiangMuZS(List<String> querySheBeiList);

    Map<String,Long> findXiangMutimes(List<String> querySheBeiList, String dateNowStr);
    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findYuJingStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeStimes(List<String> querySheBeiList, String beginTime, String endTime);

    String selectSBByCode(String sys_org_code);

    Integer findXiangMuZSs(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    List<BhzCementBase> selectByCheFlag(String shebeilist, Integer cheflag, Integer alertstate, Integer curid);

    Integer findYuJingSxmtime(List<String> querySheBeiList, String dateNowStr, int i);

    Integer findBiHeSxmtime(List<String> querySheBeiList, String dateNowStr, int i);

    Integer findXiangMuZSzy(String sb, Integer grade);

    Integer findYuJingSzy(String sb, Integer grade);

    Integer findBiHeSzy(String sb, Integer grade);

    Integer findXiangMuZSyz(String sb, Integer grade);

    Integer findYuJingSyz(String sb, Integer grade);

    Integer findBiHeSyz(String sb, Integer grade);

   // @Select("select shebei_no from bhz_cement_base WHERE over_level > 0 and shebei_no in (${sbs}) and YEARWEEK(date_format(product_datetime,'%Y-%m-%d'),1) = YEARWEEK(now(),7) GROUP BY shebei_no ORDER BY count(*) desc limit 5")
    List<String> findbyshebeilistzy(List<String> sbs);
   // @Select("select shebei_no from bhz_cement_base WHERE over_level > 0 and shebei_no in (${sbs}) and DATE_FORMAT(product_datetime, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m') GROUP BY shebei_no ORDER BY count(*) desc limit 5")
    List<String> findbyshebeilistyz(List<String> sbs);

    BhzCementStatistics findzsyjsbhs(List<String> querySheBeiList, String beginTime, String endTime);

    List<BhzCementBase> selectRCJHJD(Integer curid, String shebeilist);

    @Select("select sbname from shebei_info where sbjno = #{sbjno}")
    String getSbname(String sbjno);

    List<BhzCementBase> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeilist, Integer overLevel);

    Map selectSYSdepartId(String sysOrgCode);

    List<BhzCementBase> selectHntbhzListBySTFour(Integer curid, int i, String shebeilist);

    List<BhzCementBase> selectSCData(Integer curid, String shebeilist);

//    List<Map<Object, Object>> selectCaiLiaoUse(String shebeiNo);
    List<BhzCementTongJi> selectCaiLiaoUse(String shebeiNo,String start,String end);

    Map selectwarnContent(String tableName, String sbjno);

    List<Map<String, Object>> getQddj(String sysDepartOrgcode);

    List<BhzCementBase> selecerenwudanpsgx(List<String> strings, String productDatetime_begin, String productDatetime_end);

    List<String> selecerenw(String shebeiNo, String productDatetime_begin, String productDatetime_end);

    List<String> selecerenws(List<String> shebeiNolist, String productDatetime_begin, String productDatetime_end);

    List<BhzCementBase> selectListjhjd(String shebeilist);

    String getLastPhb(List<String> shebeiNolist);
}
