package com.trtm.sy.sydpssysample.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syycjcdj.entity.SyDpsYyYuancaijinchangdengji;
import com.trtm.sy.sydpssysample.entity.*;
import com.trtm.sy.sylxdps.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.syrules.entity.SyCodingRules;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_sy_sample
 * @Author: jeecg-boot
 * @Date: 2023-01-12
 * @Version: V1.0
 */
public interface SyDpsSySampleMapper extends BaseMapper<SyDpsSySample> {

    @Select("select depart_name from sys_depart_project where org_code = #{orgCode}")
    String selectProjNames(String orgCode);


    Map<String, Object> selectReportSByUuid(String id);

    SyDpsSySample selectSampleByNo(String sampleNo);

    Map<String, Object> selectItemTypeByTitCode(String titcode);

    void updateSampleStatusByNo(String sampleNo);

    Map<String, Object> selectReportSByNo(String sampleNo);

    SyDpsJcTestitem selectTestItemByTino(String tiNo);

    List<String> findSySColumns(@Param("dataSource") String dataSource, @Param("tiNo") String tiNo);

    void updateTiNo(@Param("tiNo") String tiNo, @Param("key") String key, @Param("value") String value, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") String tiNoTemp);

    void updateTiNoBy(@Param("key") String key, @Param("value") String value, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateByTiNoTemp(@Param("key") String key, @Param("value") String value, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    List<Map<String, Object>> findIdBySampleNo(String sampleNo);

    void updateTiNoTempBySample(@Param("tiNo") String tiNo, @Param("s") String s, @Param("key") String key, @Param("value") String value, @Param("sampleNo") String sampleNo);

    void insertSql(@Param("tiNo") String tiNo, @Param("ziduanmings") String ziduanmings, @Param("values") List<String> values);

    List<Map<String, Object>> queryBySampleNo(String sampleNo);

    void dynamicRevise(@Param("tableName") String tableName,
                       @Param("key1") String key1,
                       @Param("value1") String value1,
                       @Param("key2") String key2,
                       @Param("value2") String value2,
                       @Param("key3") String key3,
                       @Param("value3") String value3);

    List<Map<String, Object>> querySql1(String sampleNo);

    Map<String, Object> querySql2(int cedianshu);

    void updateSql7(@Param("cedianshu") int cedianshu, @Param("fourSSixRFiveCS") String fourSSixRFiveCS, @Param("fourSSixRFiveCS1") String fourSSixRFiveCS1, @Param("fourSSixRFiveCS2") String fourSSixRFiveCS2, @Param("fourSSixRFiveCS3") String fourSSixRFiveCS3, @Param("sampleNo") String sampleNo);

    void updateSql8(@Param("yiqishebei") String yiqishebei, @Param("sampleNo") String sampleNo, @Param("s") String s);

    void updateSql15(@Param("baogaoriqi") String baogaoriqi, @Param("syjg") Integer syjg, @Param("buhegeyuanyin") String buhegeyuanyin, @Param("sampleNo") String sampleNo);

    void updateSql16(@Param("baogaoriqi") String baogaoriqi, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateSql17(@Param("shiyanriqi") String shiyanriqi, @Param("sampleNo") String sampleNo);

    void updateSql18(String id);

    void updateByTiNoTempTwo(@Param("key") String key, @Param("value") String value, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") String tiNoTemp, @Param("tiNo") String tiNo);

    Integer findByOrgCodeAndUserName(@Param("orgCode") String orgCode, @Param("tiNo") String tiNo, @Param("username") String username, @Param("titcode") String titcode);

    void updateYiQiSheBei(@Param("yiqishebei") Object yiqishebei, @Param("orgCode") String orgCode, @Param("tiNo") String tiNo, @Param("username") String username, @Param("titcode") String titcode);

//    void insertYiQiSheBei(String orgCode, String tiNo, String username, String yiqishebei, String titcode);

    List<Map<String, Object>> querySql3(@Param("sampleNo") String sampleNo, @Param("sampleNo1") String sampleNo1);

    void updateSql19(@Param("y") String y, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    List<Map<String, Object>> querySql4(@Param("sampleNo") String sampleNo, @Param("sampleNo1") String sampleNo1);

    void updateSql20(@Param("y") String y, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateSql21(@Param("baogaoriqi") String baogaoriqi, @Param("sampleNo") String sampleNo);

    List<Map<String, Object>> querySql5(@Param("sampleNo") String sampleNo, @Param("sampleNo1") String sampleNo1);

    void updateSql22(@Param("y") String y, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateSql23(String wzid);

    void insertYiQiSheBei(@Param("uuid") String uuid, @Param("orgCode") String orgCode, @Param("tiNo") String tiNo, @Param("username") String username, @Param("yiqishebei") String yiqishebei, @Param("titcode") String titcode);

    Map<String, Object> getOneSql1(String titCode);

    SyDpsJcTestitemtype getOneSql2(String id);

    List<Map<String, Object>> selectBySam(String sampleno);

    List<Map<String, Object>> selectBgBySam(String sampleno);

    List<Map<String, Object>> getDevice(@Param("orgCode") String orgCode, @Param("titcode") String titcode, @Param("format") String format);

    Map<String, Object> selectBySam2(String sampleno);

    Map<String, Object> selectBgBySam2(String sampleno);

    SysDepart getDepartById(String departid);

    List<SysDepart> getDepartByIds(String departIds);

    Map<String, Object> getOneSql3(@Param("id") String id, @Param("titCode") String titCode);

    SyDpsJcTestitemtypeCodingrules getOneSql4(String id);

    SyDpsSyReportM getOneSql5(String sampleno);

    List<Map<String, Object>> getOneSql6(String sampleno);

    void deleteSql1(@Param("s") String s, @Param("sampleno") String sampleno);

    void deleteSql2(@Param("s") String s, @Param("sampleno") String sampleno);

    Map<String, Object> getOneSql7(String s);

    void deleteSql3(@Param("tiTableNum") String tiTableNum, @Param("s") String s, @Param("sampleno") String sampleno);

    Map<String, Object> getOneSql8(@Param("sampleno") String sampleno, @Param("ptiNo") String ptiNo);

    Long getCount1(@Param("s") String s, @Param("sampleno") String sampleno);

    Map<String, Object> getOneSql9(String s);

    Map<String, Object> getOneSql10(@Param("id") String id, @Param("s") String s, @Param("username") String username, @Param("titcode") String titcode);

    Long getCount2(@Param("tiTableNum") String tiTableNum, @Param("sampleno") String sampleno);

    Map<String, Object> getOneSql11(@Param("tiTableNum") String tiTableNum, @Param("id") String id);

    void insertSql1(@Param("tiTableNum") String tiTableNum, @Param("uuid") String uuid, @Param("tino") String tino, @Param("i") int i, @Param("sampleno") String sampleno, @Param("tablenumber") String tablenumber, @Param("reportno") String reportno, @Param("departid") String departid);

    void updateSql24(@Param("tiTableNum") String tiTableNum, @Param("tino") String tino, @Param("i") int i, @Param("sampleno") String sampleno, @Param("tablenumber") String tablenumber, @Param("reportno") String reportno, @Param("departid") String departid, @Param("cbryj") String cbryj, @Param("sampleno1") String sampleno1);

    void insertSql2(@Param("tiTableNum") String tiTableNum, @Param("uuid") String uuid, @Param("tino") String tino, @Param("i") int i, @Param("sampleno") String sampleno, @Param("tablenumber") String tablenumber, @Param("reportno") String reportno, @Param("departid") String departid);

    void updateSql25(@Param("tiTableNum") String tiTableNum, @Param("tino") String tino, @Param("i") int i, @Param("sampleno") String sampleno, @Param("tablenumber") String tablenumber, @Param("reportno") String reportno, @Param("departid") String departid, @Param("sampleno1") String sampleno1);

    void updateSql26(@Param("joinString") String joinString, @Param("sampleno") String sampleno, @Param("ptiNo") String ptiNo);

    void updateSql27(@Param("projectname") String projectname, @Param("sampleno") String sampleno);

    void updateTableHeader(@Param("projectname") String projectname, @Param("samplename") String samplename, @Param("sampledescribe") String sampledescribe, @Param("samplequyangdidian") String samplequyangdidian,
                           @Param("sampledaibiaoshuliang") String sampledaibiaoshuliang, @Param("sampleshengchanchangjia") String sampleshengchanchangjia, @Param("sampleshengchanpihao") String sampleshengchanpihao,
                           @Param("sampleyanghutiaojian") String sampleyanghutiaojian, @Param("sampledate") String sampledate, @Param("samplechandi") String samplechandi, @Param("samplepinzhongdengji") String samplepinzhongdengji,
                           @Param("sampleshejijiliang") String sampleshejijiliang, @Param("samplejieheliaozhonglei") String samplejieheliaozhonglei, @Param("sampleliqingbiaohao") String sampleliqingbiaohao,
                           @Param("sampleliqingzhonglei") String sampleliqingzhonglei, @Param("samplejipeileixing") String samplejipeileixing, @Param("sampleshiyancengwei") String sampleshiyancengwei,
                           @Param("sampleliqinghunheliaoleixing") String sampleliqinghunheliaoleixing, @Param("samplegangjinzhijing") String samplegangjinzhijing, @Param("samplegangjinzhonglei") String samplegangjinzhonglei,
                           @Param("samplelingqi") String samplelingqi, @Param("samplewaijiajichanliang") String samplewaijiajichanliang, @Param("sampleshengchanriqi") String sampleshengchanriqi, @Param("sampleguigexinghao") String sampleguigexinghao,
                           @Param("samplequyangren") String samplequyangren, @Param("samplechuchangbianhao") String samplechuchangbianhao, @Param("sampleqiangdudengji") String sampleqiangdudengji, @Param("samplejiaobanfangshi") String samplejiaobanfangshi,
                           @Param("samplehunningtuzhonglei") String samplehunningtuzhonglei, @Param("sampleshajiangzhonglei") String sampleshajiangzhonglei, @Param("sampleshiyanzushu") String sampleshiyanzushu, @Param("samplechandi2") String samplechandi2,
                           @Param("samplechandi3") String samplechandi3, @Param("samplequyangdidian2") String samplequyangdidian2, @Param("samplequyangdidian3") String samplequyangdidian3, @Param("sampledate2") String sampledate2, @Param("sampledate3") String sampledate3,
                           @Param("sampledaibiaoshuliang2") String sampledaibiaoshuliang2, @Param("sampledaibiaoshuliang3") String sampledaibiaoshuliang3, @Param("sampledaibiaoshuliang4") String sampledaibiaoshuliang4, @Param("sampledaibiaoshuliang5") String sampledaibiaoshuliang5,
                           @Param("sampleguigexinghao2") String sampleguigexinghao2, @Param("sampleguigexinghao3") String sampleguigexinghao3, @Param("sampleguigexinghao4") String sampleguigexinghao4, @Param("sampleguigexinghao5") String sampleguigexinghao5,
                           @Param("samplegongchengbihou") String samplegongchengbihou, @Param("ycjcwtdbh") String ycjcwtdbh, @Param("sampleyangpinshuliang") String sampleyangpinshuliang, @Param("sampleyangpinshuliang2") String sampleyangpinshuliang2,
                           @Param("sampleyangpinshuliang3") String sampleyangpinshuliang3, @Param("sampleyangpinshuliang4") String sampleyangpinshuliang4, @Param("sampleyangpinshuliang5") String sampleyangpinshuliang5, @Param("shiYanYiJu") String shiYanYiJu,
                           @Param("panDingYiJu") String panDingYiJu, @Param("samplechangdu") String samplechangdu, @Param("samplezhijianriqi") String samplezhijianriqi, @Param("sampleno") String sampleno);

    void updateTableHeaderYPXX1(@Param("bgypxx") String bgypxx, @Param("sampleno") String sampleno);

    void updateTableHeaderYPXX2(@Param("jlypxx") String jlypxx, @Param("sampleno") String sampleno);

    void updateTableHeaderByTiNo(@Param("ypxx") String ypxx, @Param("sampleno") String sampleno, @Param("tiNo") String tiNo);

    Map<String, Object> getOneSql12(String insert);

    Long getCount3(@Param("insert") String insert, @Param("s") String s);

    Map<String, Object> getOneSql13(String s);

    void updateSql28(@Param("currentCode") String currentCode, @Param("id") String id);

    void insertSql3(@Param("uuid") String uuid, @Param("currentCode") String currentCode, @Param("insert") String insert);

    Map<String, Object> getSql1(String yp);

    Long getCount4(String s);

    List<Map<String, Object>> getSql2(String s);

    void updateSql29(@Param("currentCode") String currentCode, @Param("id") String id);

    Map<String, Object> getSql3(String ptiNo);

    Map<String, Object> getSql4(String tiNo);

    SyDpsJcTestitem findByTiNo(String tiNo);

    Map<String, Object> getSheBeiName(@Param("id") String id, @Param("tiNo") String tiNo, @Param("username") String username, @Param("titcode") String titcode);

    Map<String, Object> getSql5(@Param("tiTableNum") String tiTableNum, @Param("id") String id);

    void updateSql30(@Param("sampleno") String sampleno, @Param("jianceyiju") String jianceyiju, @Param("pandingyiju") String pandingyiju, @Param("shiyanwanchengqixian") String shiyanwanchengqixian, @Param("yuancaijinchangdengjiId") Integer yuancaijinchangdengjiId);

    void updateSql31(@Param("sampleno") String sampleno, @Param("yuancaijinchangdengjiId") Integer yuancaijinchangdengjiId);

    @Select("select * from sys_depart where org_code=#{orgCode}")
    SysDepart getByOrgCode(@Param("orgCode") String orgCode);

    @Select("select titType from sy_dps_jc_testitemtype where titCode=#{titCode}")
    Integer selectTitTypeByTitCode(String titCode);

//    void insertSql3(String tiTableNum, String uuid, String s, int i, String sampleno, String tablenumber, String reportno, String departid);

//    void updateSql26(String tiTableNum, String s, int i, String sampleno, String tablenumber, String reportno, String departid, String sampleno1);

//    Integer getCount(String view);

    IPage<Map> selectInfo1(@Param("view") String view, @Param("orgCode") String orgCode, @Param("signature") String signature, @Param("titCode") String titCode, @Param("sampleState") String sampleState, @Param("sampleNo") String sampleNo, @Param("reportNo") String reportNo,
                           @Param("sampleName") String sampleName, @Param("sampleGcbw") String sampleGcbw, @Param("titType") String titType, @Param("tiNo") String tiNo, @Param("lq") String lq, @Param("sampleDate") String sampleDate, @Param("lookself") Boolean lookself,
                           @Param("reportEditPerson") String reportEditPerson, @Param("shenpizhuangtai") String shenpizhuangtai, @Param("qianzhangzhuangtai") String qianzhangzhuangtai, @Param("reportEditPerson1") String reportEditPerson1, Page<Map> page);

    @Delete("delete from sy_dps_jc_shebei_shiyongjilu where sampleno=#{sampleNo}")
    void deleteBySampleNo(String sampleNo);

    List<Map<String, Object>> selectTinoBySampleNo(String sampleNo);

    List<Map<String, Object>> selectDate(@Param("sampleNo") String sampleNo, @Param("tino") String tino);

    @Select("select * from sys_depart where id = #{id}")
    SysDepart queryDepartByDepartid(String id);

    List<Map<String, Object>> selectByLeixing(@Param("qianzhangleixing") int qianzhangleixing, @Param("titcode") String titcode, @Param("liuchengleixing") int liuchengleixing);

    List<Map<String, Object>> selectByIdAndType(@Param("id") String id, @Param("type") Integer type);

    @Select("SELECT tiNo FROM sy_dps_sy_Report_S WHERE sampleNo=#{sampleno} and titype='1'")
    String selectOneTinoBySampleno(String sampleno);

    @Select("SELECT baogaoriqi FROM sy_dps_sy_tableHeader WHERE sampleNo=#{sampleno} and tiNo=#{tino}")
    String selectBgdateBySampleno(@Param("sampleno") String sampleno, @Param("tino") String tino);

    Map<String, Object> selectByRSId(String id);

    Map<String, Object> selectBysampleNoAndtiNoTemp(@Param("tiNo") String tiNo, @Param("sampleno") String sampleno, @Param("tinotemp") String tinotemp);

    @Select("select id from sy_dps_jc_testItemType_CodingRules where departId=#{departId} and titCode=#{titCode}")
    Map<String, Object> selectId(@Param("departId") String departId, @Param("titCode") String titCode);

    @Select("select * from sy_dps_jc_testItemType_CodingRules where id = #{id}")
    SyDpsJcTestitemtypeCodingrules selectTCById(String id);

    @Select("select * from sys_depart where id = #{departId}")
    SysDepart selectByDepartId(String departId);

    @Select("select * from sy_dps_jc_testitemtype where titCode = #{titCode}")
    SyDpsJcTestitemtype selectTtByTitcode(String titCode);

    Map<String, Object> selectTinoBySampleno(String sampleNo);

    @Select("select id,currentCode from sy_dps_sy_codingFlowNumber where NoFlowNumber=#{NoFlowNumber}")
    Map<String, Object> selectByNoFlowNumber(String NoFlowNumber);

    @Select("select count(0) from sy_dps_sy_Sample where insertNo is null  and sampleNo like concat(#{str},'%')")
    Long selectCount(String str);

    @Select("select sampleNo from sy_dps_sy_Sample where insertNo is null  and sampleNo like concat(#{str},'%')")
    List<Map<String, Object>> selectSampleNoLike(String str);

    @Update("update sy_dps_sy_codingFlowNumber set currentCode = #{currentCode} where id=#{id}")
    void updateCurrentCodeById(@Param("currentCode") String currentCode, @Param("id") String id);

    @Insert("insert into sy_dps_sy_codingFlowNumber values (newId(),#{currentCode},#{yp})")
    void addCodingFlowNumber(@Param("currentCode") String currentCode, @Param("str") String str);

    @Select("select * from sy_dps_sy_report_m where sampleno = #{sampleNo}")
    SyDpsSyReportM selectRMBySampleNo(String sampleNo);

    @Select("select * from sy_dps_sy_report_s where sampleno = #{sampleNo}")
    List<SyDpsSyReportS> selectRSBySampleNo(String sampleNo);

    @Select("select * from sy_dps_sy_tableheader where sampleno = #{sampleNo}")
    List<SyDpsSyTableheader> selectTHBySampleNo(String sampleNo);

    @Select("select * from sy_dps_jc_testitem where tino = #{tiNo}")
    SyDpsJcTestitem selectTIByTino(String tiNo);

    @Select("select * from  ${tableName}  where sampleNo=#{sampleNo} and tiNoTemp=#{tiNoTemp}")
    Map<String, Object> selectBySamplenoFromTino(@Param("tableName") String tableName, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") Integer tiNoTemp);

    void insert1(@Param("tino") String tino, @Param("str") String str,
                 @Param("tiNoTemp") Integer tiNoTemp, @Param("yp") String yp,
                 @Param("jl") String jl, @Param("bg") String bg, @Param("departId") String departId);

    void update1(@Param("tino") String tino, @Param("str") String str, @Param("key") String key, @Param("value") String value);

    @Select("select count(0) from sy_dps_sy_sample where sampleNo=#{sampleNo}")
    Long selectCountBySampleNo(String sampleNo);

    void updateCFN(@Param("sampleNo2") String sampleNo2, @Param("sampleNo1") String sampleNo1, @Param("sampleNoNoSuffix") String sampleNoNoSuffix);

    @Update("update sy_dps_sy_Report_M set sampleNo = #{sampleNo},reportNo = #{reportNo},tableNumber = #{tableNumber} where sampleNo = #{sampleNoOld}")
    void updateRM(@Param("sampleNo") String sampleNo, @Param("c") String c, @Param("tableNumber") String tableNumber, @Param("sampleNoOld") String sampleNoOld);

    @Update("update sy_dps_sy_Report_S set sampleNo = #{sampleNo},reportNo = #{reportNo} where sampleNo = #{sampleNoOld}")
    void updateRS(@Param("sampleNo") String sampleNo, @Param("reportNo") String reportNo, @Param("sampleNoOld") String sampleNoOld);

    @Select("select tino from sy_dps_sy_tableHeader where sampleNo = #{sampleNo}")
    List<String> selectTiNoList(String sampleNo);

    @Update("update ${table} set sampleNo = #{sampleNo},reportNo = #{reportNo},tableNumber = #{tableNumber} where sampleNo = #{sampleNoOld}")
    void updateTable(@Param("table") String table, @Param("sampleNo") String sampleNo, @Param("reportNo") String reportNo, @Param("tableNumber") String tableNumber, @Param("sampleNoOld") String sampleNoOld);

    @Update("update sy_dps_sy_tableHeader set sampleNo=#{sampleNox},reportNo=#{reportNo},tableNumber=#{tableNumber},reportingSheetNo=#{reportingSheetNo}," +
            "approvalTableNo=#{approvalTableNo},sampleNoNew=#{sampleNoNew},reportNoNew=#{reportNoNew},tableNumberNew=#{tableNumberNew}," +
            "reportingSheetNoNew=#{reportingSheetNoNew},approvalTableNoNew=#{approvalTableNoNew} where sampleNo=#{sampleNo}")
    void updateNo1(@Param("sampleNox") String sampleNox, @Param("reportNo") String reportNo, @Param("tableNumber") String tableNumber,
                   @Param("reportingSheetNo") String reportingSheetNo, @Param("approvalTableNo") String approvalTableNo,
                   @Param("sampleNoNew") String sampleNoNew, @Param("reportNoNew") String reportNoNew, @Param("tableNumberNew") String tableNumberNew,
                   @Param("reportingSheetNoNew") String reportingSheetNoNew, @Param("approvalTableNoNew") String approvalTableNoNew, @Param("sampleNo") String sampleNo);

    @Update("update ${table} set sampleNo=#{sampleNo1} where sampleNo=#{sampleNo2}")
    void updateNo2(@Param("table") String table, @Param("sampleNo1") String sampleNo1, @Param("sampleNo2") String sampleNo2);

    @Delete("delete from ${table} where sampleId=#{sampleId}")
    void deleteNo3(@Param("table") String table, @Param("sampleId") String sampleId);

    @Select("select * from sy_dps_sy_sample where id = #{id}")
    SyDpsSySample selectSam(String id);

    @Delete("delete from sy_dps_sy_Report_M where sampleNo=#{sampleNo}")
    void delRMBySampleNo(String sampleNo);

    @Delete("delete from sy_dps_sy_Report_S where sampleNo=#{sampleNo}")
    void delRSBySampleNo(String sampleNo);

    @Delete("delete from sy_dps_sy_tableHeader where sampleNo=#{sampleNo}")
    void delTHBySampleNo(String sampleNo);

    @Select("select tiNo from sy_dps_sy_tableHeader where sampleNo=#{sampleNo}")
    List<Map<String, Object>> selectTH(String sampleNo);

    @Delete("delete from ${tiNo} where sampleNo=#{sampleNo}")
    void delTableBySampleNo(@Param("tiNo") String tiNo, @Param("sampleNo") String sampleNo);

    @Insert("INSERT INTO sy_dps_deleteyp (username,date,sampleNo) VALUES (#{username},#{date},#{sampleNo})")
    void insertDT(@Param("username") String username, @Param("date") String date, @Param("sampleNo") String sampleNo);

    @Select("select qrcode from  sy_dps_sy_SampleQRCode  where sampleNo= #{sampleNo}")
    List<Map<String, Object>> selectGrcode(String sampleNo);

    @Select("select * from  sy_dps_yy_yuancaijinchangdengji  where sampleNo= #{sampleNo}")
    SyDpsYyYuancaijinchangdengji selectYCJCDJ(String sampleNo);

    @Delete("delete from sy_t_consign_code where CODE=#{code}")
    void del1(String code);

    @Delete("delete from sy_dps_sy_SampleQRCode where sampleNo=#{sampleNo}")
    void del2(String sampleNo);

    @Delete("delete from sy_dps_sy_SamplePic where sampleNo=#{sampleNo}")
    void del3(String sampleNo);

    @Delete("delete from sy_dps_sy_Sample where sampleNo=#{sampleNo}")
    void del4(String sampleNo);

    @Delete("delete from sy_dps_yy_yuancaiquyangweituo where yuancaijinchangdengjiId=#{id}")
    void del5(Integer id);

    @Delete("delete from sy_dps_yy_quyangjiludan where yuancaijinchangdengjiId=#{id}")
    void del6(Integer id);

    @Delete("delete from sy_dps_yy_renwudan where yuancaijinchangdengjiId=#{id}")
    void del7(Integer id);

    Map<String, Object> getSampleId(String id);

    @Select("select tiNo,max(editState) editState from sy_dps_sy_Report_S where sampleNo=#{sampleNo} group by tiNo,sampleNo,tiType order by case when tiType=1 then 9 else tiType end desc")
    List<Map<String, Object>> getRSTino(String sampleNo);

    @Select("select a.qrcode,a.beginImage,CSTATUS,huojianame,huojiacenshu,NO from sy_dps_sy_SampleQRCode a LEFT join sy_T_consign_code b on a.qrcode=b.CODE where a.sampleNo=#{sampleNo} ")
    List<Map<String, Object>> getQRCode(String sampleNo);

    @Select("select pic1,pic2,pic3,pic4 from sy_dps_sy_SamplePic where sampleNo=#{sampleNo} ")
    List<Map<String, Object>> getPici(String sampleNo);

    @Select("select shiyanyiju,pandingyiju from sy_dps_sy_tableHeader where sampleNo=#{sampleNo} limit 1")
    List<Map<String, Object>> getYJ(String sampleNo);

    @Delete("delete from sy_dps_sy_Report_M where sampleNo= #{sampleNo}")
    void deleteReportM(String sampleNo);

    @Delete("delete from sy_dps_sy_Report_S where sampleNo= #{sampleNo}")
    void deleteReportS(String sampleNo);

    @Delete("delete from ${tino} where sampleNo=#{sampleNo}")
    void deleteByTiNo(@Param("tino") String tino, @Param("sampleNo") String sampleNo);

    @Insert("insert into sy_dps_deleteyp (username,date,sampleNo) values (#{id},#{date},#{sampleNo})")
    void insertDataToDeleteYp(@Param("id") String id, @Param("date") String date, @Param("sampleNo") String sampleNo);

    @Delete("delete from sy_dps_sy_waibuweituo where sampleNo= #{sampleNo}")
    void deleteFromWaiBuWT(String sampleNo);

    IPage<Map> getSignatureGrid(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String beginDate, String endDate, String reportNo, Integer liuchengleixing, String type, String state, String userId, String beginReportDate, String endReportDate, String s);

    IPage<Map>  getSignatureGrid2(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String beginDate, String endDate, String reportNo, Integer liuchengleixing, String type, String state, String userId, String beginReportDate, String endReportDate, String s);


    List<Map<String, Object>> get2(@Param("sql")String sql, @Param("biaoming")String biaoming, @Param("orderby")String orderby);

    List<Map<String, Object>> get1(@Param("sql")String sql, @Param("biaoming")String biaoming, @Param("orderby")String orderby, @Param("groupby")String groupby);

    List<Map<String, Object>> getTable(@Param("table")String table, @Param("sql")String sql);


    Map<String, Object> selectByBiaoMing1(String biaoming, String sql);

    Map<String, Object> selectByBiaoMing2(String biaoming, String sql);

    List<Map<String, Object>> selectCBRDUSHU(String id);

    IPage getGrid3(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String beginDate, String endDate, String reportNo, String tiNo, String username, String state, String self, String shenpizhuangtai);

    void updateByTiNo(@Param("tiNo") String tiNo, @Param("sql") String sql, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") String tiNoTemp);

    void updateTiNoBySql(@Param("sql") String sql, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateTiNoSql(@Param("tiNo") String tiNo, @Param("sql") String sql, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") String tiNoTemp);

    void updateByTiNoTempSql(@Param("sql") String sql, @Param("sampleNo") String sampleNo, @Param("tiNo") String tiNo);

    void updateByTiNoTempTwoSql(@Param("sql") String sql, @Param("sampleNo") String sampleNo, @Param("tiNoTemp") String tiNoTemp, @Param("tiNo") String tiNo);

    List<Map> getByJlb(@Param("jlb") String jlb, @Param("wtid") String wtid);
    List<Map> getByJlb2(@Param("jlb") String jlb, @Param("wtid") String wtid, @Param("tiNoTemp") String tiNoTemp);

    List<Map<String, Object>> selectSyjzb(@Param("wtid") String wtid, @Param("lq") String lq, @Param("sylx") String sylx);

    List<Map<String, Object>> selectYaLiJi(String syjid);

    List<Map<String, Object>> selectWnj(String syjid);

    List<Map<String, Object>> getPzBmBySyJzb(String wtid);

    List<TSyjzb> getSyJzbByGjPh(@Param("wtid") String wtid, @Param("gangjpaih") String gangjpaih);

    List<Map<String, Object>> getSyJzb(String wtid);

    List<Map> selectBySampleList(@Param("orgCode") String orgCode, @Param("signature") String signature, @Param("titCode") String titCode,
                             @Param("sampleState") String sampleState, @Param("sampleNo") String sampleNo, @Param("reportNo") String reportNo,
                             @Param("sampleName") String sampleName, @Param("sampleGcbw") String sampleGcbw, @Param("titType") String titType,
                             @Param("tiNo") String tiNo, @Param("lq") String lq, @Param("sampleDate") String sampleDate,
                             @Param("lookself") Boolean lookself, @Param("userName") String userName, @Param("shenpizhuangtai") String shenpizhuangtai,
                             @Param("qianzhangzhuangtai") String qianzhangzhuangtai, @Param("userName1") String userName1, @Param("count") int count,
                             @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, Page<Map> page);

    IPage<SyResponse> selectInfo(SyRequest syRequest, Page<SyResponse> page);

    Map<String, Object> selectByBiaomingANDsampleno( @Param("tablename")String tablename,  @Param("sampleNo")String sampleNo);

    SyCodingRules selectDepartRuleByUserOrgCode(String departOrgCode);

    Map<String, Object> getSampleById(String id);

    String selectBgTiNo(String sampleNo);


    List<Map> getSyRenWu(@Param("orgCode") String orgCode);

    @Select("select titType from sy_dps_jc_testitemtype where titCode=#{titCode}")
    String selectTypeByTitCode(@Param("titCode") String titCode);

    List<Map> getTypeListByCode(String code);

    @Delete("delete from sy_dps_sy_Sample_GBHT where sampleNo= #{sampleNo}")
    void delGBHT(String sampleNo);

    @Delete("delete from sy_dps_yy_Measurement where sampleNo= #{sampleNo}")
    void delMeasurement(String sampleNo);

    IPage<Map> searchPhbData(@Param("tiNo") String tiNo, @Param("orgCode") String orgCode, @Param("sampleNo") String sampleNo, Page page);

    Map<String, Object> selectbywtdh(String wtdbh);

    Map<String, Object> selectTitCodeBYSampleNo(String sampleNo);

    Map<String, Object> selectBaogaoriqiBySampleNO(String sampleNo);

    @Select("SELECT shiyanriqi from sy_dps_sy_tableHeader where sampleNo=#{sampleNo} and tiNo=#{tino}")
    Map<String, Object> selectShiyanriqiBySamplenoAndtino(String sampleNo, String tino);

    @Select("select top 1 baogaoriqi_3d from JB010401 where sampleNo=#{sampleNo} ")
    Map<String, Object> selectFromJB010401(String sampleNo);

    @Select("SELECT baogaoriqi from sy_dps_sy_tableHeader where sampleNo=#{sampleNo} and tiNo=#{tino}")
    Map<String, Object> selectBaogaoriqiBySamplenoAndtino(String sampleNo, String tino);

    String selectRolename(String id);

    @Update("UPDATE `sy_dps_sy_sample` SET `pdfurl` = #{pDfurl} WHERE `id` = #{id}")
    void updatePdfurl(String id, String pDfurl);
}
