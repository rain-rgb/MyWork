package com.trtm.sy.registerfile.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.registerfile.mapper.SyFileMapper;
import com.trtm.sy.registerfile.model.entity.SyFile;
import com.trtm.sy.registerfile.service.SyFileService;
import com.trtm.sy.registerfile.tool.FileUploadUtils;
import com.trtm.sy.registerfile.tool.FileUtil;

import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class SyFileServiceImpl extends ServiceImpl<SyFileMapper, SyFile> implements SyFileService {

    @Autowired
    private SyFileMapper syFileMapper;
    @Autowired
    private HttpServletRequest request;
//    @Value("${jeecg.upload-file.pdf}")
//    private String pdf;


    /**
     * 附件、图片上传
     *
     * @param uploadFile 上传文件
     * @param form       文件来源
     * @param fileType   文件类型
     * @param path       文件保存路径
     * @param apptype
     * @param filename
     * @throws IOException
     */
    @Override
    @Transactional
    public Map<String, Object> uploadFile(MultipartFile uploadFile, String form, String fileType,
                                          String path, String apptype, String filename) throws IOException {

        // 获取前端传来的文件名称
//        String originalFilename = uploadFile.getOriginalFilename();
//        if(oConvertUtils.isNotEmpty(apptype)){
//            if(apptype.equals("app")){
//                originalFilename = filename;
//            }
//        }
//        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//        String[] imageExtension = new String[]{"HEIC", "heic", "HEIF", "heif"};
//        for (String item : imageExtension) {
//            if (extension.toLowerCase().equals(item)) {
//                throw new IOException("图片格式不正确！请重新上传");
//            }
//        }
//        // 上传文件
//        String filePath = FileUploadUtils.uploadFile(path, uploadFile);
//        // 保存后返回到前端的数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("originalFilename", originalFilename);
//        map.put("filePath", filePath);
//        // 将数据保存到 zhgd_file文件表中
//        SyFile file = new SyFile();
//        //通过 sys_file_type 文件类型 和 value 查出code 存到文件表里
//        //String dictcode= sysDictDataService.selectDictCode("sys_file_type",value,);
//        file.setFjbs(form);
//        file.setFjmc(originalFilename);
//        file.setFjlj(filePath);
//        file.setFjlx(fileType);
//        file.setDelFlag("N");
//        syFileMapper.insert(file);
////         返回入库后的id给前端，用来提交整个表单
//        map.put("id", file.getFjid());
//        if (oConvertUtils.isNotEmpty(file.getFjlj())) {
//            String fjlj = file.getFjlj();
//            String[] split = fjlj.split("uploadFile");
//            map.put("fjyllj",  split[1].replace("\\", "/"));
//        }
//        map.put("form", form);
//        map.put("fileType", fileType);
//        return map;
        return null;
    }

    @Override
    @Transactional
    public Map<String, Object> uploadFileByyply(MultipartFile uploadFile, String form, String fileType,
                                                String path, String fjfjdid) throws IOException {

//        // 获取前端传来的文件名称
//        String originalFilename = uploadFile.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//        String[] imageExtension = new String[]{"HEIC", "heic", "HEIF", "heif"};
//        for (String item : imageExtension) {
//            if (extension.toLowerCase().equals(item)) {
//                throw new IOException("图片格式不正确！请重新上传");
//            }
//        }
//        // 上传文件
//        String filePath = FileUploadUtils.uploadFile(path, uploadFile);
//        // 保存后返回到前端的数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("originalFilename", originalFilename);
//        map.put("filePath", filePath);
//        // 将数据保存到 zhgd_file文件表中
//        SyFile file = new SyFile();
//        //通过 sys_file_type 文件类型 和 value 查出code 存到文件表里
//        //String dictcode= sysDictDataService.selectDictCode("sys_file_type",value,);
//        file.setFjbs(form);
//        file.setFjmc(originalFilename);
//        file.setFjlj(filePath);
//        file.setFjlx(fileType);
//        file.setDelFlag("N");
//        file.setFjfjdid(fjfjdid);
//        syFileMapper.insert(file);
////         返回入库后的id给前端，用来提交整个表单
//        map.put("id", file.getFjid());
//        map.put("form", form);
//        map.put("fileType", fileType);
//        return map;
        return null;
    }

    /**
     * 下载附件
     *
     * @param id
     * @param response
     */
    @Override
    public void download(String id, HttpServletResponse response) throws Exception {
        // 获取需要下载的附件ID
        if (id != null) {
            SyFile file = syFileMapper.selectById(id);
            if (file != null) {
                FileUploadUtils.downloadFile(file.getFjlj(), file.getFjmc(), response);
            } else {
                throw new Exception("文件不存在");
            }
        } else {
            throw new Exception("文件不存在");
        }
        // 下载操作
    }


//    public String previewFileNew(String id, String fileUrl, HttpServletResponse res) {
//        String fileNames = null;
//        String filePath = null;
//        if (id != null) {
//            SyFile file = syFileMapper.selectById(id);
//            if (file != null) {
//                fileNames = file.getFjmc();
//                InputStream in = null;
//                OutputStream out = null;
//                filePath = fileHandler(fileNames, file.getFjlj(), fileUrl);
//                //判断是pdf还是word还是excel
//                //若是pdf直接读 否则转pdf 再读  //
//                try {
//                    if (filePath != null) {
//                        in = new FileInputStream(filePath);
//                    }
//                    String fileSuffix = FileUtil.getFileSuffix(filePath).toLowerCase();
//                    if ("pdf".equals(fileSuffix)) {
//                        res.setContentType("application/pdf");
//                    } else if ("jpg".equals(fileSuffix)) {
//                        res.setContentType("image/jpeg");
//                    } else if ("jpeg".equals(fileSuffix)) {
//                        res.setContentType("image/jpeg");
//                    } else if ("png".equals(fileSuffix)) {
//                        res.setContentType("image/png");
//                    } else if ("gif".equals(fileSuffix)) {
//                        res.setContentType("image/gif");
//                    }
//                    res.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileNames, "UTF-8"));
//                    res.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//                    res.setHeader("filename", URLEncoder.encode(fileNames, "UTF-8"));
//                    out = res.getOutputStream();
//                    byte[] b = new byte[1024];
//                    int len = 0;
//                    while ((len = in.read(b)) != -1) {
//                        out.write(b);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (in != null) {
//                            in.close();
//                        }
//                        if (out != null) {
//                            out.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return filePath;
//    }

//    private String fileHandler(String fileName, String fileUrl, String filePath) {
//        String fileSuffix = FileUtil.getFileSuffix(fileName).toLowerCase();
//        log.debug(fileSuffix);
//        if ("pdf".equals(fileSuffix) || "jpeg".equals(fileSuffix) || "jpg".equals(fileSuffix) || "png".equals(fileSuffix) || "jiff".equals(fileSuffix) || "gif".equals(fileSuffix) || "JPG".equals(fileSuffix) || "tif".equals(fileSuffix) || "JPEG".equals(fileSuffix)) {
//            return fileUrl;
//        } else if ("zip".equals(fileSuffix) || "rar".equals(fileSuffix) || "rar4".equals(fileSuffix)) {
//            return "压缩包格式不支持预览！";
//        }
//        String pdfUrl = fileUrl.substring(fileUrl.lastIndexOf("\\") + 1);
//        String pdfFile = filePath + "\\" + (pdfUrl.substring(0, pdfUrl.lastIndexOf("."))) + ".pdf";
//        File file = new File(pdfFile);
//        if (file.exists()) {
//            return pdfFile;
//        } else {
//            return Office2PDF.openOfficeToPDF(fileUrl, filePath).getAbsolutePath();
//        }
//    }

    @Override
    public Integer deletedById(String id) {
        return syFileMapper.deleteById(id);
    }

    @Override
    public SyFile getFileById(String id) {
        return this.getOne(Wrappers.lambdaQuery(new SyFile()).eq(SyFile::getFjid, id));
    }

}
