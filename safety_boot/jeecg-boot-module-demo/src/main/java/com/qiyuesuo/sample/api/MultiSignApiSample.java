package com.qiyuesuo.sample.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
 * 多方签署 API 调用示例
 * 场景：双方签署销售合同，发起方业务人员先签字，印章管理员再盖章，接收方业务员签字和印章管理员盖章不分先后；
 * 发起方从ERP系统发起，本地上传签署文件，领导审批之后，调用契约锁在线签署功能签署后，接收方再签署；
 * 步骤：
 * 1、传入要签署的文件，在契约锁中生成「签署文档」；
 * 2、传入签署相关信息，在契约锁中创建并发起「电子签约」；
 * 3、发起方审批；
 * 4、发起方签署公章；
 * 5、接收方经办人签字，接收方签署公章；
 * 6、下载合同；
 */
public class MultiSignApiSample {
	private static final Logger logger = LoggerFactory.getLogger(MultiSignApiSample.class);

    String baseServiceUrl = "http://10.10.20.49:9182"; // 替换为实际的【契约锁私有云开放平台】地址
    String appToken = "iLHntGbbBy"; // 【契约锁私有云控制台】创建应用后生成的 AppToken
    String appSecret = "svLoRghPZVkeayQG4pajNMSPsS14FN"; // 【契约锁私有云控制台】创建应用后生成的 AppSecret
    
    String categoryId = "3111202059992601068"; // 用印流程 ID，需要在[契约锁电子签署平台]-[印控]-[用印流程]中新增并启用用印流程。
    String sealId = "3111204341618479789"; // 印章 ID，需要在[契约锁电子签署平台]-[印控]-[电子印章]中制作印章，并进行「自动盖章授权」。
    String companyName = "浙江省交通投资集团有限公司"; // 组织名称
    String createrMobile = "15957140410"; // 创建人手机号
    String createrName = "张豪"; // 创建人姓名
    String leaderMobile = "15957140410"; // 领导手机号
    String receiverCompanyName = "浙江临金高速公路有限公司"; // 接收方公司
    String receiverOperatorMobile = "15957140410"; // 接收方经办人手机号
    String receiverOperatorName = "李四"; // 接收方经办人姓名
    
	public static void main(String[] args) throws Exception {
		MultiSignApiSample sample = new MultiSignApiSample();
        /**
         * 第一步：创建签署文档，调用接口【上传本地文件创建签署文档】
         */
        String documentId = sample.createDocument();
        /**
         * 第二步：创建并发起电子签约，调用接口【创建或发起电子签约】
         */
        String contractId = sample.createContract(documentId);
        /**
         * 第三步：领导审批；调用接口【文件页面】，返回文件页面链接，打开文件页面进行审批；
         * 其他方式：直接调用接口审批（参考下一步）；
         */
        sample.pageUrl(contractId);
        /**
         * 第三步：直接调用接口审批，调用接口【文件审批】；
         */
        sample.audit(contractId);
        /**
         * 第四步：发起方签署公章，调用接口【法人单位印章静默签署v2】
         */
        sample.companySign(contractId);
        /**
         * 第五步：接收方收到短信后，打开签署页面进行 经办人签字 和 公章签署（需要人工操作，此处省略）。
         * 其他方式：和第三步类似，调用【文件页面】接口获取签署链接，接收方打开签署链接进行签署.
         */
        
        /**
         * 第六步：接收方签署完成后，下载合同，调用接口【下载签署任务文档】
         */
        sample.download(documentId);
	}
	
	/**
	 * 创建签署文档，调用接口【上传本地文件创建签署文档】
	 */
	private String createDocument() throws Exception {
        HttpParamers createDocuemntParams = HttpParamers.httpPostParamers(); // post 接口
        createDocuemntParams.addParam("title", "电子成绩单");
        createDocuemntParams.addParam("fileType", "pdf");
        FileInputStream in = new FileInputStream("jeecg-boot-module-demo/src/main/java/com/qiyuesuo/sample/api/NoSign.pdf");
        FileItem file = new StreamFile(in);
        createDocuemntParams.addFile("file", file);
        
        String createDocumentUrl = baseServiceUrl + "/v2/document/createbyfile";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doService(createDocumentUrl, createDocuemntParams, header, 15000, 60000);
        	Map<String, Object> result = handleHttpResponse(response);
            // 如果调用接口成功，则文档 ID
            String documentId = (String)((Map<String, Object>)result.get("result")).get("documentId");
            logger.info("创建「文档」成功，documentId:{}", documentId);
            return documentId;
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("create file error", e);
        	throw e;
        }
	}
	
	/**
	 * 创建并发起「电子签约」，调用接口【创建或发起电子签约】
	 */
	private String createContract(String documentId) throws Exception {
		// 签署方一下的签署节点：业务人员签字
        Map<String, Object> auditAction = new HashMap<String, Object>();
        auditAction.put("type", "AUDIT"); // 签署节点类型：经办人签字
        auditAction.put("name", "监理确认签署"); // 签署节点名称
        auditAction.put("serialNo", 1); // 签署节点顺序：1表示第一个签署（审批）
        // 设置审批人
        Map<String, Object> auditOperator = new HashMap<String, Object>();
        auditOperator.put("operatorContact", leaderMobile);
        List<Map<String, Object>> auditOperators = new ArrayList<Map<String, Object>>();
        auditAction.put("actionOperators", auditOperators);
        
        // 签署方一下的签署节点：公章签署
        Map<String, Object> sealSignAction = new HashMap<String, Object>();
        sealSignAction.put("type", "CORPORATE"); // 签署节点类型：签署公章
        sealSignAction.put("name", "签署公章"); // 签署节点名称
        sealSignAction.put("serialNo", 2); // 签署节点顺序：2表示第二个签署
        sealSignAction.put("sealId", sealId); // 使用的印章
        // 签署方一下公章签署的签署位置
        Map<String, Object> sealLocation = new HashMap<String, Object>();
        sealLocation.put("rectType", "SEAL_CORPORATE"); // 「签署位置」类型：公司公章
        sealLocation.put("keyword", "甲方签名"); // 关键字，用于定位签署位置
        sealLocation.put("documentId", documentId);
        List<Map<String, Object>> sealLocations = new ArrayList<Map<String, Object>>();
        sealLocations.add(sealLocation);
        sealSignAction.put("locations", sealLocations);
        // 签署方一（发起方）
        Map<String, Object> sender = new HashMap<String, Object>();
        sender.put("tenantType", "COMPANY"); // 签署方类型：公司
        sender.put("tenantName", companyName); // 签署方名称
        sender.put("serialNo", "1"); // 签署方顺序：1表示第一个签署
        sender.put("contact", createrMobile); // 经办人联系方式
        sender.put("receiverName", createrName); // 经办人姓名
        List<Map<String, Object>> senderActions = new ArrayList<Map<String, Object>>();
        senderActions.add(auditAction);
        senderActions.add(sealSignAction);
        sender.put("actions", senderActions);

        // 签署方二的签署节点：业务人员签字
        Map<String, Object> receiverOperatorAction = new HashMap<String, Object>();
        receiverOperatorAction.put("type", "OPERATOR"); // 签署节点类型：经办人签字
        receiverOperatorAction.put("name", "业务人员签字"); // 签署节点名称
        receiverOperatorAction.put("serialNo", 1); // 签署节点顺序：1表示第一个签署
        // 签署方二下业务人员签字的签署位置
        Map<String, Object> receiverOperatorLocation = new HashMap<String, Object>();
        receiverOperatorLocation.put("rectType", "SEAL_PERSONAL"); // 「签署位置」类型：个人签名
        receiverOperatorLocation.put("keyword", "乙方签名"); // 关键字，用于定位签署位置
        receiverOperatorLocation.put("offsetY", 0.2); // 纵坐标偏移：此处为向上偏移0.2个坐标，即在关键字「乙方签名」上方；
        receiverOperatorLocation.put("documentId", documentId);
        List<Map<String, Object>> receiverOperatorLocations = new ArrayList<Map<String, Object>>();
        receiverOperatorLocations.add(receiverOperatorLocation);
        receiverOperatorAction.put("locations", receiverOperatorLocations);
        // 签署方二的签署节点：公章签署
        Map<String, Object> receiverSealAction = new HashMap<String, Object>();
        receiverSealAction.put("type", "CORPORATE"); // 签署节点类型：签署公章
        receiverSealAction.put("name", "签署公章"); // 签署节点名称
        receiverSealAction.put("serialNo", 1); // 签署节点顺序：1表示第一个签署；上面「业务人员签字」的签署顺序也是1，表示2个节点同时签署
        // 签署方二下公章签署的签署位置
        Map<String, Object> receiverSealLocation = new HashMap<String, Object>();
        receiverSealLocation.put("rectType", "SEAL_CORPORATE"); // 「签署位置」类型：公司公章
        receiverSealLocation.put("keyword", "乙方签名"); // 关键字，用于定位签署位置
        receiverSealLocation.put("documentId", documentId);
        List<Map<String, Object>> receiverSealLocations = new ArrayList<Map<String, Object>>();
        receiverSealLocations.add(receiverSealLocation);
        receiverSealAction.put("locations", receiverSealLocations);
        // 签署方二（接收方）
        Map<String, Object> receiver = new HashMap<String, Object>();
        receiver.put("tenantType", "COMPANY"); // 签署方类型：公司
        receiver.put("tenantName", receiverCompanyName); // 签署方名称
        receiver.put("serialNo", 2); // 签署方顺序：2表示第二个签署，需要第一个签署方签署完成后，才能签署
        receiver.put("contact", receiverOperatorMobile); // 经办人联系方式
        receiver.put("receiverName", receiverOperatorName); // 经办人姓名
        List<Map<String, Object>> receiverActions = new ArrayList<Map<String, Object>>();
        receiverActions.add(receiverOperatorAction);
        receiverActions.add(receiverSealAction);
        receiver.put("actions", receiverActions);
        
        // 【电子签约】的创建人
        Map<String, Object> creatorInfo = new HashMap<String, Object>();
        creatorInfo.put("mobile", createrMobile); // 创建人手机号
        
        Map<String, Object> createContractRequest = new HashMap<String, Object>();
        createContractRequest.put("subject", "销售合同"); // 电子签约名称
        createContractRequest.put("categoryId", categoryId); // 用印流程 ID
        createContractRequest.put("send", true); // 是否立即发起合同
        createContractRequest.put("tenantName", companyName); // 签署方名称
        createContractRequest.put("expireTime", "2023-05-12"); // 合同过期时间
        createContractRequest.put("creatorInfo", creatorInfo); // 创建人
        
        List<Long> documents = new ArrayList<Long>();
        documents.add(Long.valueOf(documentId));
        createContractRequest.put("documents", documents); // 签署文档
        
        List<Map<String, Object>> signatories = new ArrayList<Map<String, Object>>();
        signatories.add(sender);
        signatories.add(receiver);
        createContractRequest.put("signatories", signatories); // 签署方

        String createContractUrl = baseServiceUrl + "/contract/createbycategory";
        String createContractParamsJson = JSONUtils.toJson(createContractRequest); // content-type 为 json，需要将参数转换为 json 字符串
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doServiceWithJson(createContractUrl, createContractParamsJson, header, 15000, 60000);
        	Map<String, Object> result = handleHttpResponse(response);
            // 如果调用成功，记录[电子签约]ID
        	String contractId = (String)result.get("contractId");
            logger.info("创建「电子签约」成功，contractId:{}", contractId);
            return contractId;
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("create contract error", e);
        	throw e;
        }
	}

	/**
	 * 调用接口【文件页面】，返回文件页面链接
	 */
	private String pageUrl(String contractId) throws Exception {
		// 要审批的用户
        Map<String, Object> leader = new HashMap<String, Object>();
        leader.put("mobile", leaderMobile);
        // 要签署的「电子签约」
        Map<String, Object> contract = new HashMap<String, Object>();
        contract.put("id", contractId);
        
        Map<String, Object> pageRequest = new HashMap<String, Object>();
        pageRequest.put("contract", contract); // 要审批的合同
        pageRequest.put("operator", leader); // 要审批的用户

        String pageRequestUrl = baseServiceUrl + "/v2/contract/pageurl";
        String pageRequestParamsJson = JSONUtils.toJson(pageRequest); // content-type 为 json，需要将参数转换为 json 字符串
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doServiceWithJson(pageRequestUrl, pageRequestParamsJson, header, 15000, 60000);
        	Map<String, Object> result = handleHttpResponse(response);
            // 如果调用成功，记录[电子签约]ID
            String pageUrl = (String)((Map<String, Object>)result.get("result")).get("url");
            logger.info("文件页面链接： " + pageUrl);
            return pageUrl;
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("pageUrl error", e);
        	throw e;
        }
	}

	/**
	 * 文件审批，调用接口【文件审批】
	 */
	private void audit(String contractId) throws Exception {
		Map<String, Object> auditRequest = new HashMap<String, Object>();
        auditRequest.put("contractId", contractId); // 「电子签约」ID
        auditRequest.put("tenantName", companyName); // 签署方名称

        String auditUrl = baseServiceUrl + "/contract/audit";
        String auditParamsJson = JSONUtils.toJson(auditRequest); // content-type 为 json，需要将参数转换为 json 字符串
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doServiceWithJson(auditUrl, auditParamsJson, header, 15000, 60000);
        	handleHttpResponse(response);
            logger.info("发起方审批完成");
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("audit error", e);
        	throw e;
        }
	}

	/**
	 * 公司公章签署，调用接口【法人单位印章静默签署v2】
	 */
	private void companySign(String contractId) throws Exception {
		Map<String, Object> company = new HashMap<String, Object>();
        company.put("name", companyName); // 签署方名称
        
        Map<String, Object> signContract = new HashMap<String, Object>();
        signContract.put("id", contractId); // 电子签约 ID
        
        Map<String, Object> signRequest = new HashMap<String, Object>();
        signRequest.put("contract", signContract); // 需要签署的[电子签约]
        signRequest.put("company", company); // 需要签署的[签署方]
        
        String signUrl = baseServiceUrl + "/v2/contract/signbycompany";
        String signParamsJson = JSONUtils.toJson(signRequest); // content-type 为 json，需要将参数转换为 json 字符串
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        try {
        	String response = HttpClient.doServiceWithJson(signUrl, signParamsJson, header, 15000, 60000);
        	handleHttpResponse(response);
            logger.info("发起方签署成功");
        } catch (BizException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("sign error", e);
        	throw e;
        }
	}

	/**
	 * 文件下载，调用接口【下载签署任务文档】
	 */
	private void download(String documentId) throws Exception {
		HttpParamers downloadParams = HttpParamers.httpGetParamers(); // 接口方法为 get
        downloadParams.addParam("documentId", documentId);

        String downloadDocumentUrl = baseServiceUrl + "/document/download";
        // 设置 header 参数：每调用一次接口，都需要重新生成
        HttpHeader header = generateHttpHeader(appToken, appSecret);
        OutputStream outputStream = null;
        try {
        	outputStream = new FileOutputStream("docs/multiSignApiDoc.pdf");
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
	private HttpHeader generateHttpHeader(String appToken, String appSecret) {
        Long timestamp = System.currentTimeMillis();
        String nonce = UUID.randomUUID().toString();
        String signature = MD5.toMD5(appToken + appSecret + timestamp + nonce);
        return new HttpHeader(appToken, timestamp, signature, nonce);
	}

	/**
	 * 解析返回参数，出错后会抛出异常
	 */
	private Map<String, Object> handleHttpResponse(String response) throws BizException {
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
