package com.qiyuesuo.sample.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import org.jeecg.common.exception.JeecgBootException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiyuesuo.sample.api.exception.BizException;
import com.qiyuesuo.sample.api.utils.IOUtils;
import com.qiyuesuo.sample.api.utils.crypt.MD5;
import com.qiyuesuo.sample.api.utils.http.FileItem;
import com.qiyuesuo.sample.api.utils.http.HttpClient;
import com.qiyuesuo.sample.api.utils.http.HttpHeader;
import com.qiyuesuo.sample.api.utils.http.HttpParamers;
import com.qiyuesuo.sample.api.utils.http.StreamFile;
import com.qiyuesuo.sample.api.utils.json.JSONObject;
import com.qiyuesuo.sample.api.utils.json.JSONUtils;

/**
 * 单方签署 API 调用示例。
 * 场景：学校（或公司）签署签署证明文件。
 * 步骤：
 * 1、传入要签署的文件，在契约锁中生成「签署文档」；
 * 2、传入签署相关信息，在契约锁中创建并发起「电子签约」；
 * 3、传入要签署的签署方信息以及签署位置等信息，签署「电子签约」；
 * 4、下载签署后的文档；
 */
public class TestSign {
    private static final Logger logger = LoggerFactory.getLogger(SingleSignApiSample.class);
    public static String baseServiceUrl = "http://47.111.9.185:9182"; // 替换为实际的【契约锁私有云开放平台】地址
    public static String appToken = "IPUw7d51qS"; // 【契约锁私有云控制台】创建应用后生成的 AppToken
    public static String appSecret = "D0cms5qbH9jvGJ1qnuua19zUG6LuB4"; // 【契约锁私有云控制台】创建应用后生成的 AppSecret
    public static HttpHeader header = null;

    public static String categoryId = "3114135844814213392"; // 用印流程 ID，需要在[契约锁电子签署平台]-[印控]-[用印流程]中新增并启用用印流程。
    public static String sealId = "3114135597589352680"; // 印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    public static String companyName = "杭州高讯物联网科技有限公司"; // 组织名称
    public static String mobile = "15957140410"; // 创建人手机号


    public static String createbyfile(String pdfurl) throws Exception {
        /**
         * 第一步：创建签署文档，调用接口【上传本地文件创建签署文档】，获取文档 ID。
         */
        HttpParamers createDocuemntParams = HttpParamers.httpPostParamers(); // post 接口
        createDocuemntParams.addParam("title", "物联网指令单");
        createDocuemntParams.addParam("fileType", "pdf");
//        FileInputStream in = new FileInputStream("https://safety-boots.oss-cn-beijing.aliyuncs.com/upload/物联网指令单_1688715744906.pdf");
//        FileItem file = new StreamFile(in);
//        createDocuemntParams.addFile("file", file);
        // 读取PDF文件的URL
        URL pdfUrl = new URL(pdfurl);
        // 打开PDF文件连接
        HttpURLConnection pdfConnection = (HttpURLConnection) pdfUrl.openConnection();
        // 设置PDF文件请求方法为GET
        pdfConnection.setRequestMethod("GET");
        // 获取响应码
        int responseCode = pdfConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 获取PDF文件内容的输入流
            InputStream inputStream = new BufferedInputStream(pdfConnection.getInputStream());
            FileItem file = new StreamFile(inputStream);
            createDocuemntParams.addFile("file", file);
        } else {
            throw new JeecgBootException("无法下载PDF文件。服务器响应码：" + responseCode);
        }

        String documentId = null;
        String createDocumentUrl = baseServiceUrl + "/v2/document/createbyfile";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doService(createDocumentUrl, createDocuemntParams, header, 15000, 60000);
            Map<String, Object> result = handleHttpResponse(response);
            // 如果调用接口成功，则文档 ID
            documentId = (String) ((Map<String, Object>) result.get("result")).get("documentId");
            logger.info("创建「文档」成功，documentId:{}", documentId);
            // 关闭PDF文件连接
            pdfConnection.disconnect();
            return documentId;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("create file error", e);
            throw e;
        }
    }

    public static String createbycategory(String documentId, List<Map<String, String>> contactList) throws Exception {
        /**
         * 第二步：使用第一步生成的文档 ID 创建并发起电子签约，调用接口【创建或发起电子签约】，获取【电子签约】ID。
         */
        // 签署方list
        List<Map<String, Object>> signatories = new ArrayList<Map<String, Object>>();

        // 签署方下的签署节点
        int i = 0;
        for (Map<String, String> map : contactList) {
            i++;
            String operatorContact = map.get("operatorContact");
            String operatorName = map.get("operatorName");
            Map<String, Object> signatory = new HashMap<String, Object>();
            signatory.put("tenantType", "COMPANY"); // COMPANY表示签署方类型是公司,PERSONAL表示签署方是个人
            signatory.put("tenantName", companyName); // 签署方名称-公司名称
            signatory.put("signatoryNo", IdUtil.randomUUID()); // 签署方编号
            signatory.put("contact", operatorContact); // 组织签署方传入经办人联系方式，个人签署方传入个人联系方式
            signatory.put("serialNo", 1); // 签署顺序，最小为1，表示第一个签署
            if (i == 2) {
                Map<String, Object> action = new HashMap<String, Object>();
                action.put("type", "PERSONAL"); // 节点类型，CORPORATE（企业签章），PERSONAL（个人签字）
                action.put("name", "签字动作"); // 节点名称
                action.put("serialNo", 1); // 节点顺序，最小为1，表示第一个签署
                Map<String, Object> action2 = new HashMap<String, Object>();
                action2.put("type", "CORPORATE"); // 节点类型，CORPORATE（企业签章），PERSONAL（个人签字）
                action2.put("name", "盖章动作"); // 节点名称
                action2.put("serialNo", 2); // 节点顺序，最小为1，表示第一个签署
                // 指定签署节点的签署人
                List<Map<String, Object>> actionOperators = new LinkedList<>();
                Map map1 = new HashMap();
                map1.put("operatorName", operatorName);
                map1.put("operatorContact", operatorContact);
                actionOperators.add(map1);
                List<Map<String, Object>> locations = new LinkedList<>();
                Map map2 = new HashMap();
                map2.put("documentId", documentId);
                map2.put("rectType", "SEAL_PERSONAL");
//                map2.put("keyword","总监理工程师");
                map2.put("offsetX", 0.686);
                map2.put("offsetY", 0.314);
                map2.put("page", 1);

                Map map3 = new HashMap();
                map3.put("documentId", documentId);
                map3.put("rectType", "TIMESTAMP");
//                map3.put("keyword","日期");
//                map3.put("keywordIndex",2);
                map3.put("offsetX", 0.686);
                map3.put("offsetY", 0.304);
                map3.put("page", 1);

                locations.add(map3);
//                map2.put("relativePosition",)
                locations.add(map2);
                action.put("actionOperators", actionOperators);
                action.put("locations", locations);
                List<Map> actions = new ArrayList<>();
                actions.add(action);
                actions.add(action2);
                signatory.put("actions", actions); // 签署节点，可以有多个，例如可以有「签署公章」「法人签署」「经办人签署」等
            } else if (i == 1) {
                Map<String, Object> action = new HashMap<String, Object>();
                action.put("type", "PERSONAL"); // 节点类型，CORPORATE（企业签章），PERSONAL（个人签字）
                action.put("name", "签字动作"); // 节点名称
                action.put("serialNo", 1); // 节点顺序，最小为1，表示第一个签署
                // 指定签署节点的签署人
                List<Map<String, Object>> actionOperators = new LinkedList<>();
                Map map1 = new HashMap();
                map1.put("operatorName", operatorName);
                map1.put("operatorContact", operatorContact);
                actionOperators.add(map1);
                List<Map<String, Object>> locations = new LinkedList<>();
                Map map2 = new HashMap();
                map2.put("documentId", documentId);
                map2.put("rectType", "SEAL_PERSONAL");
//                map2.put("keyword","专业监理工程师");
                map2.put("offsetX", 0.279);
                map2.put("offsetY", 0.308);
                map2.put("page", 1);

                Map map3 = new HashMap();
                map3.put("documentId", documentId);
                map3.put("rectType", "TIMESTAMP");
//                map3.put("keyword","日期");
//                map3.put("keywordIndex",1);
                map3.put("offsetX", 0.279);
                map3.put("offsetY", 0.290);

                map3.put("page", 1);

                locations.add(map3);
                locations.add(map2);
                action.put("actionOperators", actionOperators);
                action.put("locations", locations);
                List<Map> actions = new ArrayList<>();
                actions.add(action);
                signatory.put("actions", actions); // 签署节点，可以有多个，例如可以有「签署公章」「法人签署」「经办人签署」等
            } else if (i == 3) {
                Map<String, Object> action = new HashMap<String, Object>();
                action.put("type", "PERSONAL"); // 节点类型，CORPORATE（企业签章），PERSONAL（个人签字）
                action.put("name", "签字动作"); // 节点名称
                action.put("serialNo", 1); // 节点顺序，最小为1，表示第一个签署
                // 指定签署节点的签署人
                List<Map<String, Object>> actionOperators = new LinkedList<>();
                Map map1 = new HashMap();
                map1.put("operatorName", operatorName);
                map1.put("operatorContact", operatorContact);
                actionOperators.add(map1);
                List<Map<String, Object>> locations = new LinkedList<>();
                Map map2 = new HashMap();
                map2.put("documentId", documentId);
                map2.put("rectType", "SEAL_PERSONAL");
//                map2.put("keyword","施工单位收到签");
                map2.put("offsetX", 0.686);
                map2.put("offsetY", 0.240);
                map2.put("page", 1);
                Map map3 = new HashMap();
                map3.put("documentId", documentId);
                map3.put("rectType", "TIMESTAMP");
//                map3.put("keyword","日期");
//                map3.put("keywordIndex",4);
                map3.put("offsetX", 0.686);
                map3.put("offsetY", 0.216);

                map3.put("page", 1);

                locations.add(map3);
                locations.add(map2);
                action.put("actionOperators", actionOperators);
                action.put("locations", locations);
                List<Map> actions = new ArrayList<>();
                actions.add(action);
                signatory.put("actions", actions); // 签署节点，可以有多个，例如可以有「签署公章」「法人签署」「经办人签署」等
            }
            signatories.add(signatory);
        }
        // 【电子签约】的创建人
        Map<String, Object> creatorInfo = new HashMap<String, Object>();
        creatorInfo.put("mobile", mobile); // 创建人手机号

        Map<String, Object> createContractRequest = new HashMap<String, Object>();
        createContractRequest.put("subject", "物联网指令单"); // 电子签约名称
        createContractRequest.put("categoryId", categoryId); // 用印流程 ID
        createContractRequest.put("send", true); // 是否立即发起合同
        createContractRequest.put("tenantName", companyName); // 发起方名称
        createContractRequest.put("expireTime", "2023-08-09"); // 合同过期时间
        createContractRequest.put("creatorInfo", creatorInfo); // 创建人


        List<String> documents = new ArrayList<String>();
        documents.add(documentId);
        createContractRequest.put("documents", documents); // 签署文档
        createContractRequest.put("signatories", signatories); // 签署方

        String createContractParamsJson = JSONUtils.toJson(createContractRequest); // content-type 为 json，需要将参数转换为 json 字符串
        System.out.println("createContractParamsJson = " + createContractParamsJson);
        String contractId = null;
        String createContractUrl = baseServiceUrl + "/contract/createbycategory";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(createContractUrl, createContractParamsJson, header, 15000, 60000);
            Map<String, Object> result = handleHttpResponse(response);
            // 如果调用成功，记录[电子签约]ID
            contractId = (String) result.get("contractId");
            logger.info("创建「电子签约」成功，contractId:{}", contractId);
            return contractId;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("create contract error", e);
            throw e;
        }
    }


    public static void signbycompany(String documentId, String contractId, String keyword) throws Exception {
        /**
         * 第三步：签署【电子签约】，调用接口【法人单位印章静默签署v2】
         */
        // 签署位置
        Map<String, Object> stamper = new HashMap<String, Object>();
        stamper.put("documentId", documentId);
        stamper.put("type", "SEAL_CORPORATE"); // 签章类型
        stamper.put("sealId", sealId); // 印章 ID
        stamper.put("keyword", keyword); // 关键字；通过关键字定位签署位置
        stamper.put("offsetX", 0); // 横坐标偏移；此处为：通过关键字定位签署位置后，再向左偏移0.1个坐标


        Map<String, Object> company = new HashMap<String, Object>();
        company.put("name", companyName); // 签署方名称

        Map<String, Object> contract = new HashMap<String, Object>();
        contract.put("id", contractId); // 电子签约 ID

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contract", contract); // 需要签署的[电子签约]
        signRequest.put("company", company); // 需要签署的[签署方]
        List<Map<String, Object>> stampers = new ArrayList<Map<String, Object>>();
        stampers.add(stamper);
        signRequest.put("stampers", stampers); // 签署位置

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/contract/signbycompany";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            handleHttpResponse(response); // 此方法会校验 code，只要校验通过则表明签署成功
            logger.info("签署成功");
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    public static void download(String documentId) throws Exception {
        /**
         * 第四步：下载签署后的文档，调用接口【下载签署任务文档】
         */
        HttpParamers downloadParams = HttpParamers.httpGetParamers(); // 接口方法为 get
        downloadParams.addParam("documentId", documentId);

        String downloadDocumentUrl = baseServiceUrl + "/document/download";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D:\\javawork\\safety_boot\\jeecg-boot-module-demo\\src\\main\\java\\com\\qiyuesuo\\sample\\api\\singleSignApiDoc.pdf");
            HttpClient.doDownload(downloadDocumentUrl, downloadParams, header, 15000, 60000, outputStream);
            // doDownload 正常执行，不报错，则下载成功
            logger.info("下载成功");
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("download error", e);
            throw e;
        } finally {
            IOUtils.safeClose(outputStream);
        }
    }

    /**
     * 单位印章静默签署授权链接
     *
     * @return
     */
    public static String companysignsilent() throws Exception {
        Map<String, Object> authCompany = new HashMap<>();
        authCompany.put("name", companyName);

        Map<String, Object> authUser = new HashMap<>();
        authUser.put("mobile", mobile);

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("authCompany", authCompany);
        signRequest.put("authUser", authUser);
        signRequest.put("authScope", "签署全部合同");

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/auth/companysignsilent/url";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);
            Map<String, Object> result = (Map<String, Object>) map.get("result");
            String pageUrl = result.get("url").toString();
            logger.info(handleHttpResponse(response).toString());
            return pageUrl;

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 个人签名静默签署授权链接
     *
     * @return
     */
    public static String personalsignsilent(String mobile) throws Exception {

        Map<String, Object> authUser = new HashMap<>();
        authUser.put("mobile", mobile);

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("authUser", authUser);
        signRequest.put("authScope", "签署全部合同");

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/auth/personalsignsilent/url";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);
            Map<String, Object> result = (Map<String, Object>) map.get("result");
            String pageUrl = result.get("url").toString();
            logger.info(handleHttpResponse(response).toString());
            return pageUrl;

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }
    /**
     * 文件页面
     *
     * @return
     */
    public static String pageurl(String contractID,String mobile) throws Exception {

        Map<String, Object>  contract = new HashMap<>();
        contract.put("id", contractID);

        Map<String, Object>  operator = new HashMap<>();
        operator.put("mobile", mobile);


        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contract", contract);
        signRequest.put("operator", operator);

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/contract/pageurl";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);
            Map<String, Object> result = (Map<String, Object>) map.get("result");
            String pageUrl = result.get("url").toString();
            logger.info(handleHttpResponse(response).toString());
            return pageUrl;

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }
    /**
     * 文件签署页面v3
     *
     * @return
     */
    public static String signurl(String contractID,String mobile) throws Exception {


        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contractId", contractID);
        signRequest.put("tenantType", "COMPANY");
        signRequest.put("contact", mobile);
        signRequest.put("tenantName", companyName);

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/contract/signurl/v3";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);
            String signUrl1 = map.get("signUrl").toString();
            logger.info(handleHttpResponse(response).toString());
            return signUrl1;

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 查询单位静默签授权记录
     */
    public static void record() throws Exception {
        Map<String, Object> authCompany = new HashMap<>();
        authCompany.put("name", companyName);

        Map<String, Object> authUser = new HashMap<>();
        authUser.put("mobile", mobile);

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("authCompany", authCompany);
        signRequest.put("authUser", authUser);
        signRequest.put("authScope", "签署全部合同");

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/auth/signsilent/company/record";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            handleHttpResponse(response);
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 个人签名创建-链接页面
     */
    public static void createurl(String mobile) throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("mobile", mobile);

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("user", user);

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/seal/personal/createurl";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);
            Map<String, Object> result = (Map<String, Object>) map.get("result");
            String pageUrl = result.get("pageUrl").toString();
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 创建内部员工
     */
    public static void createEmployee(String name, String contact) throws Exception {
        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("name", name);
        signRequest.put("companyName", companyName);
        signRequest.put("contact", contact);

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/employee/create";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 个人静默签署v2
     */
    public static void signbyperson(String contractId, String mobile) throws Exception {

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("mobile", mobile);

        Map<String, Object> contract = new HashMap<String, Object>();
        contract.put("id", Long.parseLong(contractId));

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contract", contract);
        signRequest.put("user", user);
        signRequest.put("personSealCarrier", "PERSON_SIGN");

        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/contract/signbyperson";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 获取个人认证链接2
     */
    public static void authurl2(String mobile) throws Exception {

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("mobile", mobile);
        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/userauth/authurl2";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 法人单位签署方下的个人静默签署
     */
    public static void signbyemployee(String contractId, String mobile1, String documentId, String keyword) throws Exception {

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("mobile", mobile1);

        Map<String, Object> contract = new HashMap<String, Object>();
        contract.put("id", Long.parseLong(contractId));

        Map<String, Object> company = new HashMap<String, Object>();
        company.put("name", companyName);
        company.put("registerNo", "91330000765209674T");
        // 签署位置
//        Map<String, Object> stamper = new HashMap<String, Object>();
//        stamper.put("documentId", documentId);
//        stamper.put("type", "SEAL_PERSONAL"); // 签章类型
//        stamper.put("page",1);
////        stamper.put("keyword", keyword); // 关键字；通过关键字定位签署位置
//        stamper.put("offsetX", 0); // 横坐标偏移；此处为：通过关键字定位签署位置后，再向右偏移0.1个坐标
//        stamper.put("offsetY", 0); // 横坐标偏移；此处为：通过关键字定位签署位置后，再向右偏移0.1个坐标

        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contract", contract);
        signRequest.put("user", user);
        signRequest.put("company", company);
//        signRequest.put("signatoryNo",mobile);
        signRequest.put("actionNo", IdUtil.randomUUID());

        signRequest.put("personSealCarrier", "PERSON_SIGN");

//        List<Map<String,Object>> mapList = new ArrayList<>();
//        mapList.add(stamper);
//        signRequest.put("stampers", mapList);
        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String signUrl = baseServiceUrl + "/v2/contract/signbyemployee";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 获取个人扫码签链接
     */
    public static void sweepcode(String contractId) throws Exception {
        HttpParamers httpParamers = new HttpParamers(com.qiyuesuo.sample.api.utils.http.HttpMethod.GET);
        httpParamers.addParam("contractId", contractId);
        String signUrl = baseServiceUrl + "/contract/sweepcode/url";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doGet(signUrl, httpParamers, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);

            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 查询角色列表
     */
    public static void rolelist() throws Exception {
        HttpParamers httpParamers = new HttpParamers(com.qiyuesuo.sample.api.utils.http.HttpMethod.GET);
        Map company = new HashMap();
        company.put("name", companyName);
        httpParamers.addParam("company", company.toString());
        String signUrl = baseServiceUrl + "/v2/role/list";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doGet(signUrl, httpParamers, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);

            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }

    /**
     * 获取个人扫码签链接
     */
    public static void verify(String documentId) throws Exception {
        HttpParamers httpParamers = new HttpParamers(com.qiyuesuo.sample.api.utils.http.HttpMethod.GET);
        httpParamers.addParam("documentId", documentId);
        String signUrl = baseServiceUrl + "/file/verify/documentId";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
            String response = HttpClient.doGet(signUrl, httpParamers, header, 15000, 60000);
            Map<String, Object> map = handleHttpResponse(response);

            logger.info(handleHttpResponse(response).toString());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("sign error", e);
            throw e;
        }
    }


    public static void main(String[] args) throws Exception {

        String documentId = createbyfile("https://safety-boots.oss-cn-beijing.aliyuncs.com/upload/物联网指令单（ZLD-监理办JL01-20230628-002）_1688979277254.pdf");
//        String documentId = "3115923981895475649";
//        createEmployee("温承星","15270292717");
//        createEmployee("高鑫玉","15957140410");
        Map<String, String> map = new HashMap<String, String>() {{
            put("operatorName", "高鑫玉");
            put("operatorContact", "15957140410");
        }};
        Map<String, String> map1 = new HashMap<String, String>() {{
            put("operatorName", "高鑫玉");
            put("operatorContact", "15957140410");
        }};
        Map<String, String> map3 = new HashMap<String, String>() {{
            put("operatorName", "高鑫玉");
            put("operatorContact", "15957140410");
        }};
        List<Map<String,String>> mapList = Arrays.asList(map,map1,map3);
        String contractId = createbycategory(documentId,mapList);
//        String contractId = "3115923983296373188";
//        signurl(contractId,"15270292717");
//        pageurl(contractId,"15270292717");
//        verify(documentId);
//        rolelist();
//        companysignsilent();
//        signbycompany(documentId,contractId,"总监理工程师");
//        download(contractId);
//        sweepcode(contractId);

//        createEmployee("温","15270292717");
//        createurl("15270292717");
//        personalsignsilent("15270292717");
//
//        signbyperson(contractId,"15957140410");
//        authurl2("15270292717");
//        signbyemployee(contractId,"15270292717",documentId,"专业监理工程师");
    }

    /**
     * 生成 Header 参数
     */
    private static HttpHeader generateHttpHeader(String appToken, String appSecret) {
        Long timestamp = System.currentTimeMillis();
        String nonce = UUID.randomUUID().toString();
        String signature = MD5.toMD5(appToken + appSecret + timestamp + nonce);
        return new HttpHeader(appToken, timestamp, signature, nonce);
    }

    /**
     * 解析返回参数
     */
    private static Map<String, Object> handleHttpResponse(String response) throws BizException {
        JSONObject jsonObject = new JSONObject(response);
        Map<String, Object> result = jsonObject.toMap();
        if (result == null || result.isEmpty()) {
            throw new BizException("远程服务返回的数据无法解析");
        }
        Integer code = (Integer) result.get("code");
        if (code == null || code != 0) {
            throw new BizException(code, (String) result.get("message"));
        }
        return result;
    }

}
