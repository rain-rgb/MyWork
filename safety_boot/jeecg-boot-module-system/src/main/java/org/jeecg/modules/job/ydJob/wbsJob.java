package org.jeecg.modules.job.ydJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.impl.SysDepartprojectServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName wbsJob：
 * @Description TODO
 * @Author 55314
 * @Date 2023/3/6 13:34
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class wbsJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private SysDepartprojectServiceImpl sysDepartprojectService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        List<String> urlList = new ArrayList();
        String url1 = "http://apix.jizhibao.com.cn/Api/GetFundListByCont?projid=D6221674-8652-47BA-BFC6-BAA2218C9216&contid=85909117-0702-4F42-A7F7-7F04C548D006&isall=1";
        String url2 = "http://apix.jizhibao.com.cn/Api/GetFundListByCont?projid=01385444-7425-476c-b5e6-a4713dc0a61f&contid=a4c9da6b-20d5-45f4-b775-72bb982202f3&isall=1";
        String url3 = "http://apix.jizhibao.com.cn/Api/GetFundListByCont?projid=01385444-7425-476c-b5e6-a4713dc0a61f&contid=F224CE1C-3A04-4787-AEC6-F58F80F461B0&isall=1";
        urlList.add(url1);
        urlList.add(url2);
        urlList.add(url3);
        String url = "";
        for (int i = 0; i < urlList.size(); i++) {
            url = urlList.get(i);
            String body1 = HttpRequest.get(url)
                    .header("Content-Type", "application/json")
                    .execute()
                    .body();

            JSONObject jsonObject2 = new JSONObject(body1);
            String rows = String.valueOf(jsonObject2.get("eData"));
            save((i+1) + "b", rows);
        }
    }

    void save(String i, String wbs) {
        JSONArray deptobjects = JSONUtil.parseArray(wbs);
        for (Object object : deptobjects) {
            JSONObject jsonObject = (JSONObject) object;

            QueryWrapper<SysDepartproject> queryWrappercf = new QueryWrapper<>();
            queryWrappercf.eq("id", i + jsonObject.getStr("id"));
            SysDepartproject listcf = sysDepartprojectService.getOne(queryWrappercf);
            if (listcf != null) {

                listcf.setTreeid(jsonObject.getStr("sort"));
                listcf.setMemo(jsonObject.getStr("is_leaf"));
                listcf.setOrgType(jsonObject.getStr("NodeType"));
                listcf.setId(i + jsonObject.getStr("id"));
                listcf.setParentId(i + jsonObject.getStr("pid"));
                listcf.setDepartName(jsonObject.getStr("name"));
                listcf.setUpdateTime(new Date());

                sysDepartprojectService.saveOrUpdate(listcf);
                log.info("更新成功！----------------" + listcf.getDepartName() + "----------------" + listcf.getOrgCode());
                continue;
            }

            SysDepartproject sysDepartproject = new SysDepartproject();
            sysDepartproject.setId(i + jsonObject.getStr("id"));
            sysDepartproject.setParentId(i + jsonObject.getStr("pid"));
            sysDepartproject.setDepartName(jsonObject.getStr("name"));

            QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", i + jsonObject.getStr("pid"));
            List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
            String orgCode1 = list.get(0).getOrgCode();
            //获取向同级数量
            QueryWrapper<SysDepartproject> queryWrappertj = new QueryWrapper<>();
            queryWrappertj.eq("parent_id", i + jsonObject.getStr("pid"));
            List<SysDepartproject> listtj = sysDepartprojectService.list(queryWrappertj);
            int size = listtj.size();
            int ii = size / 100;
            char ch = (char)('A' + ii);
            int remainder = size % 100;
            String format = remainder >= 10 ? "%c%d" : "%c0%d";
            String orgcodeEnd = String.format(format, ch, remainder);

            StringBuilder orgcodeBuilder = new StringBuilder(orgCode1);
            orgcodeBuilder.append(orgcodeEnd);
            String orgcode = orgcodeBuilder.toString();

            sysDepartproject.setOrgCode(orgcode);
            sysDepartproject.setDelFlag("0");
            sysDepartproject.setCreateBy("王凯");
            sysDepartproject.setTreeid(jsonObject.getStr("sort"));
            sysDepartproject.setMemo(jsonObject.getStr("is_leaf"));
            sysDepartproject.setOrgType(jsonObject.getStr("NodeType"));
//            sysDepartproject.setOrgCodes("A05A01A05A02A01A03A02");
            boolean save = false;
            boolean update = false;
            QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", i + jsonObject.getStr("id"));
            List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
            if (list1.size() > 0) {
                update = sysDepartprojectService.update(sysDepartproject, queryWrapper1);
            } else {
                save = sysDepartprojectService.save(sysDepartproject);
            }
            if (save) {
                System.out.println("添加成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
            }
            if (update) {
                System.out.println("修改成功！  " + sysDepartproject.getDepartName() + "----------------" + sysDepartproject.getOrgCode());
            }
        }
    }

    public void update() {
        for (int i = 100; i < 1000; i++) {
            QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
            sysDepartprojectQueryWrapper.like("org_code", "A" +i);
            List<SysDepartproject> list = sysDepartprojectService.list(sysDepartprojectQueryWrapper);
            for (SysDepartproject sysDepartproject:list){
                String orgCode = sysDepartproject.getOrgCode();
                String newi = "";
                if (i >= 100 && i < 110) {
                    newi = "B0" + (i - 100);
                }
                if (i >= 110 && i < 200) {
                    newi = "B" + (i - 100);
                }
                if (i >= 200 && i < 210) {
                    newi = "C0" + (i - 200);
                }
                if (i >= 210 && i < 300) {
                    newi = "C" + (i - 200);
                }
                if (i >= 300 && i < 310) {
                    newi = "D0" + (i - 300);
                }
                if (i >= 310 && i < 400) {
                    newi = "D" + (i - 300);
                }
                if (i >= 400 && i < 410) {
                    newi = "E0" + (i - 400);
                }
                if (i >= 410 && i < 500) {
                    newi = "E" + (i - 400);
                }
                if (i >= 500 && i < 510) {
                    newi = "F0" + (i - 500);
                }
                if (i >= 510 && i < 600) {
                    newi = "F" + (i - 500);
                }
                if (i >= 600 && i < 610) {
                    newi = "G0" + (i - 600);
                }
                if (i >= 610 && i < 700) {
                    newi = "G" + (i - 600);
                }
                if (i >= 700 && i < 710) {
                    newi = "H0" + (i - 700);
                }
                if (i >= 710 && i < 800) {
                    newi = "H" + (i - 700);
                }
                if (i >= 800 && i < 810) {
                    newi = "I0" + (i - 800);
                }
                if (i >= 810 && i < 900) {
                    newi = "I" + (i - 800);
                }
                if (i >= 900 && i < 910) {
                    newi = "J0" + (i - 900);
                }
                if (i >= 910 && i < 1000) {
                    newi = "J" + (i - 900);
                }
                orgCode = orgCode.replace(String.valueOf(i),newi);
                sysDepartproject.setOrgCode(orgCode);
                sysDepartprojectService.updateById(sysDepartproject);
            }
        }
    }
}
