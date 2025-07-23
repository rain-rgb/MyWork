package com.trtm.api;

import com.trtm.api.exception.BizException;
import com.trtm.api.utils.IOUtils;
import com.trtm.api.utils.crypt.MD5;
import com.trtm.api.utils.http.*;
import com.trtm.api.utils.json.JSONObject;
import com.trtm.api.utils.json.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 单方签署 API 调用示例。
 * 场景：学校（或公司）签署签署证明文件。
 * 步骤：
 * 1、传入要签署的文件，在契约锁中生成「签署文档」；
 * 2、传入签署相关信息，在契约锁中创建并发起「电子签约」；
 * 3、传入要签署的签署方信息以及签署位置等信息，签署「电子签约」；
 * 4、下载签署后的文档；
 */
public class SingleSignApiSample {
	private static final Logger logger = LoggerFactory.getLogger(SingleSignApiSample.class);

	public  void main1(String[] args) throws Exception {
        String baseServiceUrl = "http://open38.qiyuesuo.net"; // 替换为实际的【契约锁私有云开放平台】地址
        String appToken = "MuYXgK1gVC"; // 【契约锁私有云控制台】创建应用后生成的 AppToken
        String appSecret = "wJPkF7RnCBX2xsVT3Z5lw8PKkyG0as"; // 【契约锁私有云控制台】创建应用后生成的 AppSecret
        HttpHeader header = null;

        String categoryId = "3093090778151239927"; // 用印流程 ID，需要在[契约锁电子签署平台]-[印控]-[用印流程]中新增并启用用印流程。
        String sealId = "3093097633678213250"; // 印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
        String companyName = "众畅科技"; // 组织名称
        String mobile = "10000000001"; // 创建人手机号

        /**
         * 第一步：创建签署文档，调用接口【上传本地文件创建签署文档】，获取文档 ID。
         */
        HttpParamers createDocuemntParams = HttpParamers.httpPostParamers(); // post 接口
        createDocuemntParams.addParam("title", "电子成绩单");
        createDocuemntParams.addParam("fileType", "pdf");
        FileInputStream in = new FileInputStream("docs/NoSign.pdf");
        FileItem file = new StreamFile(in);
        createDocuemntParams.addFile("file", file);

        String documentId = null;
        String createDocumentUrl = baseServiceUrl + "/v2/document/createbyfile";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doService(createDocumentUrl, createDocuemntParams, header, 15000, 60000);
        	Map<String, Object> result = handleHttpResponse(response);
            // 如果调用接口成功，则文档 ID
            documentId = (String)((Map<String, Object>)result.get("result")).get("documentId");
            logger.info("创建「文档」成功，documentId:{}", documentId);
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("create file error", e);
        	throw e;
        }

        /**
         * 第二步：使用第一步生成的文档 ID 创建并发起电子签约，调用接口【创建或发起电子签约】，获取【电子签约】ID。
         */
        // 签署方下的签署节点
        Map<String, Object> action = new HashMap<String, Object>();
        action.put("type", "CORPORATE"); // 节点类型，CORPORATE代表签署公章
        action.put("name", "签署公章"); // 节点名称
        action.put("serialNo", 1); // 节点顺序，最小为1，表示第一个签署
        // 签署方
        Map<String, Object> signatory = new HashMap<String, Object>();
        signatory.put("tenantType", "COMPANY"); // COMPANY表示签署方类型是公司
        signatory.put("tenantName", companyName); // 签署方名称-公司名称
        signatory.put("serialNo", 1); // 签署顺序，最小为1，表示第一个签署
        List<Map> actions = new ArrayList<Map>();
        actions.add(action);
        signatory.put("actions", actions); // 签署节点，可以有多个，例如可以有「签署公章」「法人签署」「经办人签署」等

        // 【电子签约】的创建人
        Map<String, Object> creatorInfo = new HashMap<String, Object>();
        creatorInfo.put("mobile", mobile); // 创建人手机号

        Map<String, Object> createContractRequest = new HashMap<String, Object>();
        createContractRequest.put("subject", "电子成绩单"); // 电子签约名称
        createContractRequest.put("categoryId", categoryId); // 用印流程 ID
        createContractRequest.put("send", true); // 是否立即发起合同
        createContractRequest.put("tenantName", companyName); // 签署方名称
        createContractRequest.put("expireTime", "2023-05-09"); // 合同过期时间
        createContractRequest.put("creatorInfo", creatorInfo); // 创建人

        List<String> documents = new ArrayList<String>();
        documents.add(documentId);
        createContractRequest.put("documents", documents); // 签署文档

        List<Map<String, Object>> signatories = new ArrayList<Map<String, Object>>();
        signatories.add(signatory);
        createContractRequest.put("signatories", signatories); // 签署方

        String createContractParamsJson = JSONUtils.toJson(createContractRequest); // content-type 为 json，需要将参数转换为 json 字符串
        String contractId = null;
        String createContractUrl = baseServiceUrl + "/contract/createbycategory";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doServiceWithJson(createContractUrl, createContractParamsJson, header, 15000, 60000);
        	Map<String, Object> result = handleHttpResponse(response);
            // 如果调用成功，记录[电子签约]ID
            contractId = (String)result.get("contractId");
            logger.info("创建「电子签约」成功，contractId:{}", contractId);
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("create contract error", e);
        	throw e;
        }

        /**
         * 第三步：签署【电子签约】，调用接口【法人单位印章静默签署v2】
         */
        // 签署位置
        Map<String, Object> stamper = new HashMap<String, Object>();
        stamper.put("documentId", documentId);
        stamper.put("type", "SEAL_CORPORATE"); // 签章类型
        stamper.put("sealId", sealId); // 印章 ID
        stamper.put("keyword", "甲方签名"); // 关键字；通过关键字定位签署位置
        stamper.put("offsetX", -0.1); // 横坐标偏移；此处为：通过关键字定位签署位置后，再向左偏移0.1个坐标


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
        	outputStream = new FileOutputStream("docs/singleSignApiDoc.pdf");
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
