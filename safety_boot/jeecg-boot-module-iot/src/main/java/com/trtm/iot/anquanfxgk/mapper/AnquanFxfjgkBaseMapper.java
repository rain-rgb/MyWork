package com.trtm.iot.anquanfxgk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: anquan_fxfjgk_base
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
public interface AnquanFxfjgkBaseMapper extends BaseMapper<AnquanFxfjgkBase> {
    /**修改审批状态审批内容
     * @param list guid
     * @param status 状态0同意1不同意
     * @param opinion 审批内容
     * @return
     */
    Integer modifyFxfjgkBaseApprovalStatus(@Param("list") List<String> list,@Param("status") String status,@Param("opinion") String opinion);

    /**根据工点获取对应的风险源点信息
     * @param manageLocation 工点
     * @return
     */
    AnquanFxfjgkBase getAnquanFxfjgkBaseByManage(@Param("manageLocation") String manageLocation);
}
