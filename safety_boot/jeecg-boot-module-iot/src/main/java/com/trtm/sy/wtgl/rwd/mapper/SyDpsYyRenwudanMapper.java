package com.trtm.sy.wtgl.rwd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.sy.wtgl.rwd.entity.SyDpsYyRenwudan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: sy_dps_yy_renwudan
 * @Author: jeecg-boot
 * @Date:   2023-08-31
 * @Version: V1.0
 */
public interface SyDpsYyRenwudanMapper extends BaseMapper<SyDpsYyRenwudan> {

    Map<String, Object> getRwdSaveData(String yuancaijinchangdengjiId);

    void updateSampleTable(@Param("userid") String userid, @Param("renwudanbh") String renwudanbh, @Param("sampleno") String sampleno);

    void updateReportMTable(@Param("userid") String userid, @Param("sampleno") String sampleno);

    void updateTableHeader(@Param("renwudanbh")String renwudanbh, @Param("sampleno") String sampleno);

    void updateQuYangJlTable(@Param("quyangdanliushuihao") String quyangdanliushuihao, @Param("yuancaijinchangdengjiid") Integer yuancaijinchangdengjiid);

    Map<String, Object> getXcwtRelation(Integer xcwtdId);
}
