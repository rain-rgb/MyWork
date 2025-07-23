package com.trtm.iot.yajiangs.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiVo;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.zhangla.entity.OverHandler;
import com.trtm.iot.zhangla.entity.YaJiangVo;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.0
 */
public interface TrYajiangSMapper extends BaseMapper<TrYajiangS> {

    IPage<TrYajiangS> selectListPage(Page<TrYajiangS> page, String shebeis, String shebeibianhao);


    IPage<TrYajiangS> selectChaobiaoListPage(Page<TrYajiangS> page, String shebeis, String shebeibianhao);
    IPage<TrYajiangS> selectChaobiaoListPage1(Page<TrYajiangS> page, String shebeis, String shebeibianhao, Integer overproofStatus);

    @Update("update tr_yajiang_s set overproof_status=#{OverproofStatus} where syjid=#{syjid}")
    int updateoverproofStatus(Integer OverproofStatus, String syjid);

    List<TrYajiangSM> findBysyjid(String syjid);

    //    @Select("select count(*) from tr_yajiang_s")
    Integer count(List<String> sheBs);

    List<String> findSheBeiIds(List<String> querySheBeiList);//

    Integer findXiangMuZS(List<String> sheBeiNoList);//

    Integer findYuJingS(List<String> sheBeiNoList);//

    Integer findBiHeS(List<String> sheBeiNoList);//

    String queryIdBySheBeiNo(String sheBeiNo);//

    Integer queryCount(String id);//

    Integer queryBuhe(String id);//

    Integer queryStatus(String id);//

    String findSheBeiSyJid(String sb);//

    GongYiVo qyeryByXiangMu(String orgCode);

    List<GongYiVo> queryCountBySheBei(Integer orgCategory, String orgCode);

    List<GongYiVo> queryCountZy(Integer orgCategory, String orgCode);


    List<OverHandler> findOverHandler(String syjid);

    Integer findCount(List<String> sheBs);

    Integer selectCo(List<String> sheBs);

    Integer findHeGeCount(List<String> sheBs);

    IPage<YaJiangVo> queryDeatilss(String shebeiNo, Page<YaJiangVo> pageQuery);

    Integer findXiangMuZSs(List<String> sheBs);
    Map<String ,Long> findXiangMutime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findYuJingStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findXiangMuZSss(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    @Select("select * from tr_yajiang_s where syjid = #{syjid}")
    List<TrYajiangS> selectmList(String syjid);

    @Select("select * from tr_yajiang_s where syjid = #{syjid} and is_over_level = 1")
    List<TrYajiangS> selectmnotList(String syjid);

    List<TrYajiangS> selectListbltozl(String shebeino);
}
