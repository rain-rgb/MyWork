package com.trtm.sy.sydpssysignature.mapper;

import com.trtm.sy.sydpssysignature.entity.SyDpsSySignature;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date:   2023-09-13
 * @Version: V1.0
 */
@Mapper
public interface SyDpsSySignatureMapper extends BaseMapper<SyDpsSySignature> {

    Map<String, Object> selectBYCAID(String caid);

    Map<String, Object> selectSignSql1(String sampleno);

    Map<String, Object> selectSignSql2(String sampleno, String tiNo);

    void updateSignSql1(String baogaoriqi, String sampleno);

    Long selectSignSql3(String id, Integer type);

    Long selectSignSql4(String id);

    void updateSignSql2(String id);

    String selectSignPerson(String sampleno);

    String selectPhoneByName(String username);
}
