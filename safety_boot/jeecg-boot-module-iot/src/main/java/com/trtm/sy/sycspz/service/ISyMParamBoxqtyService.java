package com.trtm.sy.sycspz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.sy.sycspz.entity.SyMParamBoxqty;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Description: sy_m_param_boxqty
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
public interface ISyMParamBoxqtyService extends IService<SyMParamBoxqty> {

    IPage<Map<String, Object>> mparamraboxqtyList(Integer offset, Integer limit, String boxno, HttpServletRequest request, HttpServletResponse response);

    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException;
}
