package org.jeecg.modules.job;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonArray;
import com.trtm.iot.ydwbs.entity.Ydwbs;
import com.trtm.iot.ydwbs.service.IYdwbsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.job.jobutil.BysUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/14
 * \* Time: 15:22
 * \* Description: 义东wbs拿原数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ydwbs implements Job {
    @Autowired
    private  IYdwbsService iYdwbsService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;


    public String GetYDwbs() {
        //创建HttpClient对象
        String codes = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象，设置url访问地址
        HttpPost httpPost = new HttpPost("http://apix.jizhibao.com.cn/Contract/GetFundListByCont");
        //声明List集合，封装表单中的参数
        List params = new ArrayList();
        //设置请求地址
        params.add(new BasicNameValuePair("projid", "d6221674-8652-47ba-bfc6-baa2218c9216"));
        params.add(new BasicNameValuePair("contid", "85909117-0702-4f42-a7f7-7f04c548d006"));
        params.add(new BasicNameValuePair("isall", "1"));
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(params, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpPost);
            //解析响应
            String content = EntityUtils.toString(response.getEntity(), "utf8");
            String substring = content.substring(content.indexOf("["), content.indexOf("]") + 1);
            String res = StringEscapeUtils.unescapeJava(substring);
            JSONArray jsonArray = JSONUtil.parseArray(res);
            for (Object o : jsonArray) {
                JSONObject jsonObject2 = (JSONObject) o;
                Ydwbs ydwbs=new Ydwbs();
                ydwbs.setCode(jsonObject2.getStr("code"));
                ydwbs.setId(jsonObject2.getStr("id"));
                ydwbs.setContId(jsonObject2.getStr("cont_id"));
                ydwbs.setIsLeaf(jsonObject2.getStr("is_leaf"));
                ydwbs.setName(jsonObject2.getStr("name"));
                ydwbs.setNodetype(jsonObject2.getStr("NodeType"));
                ydwbs.setPic(jsonObject2.getStr("pic"));
                ydwbs.setPid(jsonObject2.getStr("pid"));
                ydwbs.setSort(jsonObject2.getStr("sort"));
                ydwbs.setStack(jsonObject2.getStr("stack"));
                boolean save = iYdwbsService.save(ydwbs);
                if(save){
                    System.out.println("添加成功"+jsonObject2.getStr("id"));
                }else{
                    System.out.println("添加失败"+jsonObject2.getStr("id"));
                }
            }
            //System.out.println(content.length());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codes;
        }
    }


    public String GetYDwbsAPI() {
        //创建HttpClient对象
        String codes = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象，设置url访问地址
        HttpPost httpPost = new HttpPost("http://apix.jizhibao.com.cn/Contract/GetFundListByCont");
        //声明List集合，封装表单中的参数
        List params = new ArrayList();
        //设置请求地址
        params.add(new BasicNameValuePair("projid", "01385444-7425-476C-B5E6-A4713DC0A61F"));
        params.add(new BasicNameValuePair("contid", "F224CE1C-3A04-4787-AEC6-F58F80F461B0"));
        params.add(new BasicNameValuePair("isall", "1"));
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(params, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpPost);
            //解析响应
            String content = EntityUtils.toString(response.getEntity(), "utf8");
            String substring = content.substring(content.indexOf("["), content.indexOf("]") + 1);
            String res = StringEscapeUtils.unescapeJava(substring);
            JSONArray jsonArray = JSONUtil.parseArray(res);
            for (Object o : jsonArray) {
                JSONObject jsonObject2 = (JSONObject) o;
                SysDepartproject sysDepartproject=new SysDepartproject();
                sysDepartproject.setParentId(jsonObject2.getStr("pid"));
                sysDepartproject.setId(jsonObject2.getStr("id"));
                sysDepartproject.setDepartName(jsonObject2.getStr("name"));
                sysDepartprojectService.saveDepartData(sysDepartproject,"肖广东");
                System.out.println("当前运行到"+jsonObject2.getStr("code"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codes;
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        GetYDwbsAPI();
    }
}
