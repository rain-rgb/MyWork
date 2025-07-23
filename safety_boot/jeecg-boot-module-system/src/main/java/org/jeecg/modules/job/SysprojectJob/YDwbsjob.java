package org.jeecg.modules.job.SysprojectJob;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.ydwbs.entity.Ydwbs;
import com.trtm.iot.ydwbs.service.IYdwbsService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/9/14
 * \* Time: 17:16
 * \* Description:
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YDwbsjob implements Job {
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IYdwbsService iYdwbsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<Ydwbs> list = iYdwbsService.getList(0);
        for (Ydwbs ydwbs : list) {
            SysDepartproject sysDepartproject=new SysDepartproject();
            String code = ydwbs.getCode();//条目编码
            String contId = ydwbs.getContId();//
            String id = ydwbs.getId();//条目ID主键唯一
            String isLeaf = ydwbs.getIsLeaf();
            String name = ydwbs.getName();//条目名称
            String nodetype = ydwbs.getNodetype();
            String pic = ydwbs.getPic();//图号
            String pid = ydwbs.getPid();//父节点ID
            String sort = ydwbs.getSort();
            String stack = ydwbs.getStack();//桩号
            Integer status = ydwbs.getStatus();
            if(code.length()==1){
                sysDepartproject.setParentId("2db8ddf2e3fa410aa367846e429c32b2");
                sysDepartproject.setMemo(id);
                sysDepartproject.setOrgType("1");
            }else{
                SysDepartproject selectmemo = sysDepartprojectService.selectmemos(pid);//根据子节点以及所属分部查询
                if(selectmemo!=null){
                    String id1 = selectmemo.getId();
                    String orgType = selectmemo.getOrgType();
                    Integer orgType1=Integer.parseInt(orgType)+1;
                    sysDepartproject.setOrgType(String.valueOf(orgType1));
                    sysDepartproject.setParentId(id1);
                    sysDepartproject.setMemo(id);
                }else{
                    log.info(String.format("义东高速wbs父子节点查询失败" + DateUtils.now()+"目标"+code));
                    return;
                }
            }
            sysDepartproject.setDepartName(name);
            sysDepartproject.setPici(0);
            sysDepartproject.setDelFlag("0");
            JSONObject formData = new JSONObject();
            String parentId = sysDepartproject.getParentId();
            formData.put("parentId", parentId);
            String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
            //update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
            sysDepartproject.setOrgCode(codeArray[0]);
            SysDepartproject selectmemo = sysDepartprojectService.selectmemos(id);
            if(selectmemo!=null){
                log.info(String.format("义东高速wbs已经存在" + DateUtils.now()+"目标"+id));
                continue;
            }else{
                boolean save = sysDepartprojectService.save(sysDepartproject);
                if(save){
                    log.info(String.format("义东高速wbs更新成功" + DateUtils.now()+"目标"+id));
                    Ydwbs ydwbs1=new Ydwbs();
                    ydwbs1.setId(id);
                    ydwbs1.setStatus(1);
                    iYdwbsService.updateById(ydwbs1);
                }else{
                    log.info(String.format("义东高速wbs更新失败" + DateUtils.now()+"目标"+id));
                    return;
                }
            }
        }
    }
}
