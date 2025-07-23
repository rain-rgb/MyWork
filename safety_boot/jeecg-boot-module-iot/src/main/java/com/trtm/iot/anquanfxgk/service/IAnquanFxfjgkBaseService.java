package com.trtm.iot.anquanfxgk.service;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: anquan_fxfjgk_base
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
public interface IAnquanFxfjgkBaseService extends IService<AnquanFxfjgkBase> {
    /**修改审批状态审批内容
     * @param list guid
     * @param status 状态0同意1不同意
     * @param opinion 审批内容
     * @return
     */
    Integer modifyFxfjgkBaseApprovalStatus(AnquanFxfjgkBase anquanFxfjgkBase);

    AnquanFxfjgkBase getAnquanFxfjgkBaseByManage(String manageLocation);
}
