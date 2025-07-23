package com.trtm.iot.swbhz.mapper;

import java.util.List;

import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.swbhz.entity.BhzSwWarnVO;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface BhzSwBasesMapper extends BaseMapper<BhzSwBases> {

    @Update("update bhz_sw_bases set alertstate=#{alertstate} where guid=#{id}")
    int updatealertsate(String id, Integer alertstate);

    @Update("update bhz_sw_bases set zongchanliang=#{zongchanliang} where guid=#{id}")
    int updateswbhzongliang(String id, Float zongchanliang);

    @Update("update bhz_sw_bases set chaobiaodengji=#{chaobiaodengji} where guid=#{id}")
    int updateswbhzdengji(String id, Integer chaobiaodengji);

    List<BhzSwBases> selectswbhzones(Integer id, Integer alertstate);

    BhzSwWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode);

    List<BhzCementWarnVO> selectWranCountByshebeino(String code, int i);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    Integer selectBiheCount(String orgCode);

    List<BhzSwBases> selectList1(String shebeiNo, Integer id);

    List<BhzSwBases> selectByGuid(String guid);

    @Select("select unit from shebei_info where sbjno = #{shebeibianhao}")
    String getUnit(String shebeibianhao);

    List<BhzSwBases> selectListToJTJT(String shebeilist, Integer curid);

    List<BhzSwBases> selectListToDJ(String shebeilist, Integer curid);

    List<BhzSwBases> selectListToTCP(String shebeilist, Integer curid);

    List<BhzSwBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<BhzSwBases> selectcbList(String shebeilist, Integer curid);

    void updateByGuid(BhzSwBases bhzSwBases);

    List<BhzSwBases> selectkzlist(String shebeilist, Integer curid);
}
