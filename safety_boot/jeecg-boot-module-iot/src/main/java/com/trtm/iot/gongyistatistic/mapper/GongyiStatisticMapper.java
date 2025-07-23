package com.trtm.iot.gongyistatistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: gongyi_statistic
 * @Author: jeecg-boot
 * @Date:   2023-03-06
 * @Version: V1.0
 */
public interface GongyiStatisticMapper extends BaseMapper<GongyiStatistic> {

    @Select("select orgcode_name,shebei_no,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where prontzhi = #{prontzhi} and pront_time like #{prontTime} GROUP BY orgcode_name")
    List<GongyiStatistic> selectCountSum(Integer prontzhi,String prontTime);

    @Select("select orgcode_name,shebei_no,pront_time,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where prontzhi = #{prontzhi} and orgcode_name = #{orgcodeName} and pront_time like #{prontTime1} GROUP BY pront_time")
    List<GongyiStatistic> selectByOrgcodeName(Integer prontzhi,String orgcodeName, String prontTime1);

    @Select("select prontzhi,shebei_no,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where sys_org_code like #{sysOrgCode} and pront_time like #{prontTime} GROUP BY prontzhi")
    List<GongyiStatistic> selectCountSums(String sysOrgCode, String prontTime);

    @Select("select prontzhi,shebei_no,pront_time,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where prontzhi = #{prontzhi} and sys_org_code like #{sysOrgCode} and pront_time like #{prontTime} GROUP BY pront_time")
    List<GongyiStatistic> selectByOrgcodeNames(Integer prontzhi,String sysOrgCode, String prontTime);

    @Select("select shebei_no,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where sys_org_code = #{sysOrgCode}")
    List<GongyiStatistic> selectCountSumss(String sysOrgCode);

    @Select("select shebei_no,sum(zongShu) zongShu,sum(yuJingS) yuJingS,sum(biHeS) biHeS from gongyi_statistic where sys_org_code like #{sysOrgCode} and pront_time like #{prontTime}")
    GongyiStatistic selectCountSumsss(String sysOrgCode,String prontTime);
}
