package org.jeecg;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CallApiDemo {


    private static final String DASH = "--";
    private static final String BOUNDARY ="";// generateBoundary();
    private static final String NEW_LINE = "\r\n";

    private static final String SERVICE_URL = "";
    private static final String APP_TOKEN = "";
    private static final String APP_SECRET = "";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    public static void main(String[] args) {
        String urlStr = SERVICE_URL + "/v2/document/createbyfile";

        HttpURLConnection conn = null;
        OutputStream out = null;

        long timestamp = System.currentTimeMillis();
        try {

         URL url = new URL("");// urlBuilder.toString();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", url.getHost());
            conn.setRequestProperty("Accept", "text/plain,application/json");
            conn.setRequestProperty("User-Agent", "privateapp-java-api-client");
            conn.setRequestProperty("x-qys-accesstoken", APP_TOKEN);
            conn.setRequestProperty("x-qys-timestamp", String.valueOf(timestamp));
//            conn.setRequestProperty("x-qys-signature", md5(APP_TOKEN, APP_SECRET, timestamp));
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("transfer-encoding","chunked");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(10240);

            out = conn.getOutputStream();

//            writeMultipart(out, "file", new File("/home/qiyuesuo/upload/file.pdf"));
//            writeMultipart(out, "title", "契约锁劳动合同-张三");
//            writeMultipart(out, "fileType", "pdf");
//            writeMultipart(out, "width", 100);
//            writeMultipart(out, "height", 100);
//            writeMultipart(out, "waterMarks", "[{\"content\":\"水印1\",\"fontSize\":\"30\",\"location\":\"UPPER_LEFT\",\"imageBase64\":\"/9j/4AAQSkZJRgABAQEASABIAAD/4gxY\"},{\"content\":\"水印2\",\"fontSize\":\"30\",\"location\":\"LOWER_LEFT\"}]");
//            writeMultipart(out, "form", "{\"textFields\":[{\"id\":\"text1\",\"width\":200,\"height\":30,\"keyword\":\"1\",\"llxOffset\":30,\"llyOffset\":0,\"llx\":0,\"lly\":0,\"fontSize\":12},{\"id\":\"text2\",\"width\":200,\"height\":30,\"keyword\":\"2\",\"llxOffset\":40,\"llyOffset\":0,\"llx\":0,\"lly\":0,\"fontSize\":12}]}");


            out.write(new StringBuilder().append(DASH).append(BOUNDARY).append(DASH).append(NEW_LINE).toString().getBytes(CHARSET));
            out.flush();

            int statusCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (statusCode == 200) {
                try (InputStream ins = conn.getInputStream()){
                    //process response data
                } catch (Exception e){
                    // 处理异常情况
                }
            } else {
                // 处理异常情况
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}