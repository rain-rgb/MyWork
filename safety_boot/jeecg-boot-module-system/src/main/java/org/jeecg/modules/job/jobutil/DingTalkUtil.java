package org.jeecg.modules.job.jobutil;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * \* @author: Xx
 * \* Date: 2021/10/19
 * \* Time: 14:45
 * \* Description: 钉钉工具类
 * \
 */
@Component
public class DingTalkUtil {
    @Autowired
    private RedisUtil redisUtil;
    private static DingTalkUtil dingTalkUtil;
    @PostConstruct
    public void init() {
        dingTalkUtil = this;
    }
    private static String APPKEY="dingi2eswtkjsoywbhix";//义东高速(东阳段：预制场钉钉考勤数据接入)APPkey
    private static String APPSECRET="lJQmF99IG-5Hpj8m-WdcJnPzJ3w3JfFPbaezD40mbD7cM6l0IrqmrF1yqh7l553P";////义东高速(东阳段：预制场钉钉考勤数据接入)APPSecret

    //获取accessToken
    public  String getToken (){
        DefaultDingTalkClient client = new
                DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APPKEY);
        request.setAppsecret(APPSECRET);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            redisUtil.set("access_token",response.getAccessToken(),7200);
            return response.getAccessToken();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用钉钉接口   获取部门用户userid列表
     * @return
     */
    public Map GetUserMessageDingTalk(long offset,String access_token){
        Map map=new HashMap();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
            OapiV2UserListRequest req = new OapiV2UserListRequest();
            req.setDeptId(503750406L);
            req.setCursor(offset);
            req.setSize(100L);
            OapiV2UserListResponse rsp = client.execute(req, access_token);
            List<OapiV2UserListResponse.ListUserResponse> list = rsp.getResult().getList();
            map.put("userlist",list); //用户信息
            if(rsp.getResult().getHasMore()){
                map.put("hasMore",rsp.getResult().getHasMore());//是否还有下一页
                map.put("next_cursor",rsp.getResult().getNextCursor());//分页查询的游标，最开始传0，后续传返回参数中的next_cursor值。
            }else{
                map.put("hasMore",rsp.getResult().getHasMore());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return map;
    }


    public Map GetUserMessageDingTalks(long offset,String access_token,long departi){
        Map map=new HashMap();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
            OapiV2UserListRequest req = new OapiV2UserListRequest();
            req.setDeptId(departi);
            req.setCursor(offset);
            req.setSize(100L);
            OapiV2UserListResponse rsp = client.execute(req, access_token);
            List<OapiV2UserListResponse.ListUserResponse> list = rsp.getResult().getList();
            map.put("userlist",list); //用户信息
            if(rsp.getResult().getHasMore()){
                map.put("hasMore",rsp.getResult().getHasMore());//是否还有下一页
                map.put("next_cursor",rsp.getResult().getNextCursor());//分页查询的游标，最开始传0，后续传返回参数中的next_cursor值。
            }else{
                map.put("hasMore",rsp.getResult().getHasMore());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 根据用户的userid list数组  [xx,xx] 格式   获取打卡信息
     * @return
     */
    public List PostListRecord(List userid,String startTime,String endTime,String access_token){
        List<OapiAttendanceListRecordResponse.Recordresult> recordresult=new ArrayList<>();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
            OapiAttendanceListRecordRequest req=new OapiAttendanceListRecordRequest();
            req.setUserIds(userid);
            req.setCheckDateFrom(startTime);
            req.setCheckDateTo(endTime);
            OapiAttendanceListRecordResponse rsp = client.execute(req, access_token);
            recordresult = rsp.getRecordresult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return recordresult;
    }



    //获取accessToken
    public  String getTokens (String APPKEY,String APPSECRET){
        DefaultDingTalkClient client = new
                DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APPKEY);
        request.setAppsecret(APPSECRET);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            redisUtil.set("access_token",response.getAccessToken(),7200);
            return response.getAccessToken();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用钉钉接口   获取部门用户userid列表
     * @return
     */
    public Map GetUserMessageDingTalks(long deptid,long offset,String access_token){
        Map map=new HashMap();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
            OapiV2UserListRequest req = new OapiV2UserListRequest();
            req.setDeptId(deptid);
            req.setCursor(offset);
            req.setSize(100L);
            OapiV2UserListResponse rsp = client.execute(req, access_token);
            List<OapiV2UserListResponse.ListUserResponse> list = rsp.getResult().getList();
            map.put("userlist",list); //用户信息
            if(rsp.getResult().getHasMore()){
                map.put("hasMore",rsp.getResult().getHasMore());//是否还有下一页
                map.put("next_cursor",rsp.getResult().getNextCursor());//分页查询的游标，最开始传0，后续传返回参数中的next_cursor值。
            }else{
                map.put("hasMore",rsp.getResult().getHasMore());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return map;
    }

}
