package com.trtm.sy.registerfile.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registerfile.model.entity.SyFileDirectory;
import com.trtm.sy.registerfile.model.entity.SyFileDm;
import com.trtm.sy.registerfile.request.SyFileDmRequest;
import com.trtm.sy.registermodules.core.page.PageResult;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface SyFileDirectoryService extends IService<SyFileDirectory> {

    /**
     *  上传文件
     */
    void uploadFile(MultipartFile[] uploadFiles, String wjmlid, String path) throws IOException;

    /**
     *  上传文件
     */
    void uploadFileGc(SyFileDmRequest request);

    /**
     * 添加文件目录
     */
    void addSyFileDirectory(SyFileDirectory syFileDirectory, String fileManagerPath);

    /**
     * 删除文件目录
     */
    void deleteSyFileDirectory(List<String> param);

    /**
     * 修改文件目录
     */
    void editSyFileDirectory(SyFileDirectory syFileDirectory);

    /**
     * 获取文件目录
     */
    List<SyFileDirectory> getSyFileDirectory(SyFileDirectory syFileDirectory);

    /**
     * 获取文件目录类型
     */
    List<SyFileDirectory> getSyFileDirectoryByClass(SyFileDirectory request);

    /**
     * 获取文件目录下的文件
     */
    PageResult<SyFileDm> getSyFileDirectoryDm(SyFileDmRequest request);

    /**
     * 修改文件目录下的文件详情信息
     */
    void editSyFileByFjId(SyFileDmRequest request);

    /**
     * 删除文件目录下的文件详情信息
     */
    void deleteSyFileDmByFjId(SyFileDmRequest request);





}
