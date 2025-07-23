package com.trtm.iot.ycltd.mapper;

import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @Description: 原材料试验详情Mapper
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
public interface YclTestDetailMapper extends BaseMapper<YclTestDetail> {

    @Update("update wztaizhang set jianyanstate = #{jianyanstate} where pici = #{pici}")
    boolean updateJYStateByPici(Integer jianyanstate, String pici);

    @Update("update wztaizhang set choujianstate = #{choujianstate} where pici = #{pici}")
    boolean updateCJStateByPici(Integer choujianstate, String pici);

    @Select("select * from ycl_test_detail where inspection_lot_number = #{inspectionLotNumber} and sample_number = #{sapmleNumber}")
    YclTestDetail selectBySampleNumber(String inspectionLotNumber, String sapmleNumber);

    boolean updateBySampleNumber(String storageId, String inspectionLotNumber, String testName, Date samplingTime,
                                 Date testTime, Integer jianyanType, String conclusion, Integer testStatus, String tester,
                                 String report, String createBy, Date createTime, String updateTime, String sampleNumber);
}
