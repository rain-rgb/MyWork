package com.trtm.sy.sign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.bean.vo.DocumentUrlVO;
import com.qiyuesuo.sdk.v2.json.JSONObject;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.param.SignParam;
import com.qiyuesuo.sdk.v2.param.UserSignParam;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;
import com.qiyuesuo.sdk.v2.utils.IOUtils;
import com.qiyuesuo.sdk.v2.utils.MapUtils;
import com.trtm.sy.sign.mapper.BusSignMapper;
import com.trtm.sy.sign.model.entity.BusSign;
import com.trtm.sy.sign.model.enums.signEnums;
import com.trtm.sy.sign.service.BusSignService;
import org.jeecg.common.exception.JeecgBootException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wcx
 */
@Service
public class BusSignServiceImpl extends ServiceImpl<BusSignMapper, BusSign> implements BusSignService {
    private static final Logger logger = LoggerFactory.getLogger(BusSignServiceImpl.class);

    @Override
    public SdkResponse<Contract> getdraft(String name, Map<String, String> personList) throws Exception {

        Contract draftContract = new Contract();
        //设置合同主题subject
        draftContract.setSend(false);
        //以下业务分类包含发起方-企业接收方-个人接收方，文件模板中包含两个参数
//        draftContract.setCategory(new Category(2635406141512749699L)); // 设置业务分类ID,套用该业务分类的配置
        // 设置接收方详情，接收方需要与业务分类中设置的签署方流程匹配
        // 公司签署方
        Signatory companySignatory1 = new Signatory();
        companySignatory1.setTenantName(signEnums.companyName);
        companySignatory1.setTenantType("COMPANY");
        companySignatory1.setReceiver(new User("15195880106", "MOBILE"));
        companySignatory1.setSerialNo(1);

//        if (name.equals("检测报告")) {
//            draftContract.setSubject("检测报告");
//            reportActions(draftContract, companySignatory1, personList);
//
//        } else if (name.equals("检测记录")) {
//            draftContract.setSubject("检测记录");
//            jlActions(draftContract, companySignatory1, personList);
//        }
        draftContract.setSubject("检测报告");
        allActions(draftContract, companySignatory1, personList);

        //发起请求
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        String response = null;
        try {
            response = client.service(new ContractDraftRequest(draftContract));
        } catch (Exception e) {
            throw new Exception("创建合同草稿请求服务器失败，失败原因：" + e.getMessage());
        }
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response, Contract.class);
        if (!sdkResponse.getCode().equals(0)) {
            throw new Exception("创建合同草稿失败，失败原因：" + sdkResponse.getMessage());
        }
        return sdkResponse;

    }

    private void allActions(Contract draftContract, Signatory companySignatory1, Map<String, String> personList) {
        //签署动作
        List<Action> actions = new ArrayList<>();
        action(actions, "报告检测签字", "PERSONAL", 1, personList.get("jcry"));
        action(actions, "报告审核签字", "PERSONAL", 2, personList.get("fhry"));
        action(actions, "报告批准签字", "PERSONAL", 3, personList.get("qfry"));
        action(actions, "报告表盖章", "COMPANY", 4, personList.get("qfry"));

        action(actions, "记录表检测签字", "PERSONAL", 5, personList.get("jcry"));
        action(actions, "记录表记录签字", "PERSONAL", 6, personList.get("jlry"));
        action(actions, "记录表复核签字", "PERSONAL", 7, personList.get("fhry"));
        action(actions, "记录表盖章", "COMPANY", 8, personList.get("fhry"));
        companySignatory1.setActions(actions);
        draftContract.addSignatory(companySignatory1);
    }

    /**
     * 记录流程
     */
    private void jlActions(Contract draftContract, Signatory companySignatory1, Map<String, String> personList) {
        //签署动作
        List<Action> actions = new ArrayList<>();
        action(actions, "记录表检测签字", "PERSONAL", 4, personList.get("jcry"));
        action(actions, "记录表记录签字", "PERSONAL", 5, personList.get("jlry"));
        action(actions, "记录表复核签字", "PERSONAL", 6, personList.get("fhry"));
        action(actions, "记录表盖章", "COMPANY", 7, personList.get("fhry"));
        companySignatory1.setActions(actions);
        draftContract.addSignatory(companySignatory1);
    }

    /**
     * 报告流程
     */
    private void reportActions(Contract draftContract, Signatory companySignatory1, Map<String, String> personList) {
        //签署动作
        List<Action> actions = new ArrayList<>();
        action(actions, "施工单位试验室负责人签字", "PERSONAL", 1, personList.get("sgdwfzr"));
        action(actions, "监理单位试验室负责人签字", "PERSONAL", 2, personList.get("jldwfzr"));
        action(actions, "审批表盖章", "COMPANY", 3, personList.get("jldwfzr"));
        companySignatory1.setActions(actions);
        draftContract.addSignatory(companySignatory1);
    }

    private void action(List<Action> actions, String name, String type, int i, String mobile) {
        //签字
        Action action = new Action();
        action.setName(name);
        action.setType(type);
        action.setSerialNo(i);//签署顺序
        //签字人信息
        User user = new User(mobile, "MOBILE");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        action.setOperators(userList);//添加操作人
        actions.add(action);
    }


    @Override
    public Long getaddbyfile(String pdfurl, String name, Long contractId) throws FileNotFoundException {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        // 添加合同文档
        DocumentAddByFileRequest request = new DocumentAddByFileRequest(contractId,
                new com.qiyuesuo.sdk.v2.http.StreamFile(new FileInputStream(pdfurl)), "pdf", name);
        String response = client.service(request);
        SdkResponse<DocumentAddResult> responseObj = JSONUtils.toQysResponse(response, DocumentAddResult.class);
        if (responseObj.getCode() == 0) {
            DocumentAddResult result = responseObj.getResult();
            logger.info("添加合同文档成功，文档ID:{}", result.getDocumentId());
            return result.getDocumentId();
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            throw new JeecgBootException("添加合同文档失败：" + responseObj.getMessage());
        }
    }

    @Override
    public void getsend(SdkResponse<Contract> sdkResponse, Long documentId, Integer spbnum, Integer bgnum, Integer jlnum) {
        //获取合同ID
        Long contractId = sdkResponse.getResult().getId();
        List<Signatory> signatories = sdkResponse.getResult().getSignatories();
        List<Action> actions = signatories.get(0).getActions();
        List<Stamper> stampers = new ArrayList<>();
        //操作节点添加操作动作和位置

        for (int i = 0; i < actions.size(); i++) {
//            if (actions.get(i).getName().equals("施工单位试验室负责人签字")) {
//                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", 0, -0.035, "检测：", 0, 0);
//            }
//            if (actions.get(i).getName().equals("监理单位试验室负责人签字")) {
//                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", 0, -0.035, "记录：", 0, 0);
//            }
//            if (actions.get(i).getName().equals("审批表盖章")) {
//                getStampers(stampers, actions.get(i).getId(), documentId, "COMPANY", 0.215, 0.815, null, 0, 0, spbnum);
//                getStampers(stampers, actions.get(i).getId(), documentId, "TIMESTAMP", 0, -0.01, "日期：", 0, 0, spbnum);
//            }
            if (actions.get(i).getName().equals("报告检测签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", -0.035, -0.035, "检测：", bgnum, 0, spbnum);
            }
            if (actions.get(i).getName().equals("报告审核签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", -0.035, -0.035, "审核：", bgnum, 0, spbnum);
            }
            if (actions.get(i).getName().equals("报告批准签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", -0.035, -0.035, "批准：", bgnum, 0, spbnum);
            }
            if (actions.get(i).getName().equals("报告表盖章")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "COMPANY", 0.215, 0.815, null, bgnum, 0, spbnum);
                getStampers(stampers, actions.get(i).getId(), documentId, "TIMESTAMP", -0.035, -0.01, "日期：", bgnum, 0, spbnum
                );
            }
            if (actions.get(i).getName().equals("记录表检测签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", -0.035, -0.035, "检测：", bgnum, jlnum, spbnum);
            }
            if (actions.get(i).getName().equals("记录表记录签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", 0, -0.035, "记录：", bgnum, jlnum, spbnum);
            }
            if (actions.get(i).getName().equals("记录表复核签字")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "PERSONAL", 0, -0.035, "复核：", bgnum, jlnum, spbnum);
            }
            if (actions.get(i).getName().equals("记录表盖章")) {
                getStampers(stampers, actions.get(i).getId(), documentId, "COMPANY", 0.215, 0.815, null, bgnum, jlnum, spbnum);
                getStampers(stampers, actions.get(i).getId(), documentId, "TIMESTAMP", 0, -0.01, "日期：", bgnum, jlnum, spbnum);
            }
        }
        // 发起合同
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        ContractSendRequest request = new ContractSendRequest(contractId, stampers);
        String response = client.service(request);
        SdkResponse<Object> responseObj = JSONUtils.toQysResponse(response);
        if (responseObj.getCode() == 0) {
            logger.info("合同发起成功");
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            throw new JeecgBootException("合同发起失败：" + responseObj.getMessage());
        }
    }

    @Override
    public void ceshi() throws Exception {
//        Map<String, String> personList = new HashMap<>();
//        personList.put("jcry", "15270292717");
//        personList.put("shry", "15270292717");
//        personList.put("qfry", "15270292717");
//        SdkResponse<Contract> sdkResponse = getdraft("记录", personList, null);
//        Long documentid = getaddbyfile("D:\\suxin\\qianzhang\\pdf.pdf", "钢筋原材料试验检测报告", sdkResponse.getResult().getId());
//        getsend(sdkResponse, documentid, "", 1, jlnum);
    }

    @Override
    public Object getpersonalsign(Long contractId, String mobile) {
        // 签署个人签名
        UserSignParam param = new UserSignParam();
        param.setUser(new User(mobile, "MOBILE"));
        param.setContractId(contractId);
        //企业名称
        Company company = new Company();
        company.setName(signEnums.companyName);
        param.setCompanySignatory(company);
        ContractSignUserRequest request = new ContractSignUserRequest(param);
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        String response = client.service(request);
        SdkResponse responseObj = JSONUtils.toQysResponse(response);
        if (responseObj.getCode() == 0) {
            logger.info("签署成功");
            return true;
        } else {
            logger.info("签署失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            throw new JeecgBootException("签署失败：" + responseObj.getMessage());
        }
    }

    @Override
    public Object getauthurl(String mobile) {
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        // 添加时间间隔
        calendar.add(Calendar.MINUTE, 30);     // 增加30分钟
        // 获取新的日期和时间
        Date newDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(newDate);
        //获取授权链接
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        PersonalSignAuthUrlRequest request = new PersonalSignAuthUrlRequest(new User(mobile, "MOBILE"), format1, "auth");
        String response = client.service(request);
        SdkResponse<PersonalSignAuthUrlResult> responseObj = JSONUtils.toQysResponse(response, PersonalSignAuthUrlResult.class);
        if (responseObj.getCode().equals(0)) {
            return responseObj.getResult().getPageUrl();
        } else {
            logger.info("获取授权链接失败,失败原因：{}", responseObj.getMessage());
            throw new JeecgBootException("获取授权链接失败：" + responseObj.getMessage());
        }
    }

    @Override
    public void getemployeeaudit(Long contractId) {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        // 审批
        ContractAuditRequest request = new ContractAuditRequest(contractId, true, "审批通过");
        String response = client.service(request);
        SdkResponse responseObj = JSONUtils.toQysResponse(response);
        if (responseObj.getCode() == 0) {
            logger.info("审批成功");
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
        }
    }

    @Override
    public void getcompanysign(Long contractId, Long sealId) {
        // 签署公章
        SignParam param = new SignParam();
        param.setContractId(contractId);
        param.setSealId(sealId);
        ContractSignCompanyRequest request = new ContractSignCompanyRequest(param);
        //调用接口
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        String response = client.service(request);
        SdkResponse<Object> responseObj = JSONUtils.toQysResponse(response);
        if (responseObj.getCode() == 0) {
            logger.info("公章签署成功");
        } else {
            logger.info("公章签署失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            throw new JeecgBootException("公章签署失败,错误信息:" + responseObj.getMessage());
        }
    }

    @Override
    public void getdownload2(Long documentId, String url, String pdfname) throws FileNotFoundException {
        // 下载合同文档
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        DocumentDownloadRequest request = new DocumentDownloadRequest(documentId);
        FileOutputStream fos = new FileOutputStream(url + pdfname);
        client.download(request, fos);
        IOUtils.safeClose(fos);
        logger.info("下载合同文档成功");
    }


    @Override
    public String getdownloadurl(Long contractId) throws Exception {
        //调用合同下载链接接口
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        ContractDownloadUrlRequest downloadRequest = new ContractDownloadUrlRequest(contractId);
        //下载文件参数
        downloadRequest.addDownloadItem("CONTRACT");
        String response = client.service(downloadRequest);
        SdkResponse<ContractDownloadUrlResult> responseObj = JSONUtils.toQysResponse(response, ContractDownloadUrlResult.class);
        if (responseObj.getCode().equals(0)) {
            logger.info("合同下载接口请求成功");
            List<DocumentUrlVO> downloadUrls = responseObj.getResult().getDownloadUrls();
            return downloadUrls.get(0).getDownloadUrl();
        } else {
            logger.info("获取授权链接失败,失败原因：{}", responseObj.getMessage());
            throw new JeecgBootException("获取授权链接失败：" + responseObj.getMessage());
        }
    }

    @Override
    public void getinvalid(Long contractId, Integer status) {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        if (status == 1) {
            // “已完成”状态下请求作废合同，同时发起方签署作废合同
            ContractInvalidRequest request = new ContractInvalidRequest(contractId, null, "请求作废合同", false);
            String response = client.service(request);
            SdkResponse responseObj = JSONUtils.toQysResponse(response);
            if (responseObj.getCode() == 0) {
                logger.info("合同作废请求成功");
            } else {
                logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            }
        } else if (status == 0) {
            // “签署中”状态下撤回合同
            ContractInvalidRequest request = new ContractInvalidRequest(contractId, "撤回合同");
            String response = client.service(request);
            SdkResponse responseObj = JSONUtils.toQysResponse(response);
            if (responseObj.getCode() == 0) {
                logger.info("合同撤回成功");
            } else {
                logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            }
        }
    }

    @Override
    public void getpersonalauth(String mobile) {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        UserAuthPageRequest request = new UserAuthPageRequest();
        request.setUser(new User(mobile, "MOBILE"));
        String response = client.service(request);
        SdkResponse<UserAuthPageResult> responseObj = JSONUtils.toQysResponse(response, UserAuthPageResult.class);
        if (responseObj.getCode() == 0) {
            UserAuthPageResult result = responseObj.getResult();
            logger.info("请求成功，认证链接:{}", result.getAuthUrl());
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
        }
    }

    @Override
    public String getviewurl(Long contractId, Long documentId) {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        ContractViewPageRequest request = new ContractViewPageRequest(contractId);
        String response = client.service(request);
        SdkResponse<ContractPageResult> responseObj = JSONUtils.toQysResponse(response, ContractPageResult.class);
        if (responseObj.getCode() == 0) {
            ContractPageResult result = responseObj.getResult();
            return result.getPageUrl();
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            return responseObj.getMessage();
        }
    }

    @Override
    public String shorturl(Long contractId) {
        SdkClient client = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        ContractShorturlRequest request = new ContractShorturlRequest(contractId);
        String response = client.service(request);
        SdkResponse<ContractShorturlResult> responseObj = JSONUtils.toQysResponse(response, ContractShorturlResult.class);
        if (responseObj.getCode() == 0) {
            ContractShorturlResult result = responseObj.getResult();
            return result.getShorturl();
        } else {
            logger.info("请求失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
            return responseObj.getMessage();
        }

    }

    @Override
    public Object queryJL(String phone) {
        SdkClient sdkClient = new SdkClient(signEnums.baseServiceUrl, signEnums.appToken, signEnums.appSecret);
        PersonalSignAuthQueryRequest request = new PersonalSignAuthQueryRequest(new User(phone, "MOBILE"), PersonalAuthStatus.authorized);
        String response = sdkClient.service(request);
        JSONObject jsonObject = new JSONObject(response);
        Map<String, Object> map = jsonObject.toMap();
        Integer code = (Integer) map.get("code");
        if (code == 1005) {
            throw new JeecgBootException("授权已过期，请重新授权");
        }
        String message = (String) map.get("message");
        List<PersonalAuthAutoSignResult> result = (List<PersonalAuthAutoSignResult>) map.get("result");
        SdkResponse responseObject = new SdkResponse(code, message, result);
        if (responseObject.getCode().equals(0)) {
            return responseObject.getResult();
        } else {
            logger.info("授权记录查询失败,失败原因：{}", responseObject.getMessage());
            throw new JeecgBootException("授权记录查询失败:" + responseObject.getResult());
        }
    }


    /**
     * 签署位置接口
     */
    @NotNull
    private void getStampers(List<Stamper> stampers, Long actionid, Long documentid, String type, double firstX,
                             double firstY, String keyword, int bgnum, int jlnum, int spbnum) {
        int num = 1;
        int j = 1;
        if (bgnum != 0 && jlnum == 0) {
            num = bgnum;
            j = 1 ;
        } else if (bgnum != 0 && jlnum != 0) {
            num = jlnum;
            j = bgnum + 1 ;
        } else if (bgnum == 0 && jlnum != 0) {
            num = jlnum;
        }


        for (int i = 0; i < num; i++) {
            //签署位置
            Stamper stamper = new Stamper();
            stamper.setActionId(actionid);
            stamper.setDocumentId(documentid);
            stamper.setType(type);
            stamper.setPage(i + j);
            if (keyword != null) {
                if (keyword.equals("记录：") || keyword.equals("复核：")) {
                    stamper.setKeyword(keyword);
                    stamper.setKeywordIndex(i + j - 1);
                } else {
                    stamper.setKeyword(keyword);
                    stamper.setKeywordIndex(i + j);
                }
            }
            stamper.setOffsetX(firstX);
            stamper.setOffsetY(firstY);
            if (type.equals("TIMESTAMP")) {
                stamper.setDatePattern("Chinese");
            }
            //添加
            stampers.add(stamper);
        }
    }
}
