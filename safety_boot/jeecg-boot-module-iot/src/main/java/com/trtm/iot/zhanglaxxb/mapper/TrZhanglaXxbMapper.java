package com.trtm.iot.zhanglaxxb.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 张拉信息表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
public interface TrZhanglaXxbMapper extends BaseMapper<TrZhanglaXxb> {

    List<TrZhanglaXxbMStatistics> selectxxbListstatistics(Integer id, Integer statistics);

    @Update("update tr_zhangla_xxb set statistics=#{statistics} where shebeibianhao=#{shebeibianhao}")
    int updateStatistics(String shebeibianhao, Integer statistics);

    TrZhanglaXxb queryone(String shebeiNo);

    List<TrZhanglaXxb> selectListsss(String shebeibianhao,Integer id);

    List<TrZhanglaM> selectListm(String shebeibianhao, Integer id);

    List<TrZhanglaXxb> selectListbl(String shebeibianhao,Integer id);

    List<TrZhanglaXxb> selectListshyj(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListzjzl(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListzlpz(String shebeibianhao);

    List<TrZhanglaXxb> selectzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectrczlList(String shebeiNo,Integer id);
    List<TrZhanglaXxb> selectytwzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectydzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectzl47toyd(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectLists(String uuid);

    List<TrZhanglaXxb> selectListdata(String shebeibianh);

    List<TrZhanglaXxb> selectListst(String shebeibianh);

    @Select("select * from tr_zhangla_xxb where id > #{curid} and over_level = 0 limit 100")
    List<TrZhanglaXxb> selectbigid(Integer curid);

    @Select("select * from tr_zhangla_xxb where id > #{curid} limit 100")
    List<TrZhanglaXxb> selectbigids(Integer curid);

    Integer findYuJingS(List<String> querySheBeiList);

    Integer findBiHeS(List<String> querySheBeiList);

    Integer findXiangMuZS(List<String> querySheBeiList);

    Map<String ,Long> findXiangMutime(List<String> querySheBeiList, String dateNowStr);
    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZStimes(List<String> querySheBeiList,String beginTime, String endTime);

    Integer findYuJingStimes(List<String> querySheBeiList,String beginTime, String endTime);

    Integer findBiHeStimes(List<String> querySheBeiList,String beginTime, String endTime);

    Integer findXiangMuZSs(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    Integer findRenwudanzs(List<String> zhiliangrenwudanList);

    Integer findRenwudanyjs(List<String> zhiliangrenwudanList);

    Integer findRenwudanbhs(List<String> zhiliangrenwudanList);

    List<TrZhanglaXxb> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<TrZhanglaXxb> selectListss(List<String> shebeiList, String date);

    List<TrZhanglaXxb> selectBYSBList(String shebeilist, Integer curid);
    List<TrZhanglaXxb> getrcwqxzhanglaList();

    List<TrZhanglaXxb> selectlist();

    List<TrZhanglaXxb> selectListoJG(String shebeilist);
}
