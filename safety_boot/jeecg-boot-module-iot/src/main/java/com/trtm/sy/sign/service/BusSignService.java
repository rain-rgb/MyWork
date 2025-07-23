package com.trtm.sy.sign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.response.SdkResponse;
import com.trtm.sy.sign.model.entity.BusSign;


import java.io.FileNotFoundException;
import java.util.Map;

/**
 *
 * @author wcx
 */
public interface BusSignService extends IService<BusSign> {

    SdkResponse<Contract>  getdraft(String name, Map<String, String> personList) throws Exception;

    Long getaddbyfile(String pdfurl, String name, Long contractId) throws FileNotFoundException;

    void getsend(SdkResponse<Contract> sdkResponse, Long documentId, Integer spbnum, Integer num, Integer jlnum);

    void ceshi() throws Exception;

    Object getpersonalsign(Long contractId, String mobile);

    Object getauthurl(String mobile);

    void getemployeeaudit(Long contractId);

    void getcompanysign(Long contractId, Long sealId);

    void getdownload2(Long documentId, String url, String pdfname) throws FileNotFoundException;

    String getdownloadurl(Long contractId) throws Exception;

    void getinvalid(Long contractId, Integer status);

    void getpersonalauth(String mobile);

    String getviewurl(Long contractId, Long documentId);

    String shorturl(Long contractId);

    Object queryJL(String phone);
}
