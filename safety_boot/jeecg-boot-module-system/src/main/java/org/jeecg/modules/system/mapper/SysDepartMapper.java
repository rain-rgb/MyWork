package org.jeecg.modules.system.mapper;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.aiwarnmsg.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author: Steve
 * @Since： 2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     */
    public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

    @Select("select id from sys_depart where org_code=#{orgCode}")
    public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    @Select("select id,parent_id from sys_depart where id=#{departId}")
    public SysDepart getParentDepartId(@Param("departId") String departId);

    /**
     * 根据部门Id查询,当前和下级所有部门IDS
     *
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDepId(@Param("departId") String departId);

    /**
     * 根据部门编码获取部门下所有IDS
     *
     * @param orgCodes
     * @return
     */
    List<String> getSubDepIdsByOrgCodes(@org.apache.ibatis.annotations.Param("orgCodes") String[] orgCodes);

    List<SysDepart> queryTreeListByPid(@Param("parentId") String parentId);

    /**
     * 根据id下级部门数量
     *
     * @param parentId
     * @return
     */
    @Select("SELECT count(*) FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
    Integer queryCountByPid(@Param("parentId") String parentId);

    /**
     * 根据OrgCod查询所属公司信息
     *
     * @param orgCode
     * @return
     */
    SysDepart queryCompByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 根据id下级部门
     *
     * @param parentId
     * @return
     */
    @Select("SELECT * FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
    List<SysDepart> queryDeptByPid(@Param("parentId") String parentId);

    SysDepart selectByCode(String sysOrgCode);

    // 分组查询所以项目级别的项目信息
    @Select("SELECT * FROM sys_depart where org_category=4")
    List<SysDepart> selectBySBNo();

    List<String> fingAllSheBeiNo(String orgCode);

    SysDepart getSysDepart(String sysOrgCode);

    /**
     * <p>
     * 用户表 前端控制器
     * </p>
     *
     * @Author scott
     * @since 2018-12-20
     */
    @Slf4j
    @RestController
    @RequestMapping("/sys/common")
    class CommonController {

        @Autowired
        private ISysBaseAPI sysBaseAPI;

        @Value(value = "${jeecg.path.upload}")
        private String uploadpath;

        /**
         * 本地：local minio：minio 阿里：alioss
         */
        @Value(value = "${jeecg.uploadType}")
        private String uploadType;

        /**
         * @return
         * @Author 政辉
         */
        @GetMapping("/403")
        public Result<?> noauth() {
            return Result.error("没有权限，请联系管理员授权");
        }

        /**
         * 文件上传统一方法
         *
         * @param request
         * @param response
         * @return
         */
        @PostMapping(value = "/upload")
        public Result<?> upload(HttpServletRequest request, HttpServletResponse response) {
            Result<?> result = new Result<>();
            String savePath = "";
            String bizPath = request.getParameter("biz");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
            if (oConvertUtils.isEmpty(bizPath)) {
                if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                    //未指定目录，则用阿里云默认目录 upload
                    bizPath = "upload";
                    //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                    //result.setSuccess(false);
                    //return result;
                } else {
                    bizPath = "";
                }
            }
            if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                savePath = this.uploadLocal(file, bizPath);
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                 //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                 String jeditor = request.getParameter("jeditor");
                 if(oConvertUtils.isNotEmpty(jeditor)){
                 result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                 result.setSuccess(true);
                 return result;
                 }else{
                 savePath = this.uploadLocal(file,bizPath);
                 }
                 */
            } else {
                //update-begin-author:taoyan date:20200814 for:文件上传改造
                savePath = CommonUtils.upload(file, bizPath, uploadType);
                //update-end-author:taoyan date:20200814 for:文件上传改造
            }
            if (oConvertUtils.isNotEmpty(savePath)) {
                result.setMessage(savePath);
                result.setSuccess(true);
            } else {
                result.setMessage("上传失败！");
                result.setSuccess(false);
            }
            return result;
        }


        /**
         * 文件上传统一方法 外加 安全帽
         *
         * @param request
         * @param response
         * @return
         */
        @PostMapping(value = "/uploadLock")
        public Result<?> uploadLock(HttpServletRequest request, HttpServletResponse response) {
            Result<?> result = new Result<>();
            String savePath = "";
            String bizPath = request.getParameter("biz");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
            if (oConvertUtils.isEmpty(bizPath)) {
                if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                    //未指定目录，则用阿里云默认目录 upload
                    bizPath = "upload";
                    //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                    //result.setSuccess(false);
                    //return result;
                } else {
                    bizPath = "";
                }
            }
            if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                savePath = this.uploadLocal(file, bizPath);
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                 //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                 String jeditor = request.getParameter("jeditor");
                 if(oConvertUtils.isNotEmpty(jeditor)){
                 result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                 result.setSuccess(true);
                 return result;
                 }else{
                 savePath = this.uploadLocal(file,bizPath);
                 }
                 */
            } else {
                //update-begin-author:taoyan date:20200814 for:文件上传改造
                savePath = CommonUtils.upload(file, bizPath, uploadType);
                //update-end-author:taoyan date:20200814 for:文件上传改造
            }
            if (oConvertUtils.isNotEmpty(savePath)) {
                result.setMessage(savePath);
                result.setSuccess(true);
            } else {
                result.setMessage("上传失败！");
                result.setSuccess(false);
            }
            return result;
        }

        /**
         * 文件上传统一方法 外加 监控量测处置附件
         *
         * @param request
         * @param response
         * @return
         */
        @PostMapping(value = "/uploadWeiyan")
        public Result<?> uploadWeiyan(HttpServletRequest request, HttpServletResponse response) {
            Result<?> result = new Result<>();
            String savePath = "";
            String bizPath = request.getParameter("biz");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
            if (oConvertUtils.isEmpty(bizPath)) {
                if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                    //未指定目录，则用阿里云默认目录 upload
                    bizPath = "uploadWeiyan";
                    //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                    //result.setSuccess(false);
                    //return result;
                } else {
                    bizPath = "";
                }
            }
            if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                savePath = this.uploadLocal(file, bizPath);
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                 //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                 String jeditor = request.getParameter("jeditor");
                 if(oConvertUtils.isNotEmpty(jeditor)){
                 result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                 result.setSuccess(true);
                 return result;
                 }else{
                 savePath = this.uploadLocal(file,bizPath);
                 }
                 */
            } else {
                //update-begin-author:taoyan date:20200814 for:文件上传改造
                savePath = CommonUtils.upload(file, bizPath, uploadType);
                //update-end-author:taoyan date:20200814 for:文件上传改造
            }
            if (oConvertUtils.isNotEmpty(savePath)) {
                result.setMessage(savePath);
                result.setSuccess(true);
            } else {
                result.setMessage("上传失败！");
                result.setSuccess(false);
            }
            return result;
        }


        @PostMapping(value = "/uploadLockAI2")
        public Result<?> uploadLockAI2(HttpServletRequest request, HttpServletResponse response) throws IOException {

            Result<?> result = new Result<>();
            String savePath = "";
//            String bizPath = request.getParameter("biz");
            String bizPath = System.getProperty("java.io.tmpdir");
            String img = request.getParameter("img");
            InputStream inputStream = null;
            File tempfile = null;
//            ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
//            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//            MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
            try {
                ImageUtil.convertBase64StrToImage(img, bizPath + "temp.jpg");
                 tempfile = new File(bizPath + "temp.jpg");
                inputStream=new FileInputStream(tempfile);
                MultipartFile file = new MockMultipartFile("file",
                        tempfile.getName(),
                        "image/jpeg",
                        inputStream);
                ;// 获取上传文件对象
                if (oConvertUtils.isEmpty(bizPath)) {
                    if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                        //未指定目录，则用阿里云默认目录 upload
                        bizPath = "uploadAI";
                        //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                        //result.setSuccess(false);
                        //return result;
                    } else {
                        bizPath = "";
                    }
                }
                if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                    //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                    savePath = this.uploadLocal(file, bizPath);
                    //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                    /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                     //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                     String jeditor = request.getParameter("jeditor");
                     if(oConvertUtils.isNotEmpty(jeditor)){
                     result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                     result.setSuccess(true);
                     return result;
                     }else{
                     savePath = this.uploadLocal(file,bizPath);
                     }
                     */
                } else {
                    //update-begin-author:taoyan date:20200814 for:文件上传改造
                    savePath = CommonUtils.upload(file, UUID.randomUUID().toString(), uploadType);
                    //update-end-author:taoyan date:20200814 for:文件上传改造
                }
                if (oConvertUtils.isNotEmpty(savePath)) {
                    result.setMessage(savePath);
                    result.setSuccess(true);
                } else {
                    result.setMessage("上传失败！");
                    result.setSuccess(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.info("上传失败");
            } finally {
                inputStream.close();
                tempfile.delete();
            }
            return result;
        }

        /**
         * 文件上传统一方法
         *
         * @param request
         * @param response
         * @return
         */
        @PostMapping(value = "/uploadLockAI")
        public Result<?> uploadLockAI(HttpServletRequest request, HttpServletResponse response) {
            Result<?> result = new Result<>();
            String savePath = "";
            String bizPath = request.getParameter("biz");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
            if (oConvertUtils.isEmpty(bizPath)) {
                if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                    //未指定目录，则用阿里云默认目录 upload
                    bizPath = "uploadAI";
                    //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                    //result.setSuccess(false);
                    //return result;
                } else {
                    bizPath = "";
                }
            }
            if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                savePath = this.uploadLocal(file, bizPath);
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                 //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                 String jeditor = request.getParameter("jeditor");
                 if(oConvertUtils.isNotEmpty(jeditor)){
                 result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                 result.setSuccess(true);
                 return result;
                 }else{
                 savePath = this.uploadLocal(file,bizPath);
                 }
                 */
            } else {
                //update-begin-author:taoyan date:20200814 for:文件上传改造
                savePath = CommonUtils.upload(file, bizPath, uploadType);
                //update-end-author:taoyan date:20200814 for:文件上传改造
            }
            if (oConvertUtils.isNotEmpty(savePath)) {
                result.setMessage(savePath);
                result.setSuccess(true);
            } else {
                result.setMessage("上传失败！");
                result.setSuccess(false);
            }
            return result;
        }

        /**
         * 文件上传统一方法(锚杆)
         *
         * @param request
         * @param response
         * @return
         */
        @PostMapping(value = "/uploadLockMG")
        public Result<?> uploadLockMG(HttpServletRequest request, HttpServletResponse response) {
            Result<?> result = new Result<>();
            String savePath = "";
            String bizPath = request.getParameter("biz");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
            if (oConvertUtils.isEmpty(bizPath)) {
                if (CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)) {
                    //未指定目录，则用阿里云默认目录 upload
                    bizPath = "uploadFile";
                    //result.setMessage("使用阿里云文件上传时，必须添加目录！");
                    //result.setSuccess(false);
                    //return result;
                } else {
                    bizPath = "";
                }
            }
            if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                savePath = this.uploadLocal(file, bizPath);
                //update-begin-author:lvdandan date:20200928 for:修改JEditor编辑器本地上传
                /**  富文本编辑器及markdown本地上传时，采用返回链接方式
                 //针对jeditor编辑器如何使 lcaol模式，采用 base64格式存储
                 String jeditor = request.getParameter("jeditor");
                 if(oConvertUtils.isNotEmpty(jeditor)){
                 result.setMessage(CommonConstant.UPLOAD_TYPE_LOCAL);
                 result.setSuccess(true);
                 return result;
                 }else{
                 savePath = this.uploadLocal(file,bizPath);
                 }
                 */
            } else {
                //update-begin-author:taoyan date:20200814 for:文件上传改造
                savePath = CommonUtils.upload(file, bizPath, uploadType);
                //update-end-author:taoyan date:20200814 for:文件上传改造
            }
            if (oConvertUtils.isNotEmpty(savePath)) {
                result.setMessage(savePath);
                result.setSuccess(true);
            } else {
                result.setMessage("上传失败！");
                result.setSuccess(false);
            }
            return result;
        }

        /**
         * 本地文件上传
         *
         * @param mf      文件
         * @param bizPath 自定义路径
         * @return
         */
        private String uploadLocal(MultipartFile mf, String bizPath) {
            try {
                String ctxPath = uploadpath;
                String fileName = null;
                File file = new File(ctxPath + File.separator + bizPath + File.separator);
                if (!file.exists()) {
                    file.mkdirs();// 创建文件根目录
                }
                String orgName = mf.getOriginalFilename();// 获取文件名
                orgName = CommonUtils.getFileName(orgName);
                if (orgName.indexOf(".") != -1) {
                    fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
                } else {
                    fileName = orgName + "_" + System.currentTimeMillis();
                }
                String savePath = file.getPath() + File.separator + fileName;
                File savefile = new File(savePath);
                FileCopyUtils.copy(mf.getBytes(), savefile);
                String dbpath = null;
                if (oConvertUtils.isNotEmpty(bizPath)) {
                    dbpath = bizPath + File.separator + fileName;
                } else {
                    dbpath = fileName;
                }
                if (dbpath.contains("\\")) {
                    dbpath = dbpath.replace("\\", "/");
                }
                return dbpath;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return "";
        }

        //	@PostMapping(value = "/upload2")
        //	public Result<?> upload2(HttpServletRequest request, HttpServletResponse response) {
        //		Result<?> result = new Result<>();
        //		try {
        //			String ctxPath = uploadpath;
        //			String fileName = null;
        //			String bizPath = "files";
        //			String tempBizPath = request.getParameter("biz");
        //			if(oConvertUtils.isNotEmpty(tempBizPath)){
        //				bizPath = tempBizPath;
        //			}
        //			String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        //			File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        //			if (!file.exists()) {
        //				file.mkdirs();// 创建文件根目录
        //			}
        //			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //			MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
        //			String orgName = mf.getOriginalFilename();// 获取文件名
        //			fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        //			String savePath = file.getPath() + File.separator + fileName;
        //			File savefile = new File(savePath);
        //			FileCopyUtils.copy(mf.getBytes(), savefile);
        //			String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        //			if (dbpath.contains("\\")) {
        //				dbpath = dbpath.replace("\\", "/");
        //			}
        //			result.setMessage(dbpath);
        //			result.setSuccess(true);
        //		} catch (IOException e) {
        //			result.setSuccess(false);
        //			result.setMessage(e.getMessage());
        //			log.error(e.getMessage(), e);
        //		}
        //		return result;
        //	}

        /**
         * 预览图片&下载文件
         * 请求地址：http://localhost:8080/common/static/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
         *
         * @param request
         * @param response
         */
        @GetMapping(value = "/static/**")
        public void view(HttpServletRequest request, HttpServletResponse response) {
            // ISO-8859-1 ==> UTF-8 进行编码转换
            String imgPath = extractPathFromPattern(request);
            if (oConvertUtils.isEmpty(imgPath) || imgPath == "null") {
                return;
            }
            // 其余处理略
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                imgPath = imgPath.replace("..", "").replace("../", "");
                if (imgPath.endsWith(",")) {
                    imgPath = imgPath.substring(0, imgPath.length() - 1);
                }
                String filePath = uploadpath + File.separator + imgPath;
                File file = new File(filePath);
                if (!file.exists()) {
                    response.setStatus(404);
                    throw new RuntimeException("文件[" + imgPath + "]不存在..");
                }
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
                inputStream = new BufferedInputStream(new FileInputStream(filePath));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            } catch (IOException e) {
                log.error("预览文件失败" + e.getMessage());
                response.setStatus(404);
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

        }

        //	/**
        //	 * 下载文件
        //	 * 请求地址：http://localhost:8080/common/download/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
        //	 *
        //	 * @param request
        //	 * @param response
        //	 * @throws Exception
        //	 */
        //	@GetMapping(value = "/download/**")
        //	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //		// ISO-8859-1 ==> UTF-8 进行编码转换
        //		String filePath = extractPathFromPattern(request);
        //		// 其余处理略
        //		InputStream inputStream = null;
        //		OutputStream outputStream = null;
        //		try {
        //			filePath = filePath.replace("..", "");
        //			if (filePath.endsWith(",")) {
        //				filePath = filePath.substring(0, filePath.length() - 1);
        //			}
        //			String localPath = uploadpath;
        //			String downloadFilePath = localPath + File.separator + filePath;
        //			File file = new File(downloadFilePath);
        //	         if (file.exists()) {
        //	         	response.setContentType("application/force-download");// 设置强制下载不打开            
        //	 			response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
        //	 			inputStream = new BufferedInputStream(new FileInputStream(file));
        //	 			outputStream = response.getOutputStream();
        //	 			byte[] buf = new byte[1024];
        //	 			int len;
        //	 			while ((len = inputStream.read(buf)) > 0) {
        //	 				outputStream.write(buf, 0, len);
        //	 			}
        //	 			response.flushBuffer();
        //	         }
        //
        //		} catch (Exception e) {
        //			log.info("文件下载失败" + e.getMessage());
        //			// e.printStackTrace();
        //		} finally {
        //			if (inputStream != null) {
        //				try {
        //					inputStream.close();
        //				} catch (IOException e) {
        //					e.printStackTrace();
        //				}
        //			}
        //			if (outputStream != null) {
        //				try {
        //					outputStream.close();
        //				} catch (IOException e) {
        //					e.printStackTrace();
        //				}
        //			}
        //		}
        //
        //	}

        /**
         * @param modelAndView
         * @return
         * @功能：pdf预览Iframe
         */
        @RequestMapping("/pdf/pdfPreviewIframe")
        public ModelAndView pdfPreviewIframe(ModelAndView modelAndView) {
            modelAndView.setViewName("pdfPreviewIframe");
            return modelAndView;
        }

        /**
         * 把指定URL后的字符串全部截断当成参数
         * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
         *
         * @param request
         * @return
         */
        private static String extractPathFromPattern(final HttpServletRequest request) {
            String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
        }

        /**
         * 中转HTTP请求，解决跨域问题
         *
         * @param url 必填：请求地址
         * @return
         */
        @RequestMapping("/transitRESTful")
        public Result transitRESTful(@RequestParam("url") String url, HttpServletRequest request) {
            try {
                ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);
                // 中转请求method、body
                HttpMethod method = httpRequest.getMethod();
                JSONObject params;
                try {
                    params = JSON.parseObject(JSON.toJSONString(httpRequest.getBody()));
                } catch (Exception e) {
                    params = new JSONObject();
                }
                // 中转请求问号参数
                JSONObject variables = JSON.parseObject(JSON.toJSONString(request.getParameterMap()));
                variables.remove("url");
                // 在 headers 里传递Token
                String token = TokenUtils.getTokenByRequest(request);
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Access-Token", token);
                // 发送请求
                String httpURL = URLDecoder.decode(url, "UTF-8");
                ResponseEntity<String> response = RestUtil.request(httpURL, method, headers, variables, params, String.class);
                // 封装返回结果
                Result<Object> result = new Result<>();
                int statusCode = response.getStatusCodeValue();
                result.setCode(statusCode);
                result.setSuccess(statusCode == 200);
                String responseBody = response.getBody();
                try {
                    // 尝试将返回结果转为JSON
                    Object json = JSON.parse(responseBody);
                    result.setResult(json);
                } catch (Exception e) {
                    // 转成JSON失败，直接返回原始数据
                    result.setResult(responseBody);
                }
                return result;
            } catch (Exception e) {
                log.debug("中转HTTP请求失败", e);
                return Result.error(e.getMessage());
            }
        }

    }
}
