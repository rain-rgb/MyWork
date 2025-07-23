package com.trtm.sy.registerfile.controller;


import com.trtm.sy.registerfile.model.entity.SyFileDirectory;
import com.trtm.sy.registerfile.request.SyFileDmRequest;
import com.trtm.sy.registerfile.service.SyFileDirectoryService;
import com.trtm.sy.registerfile.service.SyFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件目录管理控制层
 * @author lp
 */
@RestController
@RequestMapping("/sy/syFileDirectory")
@Api(tags = "文件目录管理")
public class SyFileDirectoryController extends JeecgController<SyFileDirectory, SyFileDirectoryService> {

    /**
     * 文件管理路径
     */
    @Value("${jeecg.uploadFile.fileManagerPath}")
    private String fileManagerPath;
    /**
     * 文件目录
     */
    @Value("${jeecg.uploadFile.wjml}")
    private String wjml;

    @Resource
    private SyFileService syFileService;

    @Resource
    private SyFileDirectoryService syFileDirectoryService;

//    /**
//     * 添加文件目录
//     */
////    @SyinessLog(title = "添加文件目录", opType = OpTypeEnum.ADD)
//    @ApiOperation("添加文件目录")
//    @PostMapping("addSyFileDirectory")
//    public Result<?> addSyFileDirectory(@RequestBody SyFileDirectory syFileDirectory) {
//        syFileDirectoryService.addSyFileDirectory(syFileDirectory, fileManagerPath);
//        return Result.OK();
//    }
//
//    /**
//     * 删除文件目录
//     */
////    @SyinessLog(title = "删除文件目录", opType = OpTypeEnum.DELETE)
//    @ApiOperation("删除文件目录")
//    @PostMapping("deleteSyFileDirectory")
//    public Result<?> deleteSyFileDirectory(@RequestBody List<String> param) {
//        syFileDirectoryService.deleteSyFileDirectory(param);
//        return Result.OK();
//    }
//
//    /**
//     * 修改文件目录
//     */
////    @SyinessLog(title = "修改文件目录", opType = OpTypeEnum.EDIT)
//    @ApiOperation("修改文件目录")
//    @PostMapping("editSyFileDirectory")
//    public Result<?> editSyFileDirectory(@RequestBody SyFileDirectory syFileDirectory) {
//        syFileDirectoryService.editSyFileDirectory(syFileDirectory);
//        return Result.OK();
//    }
//
//    /**
//     * 获取文件目录
//     */
//    @ApiOperation("获取文件目录")
//    @PostMapping("getSyFileDirectory")
//    public Result<?> getSyFileDirectory(@RequestBody SyFileDirectory syFileDirectory) {
//        return Result.OK(syFileDirectoryService.getSyFileDirectory(syFileDirectory));
//    }
//
//    /**
//     * 获取文件目录类型
//     */
//    @ApiOperation("获取文件目录类型")
//    @PostMapping("getSyFileDirectoryByClass")
//    public Result<?> getSyFileDirectoryByClass(@RequestBody SyFileDirectory request) {
//        return Result.OK(syFileDirectoryService.getSyFileDirectoryByClass(request));
//    }
//
//    /**
//     * 上传规程文件
//     */
//    @ApiOperation(value = "上传规程文件", notes = "返回文件入库后的的id")
//    @PostMapping("uploadFile")
//    public Result<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile[] uploadFiles,
//                                                        String wjmlid,
//                                                        HttpServletRequest request) throws IOException {
//        syFileDirectoryService.uploadFile(uploadFiles, wjmlid, fileManagerPath);
//        return Result.OK();
//    }
//
//    /**
//     * 新增规程文件
//     */
//    @ApiOperation(value = "新增规程文件", notes = "返回文件入库后的的id")
//    @PostMapping("uploadFileGc")
//    public Result<Map<String, Object>> uploadFileGc(@RequestBody SyFileDmRequest request) {
//        syFileDirectoryService.uploadFileGc(request);
//        return Result.OK();
//    }
//
//    /**
//     * 下载
//     */
//    @NoAuth
//    @ApiOperation(value = "下载")
//    @GetMapping("download/{id}")
//    public void download(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
//        syFileService.download(id, response);
//    }
//

    /**
     * 获取文件目录下的文件
     */
    @ApiOperation("获取文件目录下的文件")
    @PostMapping("getSyFileDirectoryDm")
    public Result<?> getSyFileDirectoryDm(@RequestBody SyFileDmRequest request) {
        return Result.OK(syFileDirectoryService.getSyFileDirectoryDm(request));
    }


//    /**
//     * 修改文件目录下的文件详情信息
//     */
//    @ApiOperation("修改文件目录下的文件详情信息")
//    @PostMapping("editSyFileByFjId")
//    public Result<?> editSyFileByFjId(@RequestBody SyFileDmRequest request) {
//        syFileDirectoryService.editSyFileByFjId(request);
//        return Result.OK();
//    }
//
//    /**
//     * 删除文件目录下的文件详情信息
//     */
//    @ApiOperation("删除文件目录下的文件详情信息")
//    @PostMapping("deleteSyFileDmByFjId")
//    public Result<?> deleteSyFileDmByFjId(@RequestBody SyFileDmRequest request) {
//        syFileDirectoryService.deleteSyFileDmByFjId(request);
//        return Result.OK();
//    }
//
//    /**
//     * 上传文件
//     */
//    @ApiOperation(value = "上传文件", notes = "返回文件入库后的的id")
//    @PostMapping("uploadFileWjml")
//    public Result<Map<String, Object>> uploadFileWjml(@RequestParam("file") MultipartFile uploadFile,
//                                                            @RequestParam String form, @RequestParam String fileType, String apptype,String filename,
//                                                            HttpServletRequest request) throws IOException {
//        return Result.OK(syFileService.uploadFile(uploadFile, form, fileType, wjml,apptype,filename));
//    }
//
//
//    /**
//     * 附件预览
//     */
//    @ApiOperation("附件预览")
//    @GetMapping("previewFile/{id}")
//    public Result<?> previewFile(@PathVariable("id") String id, HttpServletResponse response) {
//        String a = syFileService.previewFileNew(id, wjml, response);
//        return Result.OK(a);
//    }



}
