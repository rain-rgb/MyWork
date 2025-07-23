package com.trtm.sy.registerfile.tool;


import com.trtm.sy.registerfile.config.SxConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.jeecg.common.exception.JeecgBootException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件上传工具类
 *
 * @author
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 200 * 1024 * 1024;


    /**
     * 315上传视频
     */
    public static final long DEFAULT_MAX_SIZE_315 = 2000 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = SxConfig.getProfile();

    private static int counter = 0;

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
//    public static final String upload(MultipartFile file) throws IOException {
//        try {
//            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
//        } catch (Exception e) {
//            throw new BusinessModularException(e.getMessage());
//        }
//    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
//    public static final String upload(String baseDir, MultipartFile file) throws IOException {
//        try {
//            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
//        } catch (Exception e) {
//            throw new IOException(e.getMessage(), e);
//        }
//    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException           如果超出最大大小
     * @throws FileNameLengthLimitExceededFeilException 文件名太长
     * @throws IOException                              比如读写文件出错时
     * @throws InvalidExtensionException                文件校验异常
     */
//    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
//            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededFeilException,
//            InvalidExtensionException {
//        int fileNamelength = file.getOriginalFilename().length();
//        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
//            throw new FileNameLengthLimitExceededFeilException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
//        }
//
//        assertAllowed(file, allowedExtension);
//
//        String fileName = extractFilename(file);
//
//        File desc = getAbsoluteFile(baseDir, fileName);
//        file.transferTo(desc);
//        String pathFileName = getPathFileName(baseDir, fileName);
//        return pathFileName;
//    }

    /**
     * 读取图片
     *
     * @param path
     * @param request
     * @param response
     * @return
     */
    public static boolean downloadByFlow(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        boolean flag = true;
        FileInputStream in;
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        path = path.replace("/profile", "");
        try {
            //图片读取路径
            in = new FileInputStream(path);
            int i = in.available();
            byte[] data = new byte[i];
            in.read(data);
            in.close();
            //写图片
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + encodingFilename(fileName) + "." + extension;
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = SxConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        // String pathFileName = "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName) {
        fileName = fileName.replace("_", " ");
        fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
        return fileName;
    }

//    /**
//     * 文件大小校验
//     *
//     * @param file 上传的文件
//     * @return
//     * @throws FileSizeLimitExceededException 如果超出最大大小
//     * @throws InvalidExtensionException
//     */
//    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
//            throws FileSizeLimitExceededException, InvalidExtensionException {
//        long size = file.getSize();
//        if (DEFAULT_MAX_SIZE_315 != -1 && size > DEFAULT_MAX_SIZE_315) {
//            throw new FileSizeLimitExceededFeilException(DEFAULT_MAX_SIZE_315 / 1024 / 1024);
//        }
//
//        String fileName = file.getOriginalFilename();
//        String extension = getExtension(file);
//        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
//            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
//                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension, fileName);
//            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
//                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,fileName);
//            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
//                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,fileName);
//            } else {
//                throw new InvalidExtensionException(allowedExtension, extension, fileName);
//            }
//        }
//
//    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 业务文件的上传方法
     *
     * @param baseDir 如"D:\\suxin\\uploadFile\\announcement",则会把文件上传到该目录下，并且根据日期生成子文件夹，文件名称为不重复的随机字符串
     * @param file    上传的文件
     * @return 返回上传成功文件在硬盘上的绝对路径
     * @throws IOException
     */
//    public static final String uploadFile(String baseDir, MultipartFile file) throws IOException {
//        try {
//            return uploadFile(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
//        } catch (Exception e) {
//            throw new JeecgBootException(e.getMessage());
//        }
//    }

    /**
     * 业务文件的上传方法
     *
     * @param baseDir 如"D:\\suxin\\uploadFile\\announcement",则会把文件上传到该目录下，并且根据日期生成子文件夹，文件名称为不重复的随机字符串
     * @param file    上传的文件
     * @return 返回上传成功文件在硬盘上的绝对路径
     * @throws IOException
     */
//    public static final String uploadFileVideo(String baseDir, MultipartFile file) throws IOException {
//        try {
//            return uploadFile(baseDir, file, MimeTypeUtils.MEDIA_EXTENSION);
//        } catch (Exception e) {
//            throw new IOException(e.getMessage(), e);
//        }
//    }

    /**
     * 业务文件的上传方法
     *
     * @param baseDir 如"D:\\suxin\\uploadFile\\announcement",则会把文件上传到该目录下，并且根据日期生成子文件夹，文件名称为不重复的随机字符串
     * @param file    上传的文件
     * @return 返回上传成功文件在硬盘上的绝对路径
     * @throws IOException
     */
//    public static final String uploadFileVideos(String baseDir, MultipartFile file) throws IOException {
//        try {
//            return uploadFile(baseDir, file, MimeTypeUtils.MEDIA_EXTENSIONS);
//        } catch (Exception e) {
//            throw new IOException(e.getMessage(), e);
//        }
//    }

//    /**
//     * 文件上传
//     *
//     * @param baseDir 如"D:\\suxin\\uploadFile\\announcement",则会把文件上传到该目录下，并且根据日期生成子文件夹，文件名称为不重复的随机字符串
//     * @param file    上传的文件
//     * @return 返回上传成功文件在硬盘上的绝对路径
//     * @throws FileSizeLimitExceededException           如果超出最大大小
//     * @throws FileNameLengthLimitExceededFeilException 文件名太长
//     * @throws IOException                              比如读写文件出错时
//     * @throws InvalidExtensionException                文件校验异常
//     */
//    public static final String uploadFile(String baseDir, MultipartFile file, String[] allowedExtension)
//            throws IOException, FileNameLengthLimitExceededFeilException, InvalidExtensionException {
//        int fileNamelength = file.getOriginalFilename().length();
//        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
//            throw new FileNameLengthLimitExceededFeilException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
//        }
//
//        assertAllowed(file, allowedExtension);
//        String filePath = extractFilename(file);
//
//        File desc = getAbsoluteFile(baseDir, filePath);
//        file.transferTo(desc);
//        return desc.getAbsolutePath();
//    }


    /**
     * 业务文件的下载方法
     *
     * @param path     硬盘上的绝对路径
     * @param response
     * @throws Exception 抛出去的异常让全局异常解析器拦截
     */
    public static void downloadFile(String path, String filename, HttpServletResponse response) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            if (filename == null || "".equals(filename)) {
                filename = file.getName();
            }
            try {
                // 以流的形式下载文件
                InputStream fis = new BufferedInputStream(new FileInputStream(path));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type");
                // 设置response的Header
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(filename, "UTF-8"));
                response.addHeader("Content-Length", "" + file.length());
                response.addHeader("Access-Control-Expose-Headers", "File-Name");
                response.addHeader("File-Name", URLEncoder.encode(filename, "UTF-8"));
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException e) {
                throw new Exception(e.getMessage(), e.getCause());
            }
        } else {
            throw new Exception("文件不存在");
        }
    }

    /**
     * 业务文件压缩包的下载方法
     *
     * @param
     * @param response
     * @throws Exception 抛出去的异常让全局异常解析器拦截
     */
    public static void downloadFileZip(List<String> pathss, String filename, String zipFilePath, HttpServletResponse response, HttpServletRequest request, String Folder) throws Exception {
        if (pathss.size() != 0) {
            // 创建临时路径,存放压缩文件
//            String zipFilePath = "D:\\suxin\\zip";
            // 压缩输出流,包装流,将临时文件输出流包装成压缩流,将所有文件输出到这里,打成zip包
//            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
            // 循环调用压缩文件方法,将一个一个需要下载的文件打入压缩文件包
//            for (String path : pathss) {
//                // 该方法在下面定义
//                fileToZip(path, zipOut);
//            }
            // 压缩完成后,关闭压缩流
//            zipOut.close();
            filename = filename + System.currentTimeMillis() + ".zip";
//            ServletOutputStream outputStream = response.getOutputStream();
//            FileInputStream inputStream = new FileInputStream(zipFilePath);
            try {
//                IOUtils.copy(inputStream, outputStream);
                // 关闭输入流
//                inputStream.close();

                //下载完成之后，删掉这个zip包
//                File fileTempZip = new File(zipFilePath);
//                fileTempZip.delete();
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

                response.setContentType("multipart/form-data");
                response.setCharacterEncoding("utf-8");
                response.addHeader("Access-Control-Expose-Headers", "File-Name");
                response.addHeader("File-Name", URLEncoder.encode(filename, "UTF-8"));
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition",
                        "attachment; filename=" + toUtf8String(filename, request));
                FileUploadUtils.toZip(Folder, response.getOutputStream(), true);
                toClient.flush();
                toClient.close();
            } catch (IOException e) {
                throw new Exception(e.getMessage(), e.getCause());
            }
        } else {
            throw new Exception("文件不存在");
        }
    }

    public static String toUtf8String(String fileName, HttpServletRequest request) throws Exception {
        final String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {// IE浏览器（旧版/新版）
            finalFileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器URLEncoder
        }
        return finalFileName;
    }

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[2 * 1024];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }

                }
            }
        }
    }

    // 删除文件夹
    public boolean delete(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (!f.delete()) {
                    System.out.println(f.getAbsolutePath() + " delete error!");
                    return false;
                }
            } else {
                if (!this.delete(f.getAbsolutePath())) {
                    return false;
                }
            }
        }
        return file.delete();
    }


    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    // 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    //    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded"; // 内容类型
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);
    private static int readTimeOut = 10 * 1000; // 读取超时
    private static int connectTimeout = 10 * 1000; // 超时时间

    /**
     * 上传文件
     */
    public static String uploadFile(File file, String fileKey, String RequestURL, Map<String, String> param, String filename) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";
            /***
             * 以下是用于上传参数
             */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    dos.write(params.getBytes());
                }
            }
            sb = null;
            params = null;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            /** 上传文件 */
            sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + filename + "\"" + LINE_END);
//            sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的
            sb.append("Content-Type:multipart/form-data" + LINE_END); // 这里配置的Content-type很重要的
            // ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;


            dos.write(params.getBytes());

            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + RequestURL + ",param=" + param, ex);
            }
        }
        System.out.println(result);
        return result.toString();
    }


}
