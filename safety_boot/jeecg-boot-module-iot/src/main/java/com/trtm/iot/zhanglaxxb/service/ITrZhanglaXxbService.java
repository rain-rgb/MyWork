package com.trtm.iot.zhanglaxxb.service;

import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;

import java.util.List;

/**
 * @Description: 张拉信息表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
public interface ITrZhanglaXxbService extends IService<TrZhanglaXxb> {

    List<TrZhanglaXxb> selectxxbList(String syjid);
    /**
     * 添加一对多
     *
     */
    public void saveMain(TrZhanglaXxb trZhanglaXxb, List<TrZhanglaM> zhanglam);

    List<TrZhanglaXxb> selectLists(String uuid);

    List<TrZhanglaXxb> selectListsss(String shebeiNo,Integer id);
    List<TrZhanglaM> selectListm(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListshyj(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListzjzl(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListbl(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectListzlpz(String shebeibianhao);

    List<TrZhanglaXxb> selectzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectrczlList(String shebeiNo,Integer id);
    List<TrZhanglaXxb> selectytwzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectydzlList(String shebeiNo,Integer id);

    List<TrZhanglaXxb> selectzl47toyd(String shebeiNo,Integer id);

    List<TrZhanglaXxbMStatistics> selectxxbListstatistics(Integer curid, Integer i);

    int updateStatistics(String shebeibianhao,Integer statistics);

    TrZhanglaXxb queryone(String shebeiNo);

    List<TrZhanglaXxb> selectList(String shebeibianh);

    List<TrZhanglaXxb> selectListst(String shebeibianh);

    List<TrZhanglaXxb> selectbigid(Integer curid);

    List<TrZhanglaXxb> selectbigids(Integer curid);

    Integer findXiangMuZSs(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    Integer findRenwudanzs(List<String> zhiliangrenwudanList);

    Integer findRenwudanyjs(List<String> zhiliangrenwudanList);

    Integer findRenwudanbhs(List<String> zhiliangrenwudanList);

    List<TrZhanglaXxb> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<TrZhanglaXxb> selectListss(List<String> list1, String date);

    List<TrZhanglaXxb> selectBYSBList(String shebeilist, Integer curid);

    List<TrZhanglaXxb> getrcwqxzhanglaList();

    List<TrZhanglaXxb> selectlist();

    List<TrZhanglaXxb> selectListoJG(String shebeilist);
}
