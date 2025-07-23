package com.trtm.iot.bys.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.bys.entity.BysReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: bys_real
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface BysRealMapper extends BaseMapper<BysReal> {

    @Update("update bys_real set alertstate = #{alertstate} where id = #{id}")
    int updatealertstate(Integer id, Integer alertstate);

    @Update("update bys_real set status = #{status} where id = #{id}")
    int updateStatus(Integer id, Integer status);

    /**
     * APP试验机首页统计  万能机/压力机/标养室/抗压抗折 总数/不合格数/合格数统计
     * @return
     */
    Map stsPageLists3(String shebeis);

    List<BysReal> selectbysone(Integer curid, Integer alertstate);

    BysReal queryone(String shebeiNo);

    List<BysReal> selectListBySbno(String shebeino);

    List<BysReal> selectBysList(Integer curid, String shebeilist);

    List<BysReal> selectBysListkz(Integer curid, String shebeilist);

    BysReal selectBysbno(String shebeino, Integer curid);

    List<BysReal> queryListBySheBeis(String shebei, Integer curid);

    List<BysReal> queryListbim(String shebei, Integer curid);

    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    BysReal selectBysbnotwo(String shebeino, Integer curid);

    // Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);
}
