package com.trtm.sy.wtgl.qywtd.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.syycjcdj.entity.SyDpsYyYuancaijinchangdengji;
import com.trtm.sy.wtgl.qywtd.entity.QuYangVo;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.wtgl.qywtd.entity.response.ClResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_yy_yuancaiquyangweituo
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface SyDpsYyYuancaiquyangweituoMapper extends BaseMapper<SyDpsYyYuancaiquyangweituo> {

    IPage<QuYangVo> selectQuYangList(Page<QuYangVo> page);

    long countQuYangJiLu(Integer wztzid);

    Map<String, Object> queryUUIDByQrCode(String code);

    Map<String, Object> findByQrCode(String codeString);

    Map<String, Object> findByCodeNo(String codeString);

    SyDpsYyYuancaiquyangweituo findByErWeiMa(String codeString);

    SyDpsYyYuancaiquyangweituo findJinChangDJByEWM(String ewmuuid);

    List<String> getCaiLiaoNo(String nodeType);

    SyDpsYyYuancaijinchangdengji findJinChangDJByYCID(Integer yuancaijinchangdengjiid);

    Map<String, Object> findUUIDByQRCODE(String s);

    IPage<Map> getSyLis(Page<Map> page, QueryVo queryVo);

    IPage<SyDpsYyYuancaiquyangweituo> selectListB(Page<SyDpsYyYuancaiquyangweituo> page, QueryVo queryVo);

    String getContractNumber(String sysOrgCode);

    IPage<Map> getYgList(Page<Map> page, QueryVo queryVo);

    List<ClResponse> getQueryConditionCl(@Param("nodeType") String nodeType, @Param("sysOrgCode") String sysOrgCode);

    List<String> getPhotoUrl(Integer qydid);

    Map getDepartData(String orgCode);
}
