package org.jeecg.modules.job.kezhujob;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * @ClassName HTTPUtil：
 * @Description TODO
 * @Author 55314
 * @Date 2023/2/6 10:36
 * @Version 1.0
 **/

    public class HTTPUtil {

        /**
         * 向指定URL发送GET方法的请求
         *
         * @param httpurl
         *            请求参数用?拼接在url后边，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return result 所表明远程资源的响应结果
         */
        public static String doGet(String httpurl) {
            HttpURLConnection connection = null;
            InputStream is = null;
            BufferedReader br = null;
            String result = null;// 返回结果字符串
            try {
                // 建立远程url链接对象
                URL url = new URL(httpurl);
                // 经过远程url链接对象打开一个链接，强转成httpURLConnection类
                connection = (HttpURLConnection) url.openConnection();
                // 设置链接方式：get
                connection.setRequestMethod("GET");
                // 设置链接主机服务器的超时时间：15000毫秒
                connection.setConnectTimeout(15000);
                // 设置读取远程返回的数据时间：60000毫秒
                connection.setReadTimeout(60000);
                // 发送请求
                connection.connect();
                // 经过connection链接，获取输入流
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    // 封装输入流is，并指定字符集
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    // 存放数据
                    StringBuffer sbf = new StringBuffer();
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    result = sbf.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                connection.disconnect();// 关闭远程链接
            }
            return result;
        }


        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param httpUrl
         *            发送请求的 URL
         * @param param
         *            请求参数应该是{"key":"==g43sEvsUcbcunFv3mHkIzlHO4iiUIT R7WwXuSVKTK0yugJnZSlr6qNbxsL8OqCUAFyCDCoRKQ882m6cTTi0q9uCJsq JJvxS+8mZVRP/7lWfEVt8/N9mKplUA68SWJEPSXyz4MDeFam766KEyvqZ99d"}的形式。
         * @return 所表明远程资源的响应结果
         */
        public static String doPost(String httpUrl, String param) {

            HttpURLConnection connection = null;
            InputStream is = null;
            OutputStream os = null;
            BufferedReader br = null;
            String result = null;
            try {
                URL url = new URL(httpUrl);
                // 经过远程url链接对象打开链接
                connection = (HttpURLConnection) url.openConnection();
                // 设置链接请求方式
                connection.setRequestMethod("POST");
                // 设置链接主机服务器超时时间：15000毫秒
                connection.setConnectTimeout(15000);
                // 设置读取主机服务器返回数据超时时间：60000毫秒
                connection.setReadTimeout(60000);
                // 默认值为：false，当向远程服务器传送数据/写数据时，须要设置为true
                connection.setDoOutput(true);
                // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数无关紧要
                connection.setDoInput(true);
                // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
                connection.setRequestProperty("Content-Type", "application/json");
                // 经过链接对象获取一个输出流
                os = connection.getOutputStream();
                // 经过输出流对象将参数写出去/传输出去,它是经过字节数组写出的
                os.write(param.getBytes());
                // 经过链接对象获取一个输入流，向远程读取
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    // 对输入流对象进行包装:charset根据工做项目组的要求来设置
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuffer sbf = new StringBuffer();
                    String temp = null;
                    // 循环遍历一行一行读取数据
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    result = sbf.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 断开与远程地址url的链接
                connection.disconnect();
            }
            return result;
        }


        /**
         *
         * @param httpUrl  请求的url
         * @param param  form表单的参数（key,value形式）
         * @return
         */
        public static String doPostForm(String httpUrl, Map params, String cookie) throws IOException {
            HttpURLConnection connection = null;
            OutputStream os = null;
            InputStream is = null;
            BufferedReader br = null;
            String result = "";
            try {
                URL url = new URL(httpUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(60000);
                connection.setRequestProperty("Cookie", cookie);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                os = connection.getOutputStream();
                os.write(createLinkString(params).getBytes(StandardCharsets.UTF_8));
                is = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    result += line + "\n";
                }
            } finally {
                if (br != null) {
                    br.close();
                }
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }



    /**
         * 把数组全部元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
         * @param params 须要排序并参与字符拼接的参数组
         * @return 拼接后字符串
         */
        public static String createLinkString(Map<String, String> params) {
            List<String> keys = new ArrayList<String>(params.keySet());
            Collections.sort(keys);

            StringBuilder prestr = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = String.valueOf(params.get(key));
                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    prestr.append(key).append("=").append(value);
                } else {
                    prestr.append(key).append("=").append(value).append("&");
                }
            }
            return prestr.toString();
        }
}
