//package com.trtm.sy.registerfile.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.jeecg.common.api.vo.Result;
//import org.jeecg.common.system.base.controller.JeecgController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.trtm.sy.file.model.entity.SyFile;
//import org.trtm.sy.file.service.SyFileService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//
///**
// * @author zww
// * @since
// */
//@RestController
//@RequestMapping("/sy/syFile")
//@Api(tags = "文件管理")
//public class SyFileController extends JeecgController<SyFile, SyFileService> {
//
//    @Autowired
//    private SyFileService syFileService;
//    @Value("${jeecg.localUrl.url}")
//    private String localUrl;
//    @Value("${jeecg.uploadFile.basePath}")
//    private String basePath;
//
//    /**
//     * 附件上传
//     *
//     * @param uploadFile 附件
//     * @param form       存储标志
//     * @param fileType   附件类型
//     * @param filePath   附件存储基础地址
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "附件", name = "uploadFile", required = true, dataType = "file", paramType = "file"),
//            @ApiImplicitParam(value = "附件功能地址", name = "filePath", required = true, paramType = "query", dataType = "string"),
//            @ApiImplicitParam(value = "附件类型", name = "fileType", required = true, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(value = "附件标志", name = "form", required = true, paramType = "query", dataType = "string"),
//    })
//    @ApiOperation(value = "附件上传")
//    @PostMapping("/uploadFile")
//    public Result<?> uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile,
//                                String form, String fileType, String filePath,
//                                HttpServletRequest request) throws IOException {
//        return Result.OK(syFileService.uploadFile(uploadFile, form, fileType, basePath + "/" + filePath, null, null));
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "附件", name = "uploadFile", required = true, dataType = "file", paramType = "file"),
//            @ApiImplicitParam(value = "附件功能地址", name = "filePath", required = true, paramType = "query", dataType = "string"),
//            @ApiImplicitParam(value = "附件类型", name = "fileType", required = true, paramType = "query", dataType = "int"),
//            @ApiImplicitParam(value = "附件标志", name = "form", required = true, paramType = "query", dataType = "string"),
//    })
//    @ApiOperation(value = "样品领用附件上传")
//    @PostMapping("/uploadFileByyply")
//    public Result<?> uploadFile(
//            HttpServletRequest request) throws IOException {
//        String form = "yply";
//        String fileType = "11";
//        String filePath = "yply";
//        String fjfjdid = request.getParameter("fjfjdid");
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartFile uploadFile = multipartRequest.getFile("file");// 获取上传文件对象
//        Map<String, Object> objectMap = syFileService.uploadFileByyply(uploadFile, form, fileType, basePath + "/" + filePath, fjfjdid);
//        String filePathBack = objectMap.get("filePath").toString().replace("\\", "/");
//        String[] split = filePathBack.split(":");
//        filePathBack = "http://" + localUrl + "/jeecg-boot/signimg" + split[1];
//        objectMap.put("filePath", filePathBack);
//        return Result.OK(objectMap);
//    }
//
//    /**
//     * 下载
//     *
//     * @param id
//     * @param response
//     * @throws Exception
//     */
//    @GetMapping("download")
//    @ApiOperation("附件下载")
//    @ApiImplicitParam(value = "附件id", name = "id", required = true, paramType = "query", dataType = "int")
//    public void download(String id,
//                         HttpServletResponse response) throws Exception {
//        syFileService.download(id, response);
//    }
//
//    /**
//     * 附件查询
//     *
//     * @param id
//     */
//    @GetMapping("getFileById")
//    @ApiOperation("附件查询")
//    @ApiImplicitParam(value = "附件id", name = "id", required = true, paramType = "query", dataType = "int")
//    public Result<?> getFileById(String id) {
//        return Result.OK(syFileService.getFileById(id));
//    }
//
//
//}
