package com.trtm.sy.registertable.controller;

import com.trtm.sy.registertable.model.RegisterTableRequest;
import com.trtm.sy.registertable.service.RegisterTableService;
import io.swagger.annotations.Api;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zxl
 * @date 2024/7/1
 * @time 17:59
 */
@RestController
@Api(tags = "报表上传配置")
@RequestMapping("/sy/registerTable")
public class RegisterTableController {

    @Resource
    private RegisterTableService registerTableService;

    @PostMapping("/upload/{tableNum}")
    public Result<?> handleFileUpload(@RequestParam("file") MultipartFile file, RegisterTableRequest tableRequest, @PathVariable String tableNum) {
        registerTableService.handleFileUpload(file, tableRequest, tableNum);
        return Result.OK();
    }

//    @PostMapping("/uploadWord")
//    public Result<?> uploadWord(@RequestParam("file") MultipartFile file) {
//        registerTableService.uploadWord(file);
//        return Result.OK();
//    }
//
//    @PostMapping("/spilt")
//    public Result<?> spiltFile(@RequestParam("file") MultipartFile file, RegisterTableRequest tableRequest) {
//        registerTableService.spiltFile(file, tableRequest);
//        return Result.OK();
//    }
//
//    @PostMapping("/updateTableFormPattern/{tableNum}")
//    public Result<?> updateTableFormPattern(@PathVariable String tableNum, @RequestBody List<RegisterTableRequest> requestList) {
//        registerTableService.updateTableFormPattern(tableNum, requestList);
//        return Result.OK();
//    }
//
//    @PostMapping("/saveFormList/{tableNum}")
//    public Result<?> saveFormList(@RequestBody Map<String, List<List<BaseForm>>> request, @PathVariable String tableNum) {
//        List<List<BaseForm>> lists = request.get("request");
//        registerTableService.saveFormList(lists, tableNum);
//        return Result.OK();
//    }

}
