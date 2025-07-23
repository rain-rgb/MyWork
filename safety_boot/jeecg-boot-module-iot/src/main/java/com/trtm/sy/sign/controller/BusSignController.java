package com.trtm.sy.sign.controller;

import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.response.SdkResponse;
import com.trtm.sy.sign.model.entity.BusSign;
import com.trtm.sy.sign.service.BusSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author wcx
 */
@RestController
@Api(tags = "电子签章")
@RequestMapping("bus/sign/")
public class BusSignController extends JeecgController<BusSign, BusSignService> {
    @Resource
    private BusSignService busSignService;



    /**
     * 公有云接口
     */
    /**
     * 创建合同草稿
     */
    @ApiOperation("创建合同草稿")
    @GetMapping("draft")
    public Result<?> getdraft( String name,Map<String, String> personList) throws Exception {
        busSignService.getdraft(name, personList);
        return Result.OK();
    }

    /**
     * 用文件添加合同文档
     */
    @ApiOperation("用文件添加合同文档")
    @GetMapping("addbyfile")
    public Result<?> getaddbyfile(String pdfurl, String name, Long contractId) throws Exception {
        busSignService.getaddbyfile(pdfurl, name, contractId);
        return Result.OK();
    }

    /**
     * 发起合同
     */
    @ApiOperation("发起合同")
    @GetMapping("send")
    public Result<?> getsend(SdkResponse<Contract> sdkResponse, Long documentId, Integer spbnum,Integer bgnum,Integer jlnum) {
        busSignService.getsend(sdkResponse, documentId, spbnum,bgnum,jlnum);
        return Result.OK();
    }

    /**
     * 签署个人签名
     */
    @ApiOperation("签署个人签名")
    @GetMapping("personalsign")
    public Result<?> getpersonalsign(Long contractId, String mobile) {
        busSignService.getpersonalsign(contractId, mobile);
        return Result.OK();
    }

    /**
     * 个人签名授权页面
     */
    @ApiOperation("个人签名授权页面")
    @GetMapping("authurl")
    public Result<?> getauthurl(String mobile) {
        busSignService.getauthurl(mobile);
        return Result.OK();
    }

    /**
     * 审批
     */
    @ApiOperation("审批")
    @GetMapping("employeeaudit")
    public Result<?> getemployeeaudit(Long contractId) {
        busSignService.getemployeeaudit(contractId);
        return Result.OK();
    }

    /**
     * 签署公章
     */
    @ApiOperation("签署公章")
    @GetMapping("companysign")
    public Result<?> getcompanysign(Long contractId, Long sealId) {
        busSignService.getcompanysign(contractId, sealId);
        return Result.OK();
    }

    /**
     * 下载合同文档
     */
    @ApiOperation("下载合同文档")
    @GetMapping("download")
    public Result<?> getdownload2(Long documentId, String url, String pdfname) throws FileNotFoundException {
        busSignService.getdownload2(documentId, url, pdfname);

        return Result.OK();
    }

    /**
     * 获取文件下载链接
     */
    @ApiOperation("获取文件下载链接")
    @GetMapping("downloadurl")
    public Result<?> getdownloadurl(Long contractId) throws Exception {
        busSignService.getdownloadurl(contractId);
        return Result.OK();
    }

    /**
     * 撤回/作废合同
     */
    @ApiOperation("撤回/作废合同")
    @GetMapping("invalid")
    public Result<?> getinvalid(Long contractId, Integer status) throws Exception {
        busSignService.getinvalid(contractId, status);
        return Result.OK();
    }

    /**
     * 获取个人认证链接
     */
    @ApiOperation("获取个人认证链接 ")
    @GetMapping("personalauth")
    public Result<?> getpersonalauth(String mobile) throws Exception {
        busSignService.getpersonalauth(mobile);
        return Result.OK();
    }
    /**
     * 签章测试
     */
    @ApiOperation("签章测试")
    @GetMapping("ceshi")
    public Result<?> ceshi() throws Exception {
        busSignService.ceshi();
        return Result.OK();
    }

    /**
     * 浏览页面
     */
    @ApiOperation("浏览页面 ")
    @GetMapping("viewurl")
    public Result<?> getviewurl(Long contractid,Long documentid)  {

        return Result.OK(busSignService.getviewurl(contractid,documentid));
    }

    /**
     * 获取短链接
     */
    @ApiOperation("获取短链接 ")
    @GetMapping("shorturl")
    public Result<?> shorturl(Long contractId)  {
        return Result.OK(busSignService.shorturl(contractId));
    }

    /**
     * 查询个人静默签授权记录
     */
    @ApiOperation("查询个人静默签授权记录 ")
    @GetMapping("query")
    public Result<?> queryJl(String phone)  {
        return Result.OK(busSignService.queryJL(phone));
    }

}
