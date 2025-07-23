package org.jeecg.modules.job.jobutil;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 杭州高迅钉钉考勤工具类
 */
@Component
public class GXDingTalkUtil {

    @Autowired
    private RedisUtil redisUtil;
    // 应用的appKey
    public static final String APP_KEY = "ding1bboriegmq6aavxq";
    // 应用的appSecret
    public static final String APP_SECRET = "loZvaWCuCJnqfvVHB0UR49VdxQWuMhMnDi5XDFlX4So0lNFH253ctpguuTVVgCOr";


    /**
     * 调用接口获取AccessToken
     *
     * @return String
     */
    public String getAccessToken() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APP_KEY);
        request.setAppsecret(APP_SECRET);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);
            redisUtil.set("access_token", response.getAccessToken(), 7200);
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response.getAccessToken();
    }

    /**
     * 根据用户的userid list数组  [xx,xx] 格式   获取打卡信息
     *
     * @return
     */
    public OapiAttendanceListRecordResponse.Recordresult attendanceRecord(List userIdList, String startTime, String endTime, String token) {
        // 通过调用接口获取考勤打卡结果
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
        OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
        // 查询考勤打卡记录的起始工作日
        req.setCheckDateFrom(startTime);
        // 查询考勤打卡记录的结束工作日
        req.setCheckDateTo(endTime);
        // 员工在企业内的userid列表，最多不能超过50个。
        req.setUserIds(userIdList);
        OapiAttendanceListRecordResponse.Recordresult recordresult = new OapiAttendanceListRecordResponse.Recordresult();
        try {
            OapiAttendanceListRecordResponse rsp = client.execute(req, token);
            List<OapiAttendanceListRecordResponse.Recordresult> recordResults = rsp.getRecordresult();
            if (recordResults.size()>0){
                recordresult = recordResults.get(0);
                return recordresult;
            }
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;
    }

    /**
     * 调用钉钉接口   获取部门用户userid列表
     *
     * @return
     */
    public Map getUserList(Long deptId, Long cursor,String token) {
        Map map=new HashMap();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
            OapiV2UserListRequest req = new OapiV2UserListRequest();
            req.setDeptId(deptId);
            req.setCursor(cursor);
            req.setSize(100L);
            OapiV2UserListResponse rsp = client.execute(req, token);
            List<OapiV2UserListResponse.ListUserResponse> list = rsp.getResult().getList();
            map.put("userList",list);
            if (rsp.getResult().getHasMore()){
                map.put("hasMore",rsp.getResult().getHasMore());
                map.put("next_cursor",rsp.getResult().getNextCursor());
            }else{
                map.put("hasMore",rsp.getResult().getHasMore());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 调用钉钉接口   获取部门deptId列表
     *
     * @return
     */
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDeptList(String token) {
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> result = new ArrayList<>();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            req.setDeptId(500462207L);
            OapiV2DepartmentListsubResponse rsp = client.execute(req, token);
            result = rsp.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return result;
    }
}
