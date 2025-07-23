package com.trtm.sy.registerfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registerfile.model.entity.SyFile;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface SyFileService extends IService<SyFile> {

    // 上传文件
    Map<String, Object> uploadFile(MultipartFile uploadFile, String form, String fileType, String path, String apptype, String filename) throws IOException;

    Map<String, Object> uploadFileByyply(MultipartFile uploadFile, String form, String fileType, String path, String fjfjdid) throws IOException;

    void download(String id, HttpServletResponse response) throws Exception;

   // String previewFileNew(String id, String fileUrl, HttpServletResponse res);

    Integer deletedById(String id);

    SyFile getFileById(String id);
}
