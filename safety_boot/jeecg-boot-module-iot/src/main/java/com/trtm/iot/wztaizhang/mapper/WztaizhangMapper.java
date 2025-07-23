package com.trtm.iot.wztaizhang.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.wztaizhang.entity.JypWztz;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.wztaizhang.vo.WztaizhangKBVO;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date: 2021-06-18
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "1")
public interface WztaizhangMapper extends BaseMapper<Wztaizhang> {
    @Update("update wztaizhang set maozhongT=#{maozhongT2},pizhongT=#{pizhongT2} ,jingzhongT=#{jingzhongT2} where id=#{ids}")
    int updatealertsate(String maozhongT2, String pizhongT2, Integer ids, String jingzhongT2);

    @Select("SELECT\n" +
            "\tt2.nodeType,\n" +
            "\tt2.cailiaoName,\n" +
            "\tCOUNT( t1.pici ) as pici,\n" +
            "\tSUM( t1.jingzhongT) as jingzhong\n" +
            "FROM\n" +
            "\twztaizhang t1\n" +
            "\tJOIN wzcailiaonamedict t2 ON t1.cailiaoNo = t2.cailiaoNo \n" +
            "  JOIN sys_depart t3 ON t2.sys_org_code = t3.org_code\n" +
            "\tJOIN sys_dict_item t4 on t2.nodeType=t4.item_value\n" +
            "\twhere t4.dict_id = '1390572018679844865'\n" +
            "\tand t3.org_code LIKE concat(#{orgCode},'%')\n" +
            "GROUP BY\n" +
            "\tt2.nodeType")
    List<Map> zhongdianCailiao(String orgCode);


//    @Select("SELECT DISTINCTROW\n" +
//            "\tCONCAT_WS(\n" +
//            "\t\t'|',\n" +
//            "\t\tlc.pici,\n" +
//            "\t\tCASE WHEN tz.jianyanstate = 0 THEN\n" +
//            "\t\t\t\t'未检验' \n" +
//            "\t\t\t\tWHEN tz.jianyanstate = 1 THEN\n" +
//            "\t\t\t\t'合格' \n" +
//            "\t\t\t\tWHEN tz.jianyanstate = 3 THEN\n" +
//            "\t\t\t\t'检验中' ELSE \"\" \n" +
//            "\t\t\tEND \n" +
//            "\t\t\t \n" +
//            "\t\t) AS `text`,\n" +
//            "\t\tlc.pici `value`,\n" +
//            "\t\ttz.jianyanstate \n" +
//            "\tFROM\n" +
//            "\t( SELECT * FROM `wztaizhang_lc` WHERE liaocangno = #{lcguid} ORDER BY create_time DESC LIMIT 5 ) lc\n" +
//            "\tLEFT JOIN wztaizhang tz ON lc.pici = tz.pici")
    List<Map> selectPiciBylc(String lcguid);
    List<Map> selectPiciBylc2(String lcguid);


    List<Wztaizhang> selectLists(String strsToList1, Integer curid);

    Wztaizhang queryones(String sysOrgCode, String cailiaoNo, String gongyinsgangNo);

    Wztaizhang queryoness(String sysOrgCode, String cailiaoNo, String gongyinsgangNo, String pici);

    List<Wztaizhang> selectolddata(Integer ids);

    @Select("select org_code from sys_depart where org_category=#{orgCategory}")
    List<String> getOrgCodeList(String orgCategory);

    String selectPici(String orgcode, String nodetype);

    String selectBhgPici(String orgcode, String nodetype);

    @Select("select depart_name from sys_depart where org_code=#{orgcode}")
    String getDepartName(String orgCode);

    @Select("select org_code from sys_depart where org_category=#{orgCategory}  and  org_code like concat(#{orgCode},'%')")
    List<String> selectOrgcode(String orgCategory, String orgCode);

    List<Wztaizhang> selectBhgPiciByBd(String nodetype, String orgcode);

    @Select("select depart_name from sys_depart where org_category=3  and  #{orgcode} like concat(org_code,'%')")
    String selectGsName(String orgcode);

    @Select("select cailiaoName from wzcailiaonamedict where cailiaono=#{cailiaono}")
    String selectCailiaoName(String cailiaono);

    List<JypWztz> queryJypList();

    @Select("select nodetype from wzcailiaonamedict where cailiaono=#{cailiaono}")
    String selectNodetypeByCailiaono(String cailiaono);


    List<Map> selectmapList(String jinchangshijian_begin, String jinchangshijian_end, Integer cprule, Integer ycrule);

    List<Map> selectByYearList(Integer ycrule, Integer cprule);

    List<Map> selectByProject(String code);

    WztaizhangKBVO selectNumByCode(String code, String beginTime, String endTime, String cailiaoNo);

    List<Map> selectBiaoduanBycode(String code);

    Double selectCountByPro(String sys_depart_orgcode, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);


    List<Map> selectMonthByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);

    List<Map> selectMLvByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList);

    Double selectCountByCl(List<String> cailiaoNo, String code, String beginTime, String endTime);

    void updateDelegateState(Integer id, Integer num);

    List<JypWztz> queryJypList2(String curdate);

    IPage<Wztaizhang> selectByV(QueryVo queryVo, Page<Wztaizhang> page);

    IPage<Map> getWzTz(Page<Map> page, QueryVo queryVo);

    Map getNoName(String storageId, String inspectionLotNumber);

    @Select("select DISTINCT nodetype from wztaizhang where sys_org_code like concat(#{orgCode},'%')")
    List<String> getNodetypeByOrgCode(String orgCode);

    @Select("select sys_org_code from jt_tunnel_code where treeid = #{wbsId}")
    String getOrgCode(String wbsId);

    @Select("select id from sys_dict where dict_code = 'nodeType'")
    String getDictId();

    @Select("select item_text from sys_dict_item where dict_id = #{id} and item_value = #{nodetype}")
    String getNodetypeName(String id, String nodetype);

    @Select("select * from ycl_usage_detail where inspection_lot_number = #{pici}")
    List<YclUsageDetail> getUses(String pici);

    @Select("select sys_org_code from jt_tunnel_code where treeid = #{wbsId}")
    String getWbsOrgCode(String wbsId);

    @Select("select * from ycl_usage_detail where code like concat(#{sysOrgCode},'%')")
    List<YclUsageDetail> getUsageList(String sysOrgCode);

    @Select("select * from wztaizhang where pici = #{inspectionLotNumber}")
    Wztaizhang getState(String inspectionLotNumber);

    List<String> getNode();

    List<YclUsageDetail> getUseageList(String nodeType, String wbsid);

    String selectBydict(String nodetype);

    List<String> selectByDictValue(String cailiaoName);

    List<Map> selectDictList(Integer cprule, Integer ycrule);

    List<String> selectByGongyingshang(String gongyingshangdanweibianma);

    List<String> getPiCiByCLNo(List<String> noList);

    List<String> getByGuiGeXH(String guiGeXingHao);

    Wztaizhang getByPici(String pici);

    @Select("SELECT report from ycl_test_detail where sample_number = #{pici}")
    String getBgfile(String pici);

    List<Wztaizhang> SXListYCSD(String ycStr, String sysOrgCode);

    List<Wztaizhang> SXBhgListYCSD(String ycStr, String sysOrgCode);

    List<Wztaizhang> SXListCPSD(String cpStr, String sysOrgCode);

    List<Wztaizhang> SXBhgListCPSD(String cpStr, String sysOrgCode);

    List<Wztaizhang> SXListYCLC(String ycStr, String sysOrgCode);

    List<Wztaizhang> SXBhgListYCLC(String ycStr, String sysOrgCode);

    List<Wztaizhang> SXListCPLC(String cpStr, String sysOrgCode);

    List<Wztaizhang> SXBhgListCPLC(String cpStr, String sysOrgCode);

    IPage<Map> getSyList(Page<Map> page, QueryVo queryVo);

    String selectJYL(String sysOrgCode, String nodetype, int i);

    String selectJCL(String sysOrgCode, String nodetype);

    String selectUSES(String sysOrgCode, String nodetype);


    List<Wztaizhang> getPiciByStateCailiaoLiaocangGongyingshangNo(Integer jianyanstate, @Param("cailiaono") String cailiaono, @Param("lcbianhao") String lcbianhao, @Param("gongyingshangdanweibianma") String gongyingshangdanweibianma,@Param("orgCode") String orgCode);

    Integer updateUseNumByPici(@Param("num") Double num,@Param("pici") String pici);

    List<Wztaizhang> getListzlpz(String shebeilist);

    @Select("select cailiaoName from ycl_cailiaodict where cailiaoNo = #{cailiaono}")
    String getCaiLiaoName(String cailiaono);

    @Select("select gongyingshangName from ycl_gongyingshang where guid = #{gongyingshangdanweibianma}")
    String getGongYingShangName(String gongyingshangdanweibianma);
}
