package com.trtm.iot.zhanglam.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.trtm.iot.devicepipepilehistorydataone.entity.GongYiVo;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 张拉信息主表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
public interface TrZhanglaMMapper extends BaseMapper<TrZhanglaM> {
     List<Map<String, Object>> countList(@Param("orgCode") String code);

     @Update("update tr_zhangla_m set overproof_status=#{overproofStatus} where syjid=#{syjid}")
     int updateoverproofStatus(Integer overproofStatus, String syjid);

    List<TrZhanglaXxbMStatistics> findBysyjid(String syjid);

    List<TrZhanglaM> selectmList(String syjid);

//    @Select("select count(*) from tr_zhangla_m")
    Integer count(List<String> sheBs);

    Integer findXiangMuZS(List<String> querySheBeiList);//

    Integer findYuJingS(List<String> querySheBeiList);//

    Integer findBiHeS(List<String> querySheBeiList);//

    Integer queryCount(String sheBeiNo);//

    Integer queryBuhe(String sheBeiNo);//

    Integer queryStatus(String sheBeiNo);//

    GongYiVo qyeryByXiangMu(String orgCode);

    List<GongYiVo> queryCountBySheBei(Integer orgCategory, String orgCode);

    List<GongYiVo> queryCountZy(Integer orgCategory, String orgCode);

    @Select("select * from tr_zhangla_m where syjid = #{syjid} and over_level > 0")
    List<TrZhanglaM> selectmnotList(String syjid);

    List<TrZhanglaM> selectListbl(String shebeibianhao);

    @Update("update tr_zhangla_m set shebeibianhao = #{sbjno} where id = #{id}")
    void updateSbjById(String sbjno,Integer id);
}
