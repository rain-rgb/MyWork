package com.trtm.sy.sydpssysignature.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sydpssysignature.entity.SyDpsSySignature;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date:   2023-09-13
 * @Version: V1.0
 */
public interface ISyDpsSySignatureService extends IService<SyDpsSySignature> {

    void agree(HttpServletRequest request, MultipartFile file, HttpServletResponse response);

    void createSign(String name, Map<String, String> personList, String pdfurl, Integer spbnum,Integer bgnum, Integer jlnum, String sampleno);

    IPage gridNew(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, String tiNo, String username, String state, String self, String shenpizhuangtai);


    String sign(HashMap<String, Object> map);
}
