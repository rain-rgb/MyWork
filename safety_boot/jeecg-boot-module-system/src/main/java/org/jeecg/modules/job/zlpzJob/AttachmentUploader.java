package org.jeecg.modules.job.zlpzJob;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * @ClassName AttachmentUploader：
 * @Description 附件上传
 * @Author 55314
 * @Date 2024/2/23 8:43
 * @Version 1.0
 **/
public class AttachmentUploader {

    private static final String UPLOAD_URL = "https://sjsn.jtyst.zj.gov.cn:21086/dtas-server/api/service/ZLPZ_ZX_SCFJ"; // 请替换为实际上传接口地址

    public void uploadAttachment(String token, File file, String fileName, String fileType) throws Exception {
        String boundary = "----WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis()); // 边界标识符
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("token", token); // 添加Token信息

        DataOutputStream out = new DataOutputStream(conn.getOutputStream());

        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n")
                .append("Content-Type: application/octet-stream\r\n\r\n"); // 根据实际情况调整Content-Type
        byte[] fileBytes = readFileToBytes(file);
        String base64FileStr = Base64.getEncoder().encodeToString(fileBytes); // 假设已有Base64编码方法
        sb.append(base64FileStr).append("\r\n");

        sb.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"fileName\"\r\n\r\n")
                .append(fileName).append("\r\n");

        sb.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"type\"\r\n\r\n")
                .append(fileType).append("\r\n");

        sb.append("--").append(boundary).append("--\r\n");

        out.write(sb.toString().getBytes());
        out.flush();
        out.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 处理响应结果
            System.out.println(response.toString());
        } else {
            System.out.println("Error : HTTP error code : " + responseCode);
        }

        conn.disconnect();
    }

    private byte[] readFileToBytes(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fis.read(buffer);
        fis.close();
        return buffer;
    }

    public static void main(String[] args) throws Exception {
        AttachmentUploader uploader = new AttachmentUploader();
        File file = new File("your_file_path"); // 替换为你的文件路径
        uploader.uploadAttachment("另外提供的token值", file, "文件名.jpg", "image/jpeg");
    }
}
