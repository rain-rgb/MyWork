package org.jeecg.modules.job.SysprojectJob;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.pmscomwbs.entity.PmsComWbs;
import com.trtm.iot.pmscomwbs.service.IPmsComWbsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
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
 * \* Date: 2021/7/16
 * \* Time: 10:35
 * \* Description: 中交象山疏港高速wbs    （原 海德wbs  对接 pms_com_wbs 表 海德wbs数据 对接到 sys_depart_project）
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SysProjectJob implements Job {
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IPmsComWbsService pmsComWbsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZHONGJIAO_XIANGSHAN_SHUGANG);//中交象山疏港高速wbs
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到疏港高速定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<PmsComWbs> queryselectlist = pmsComWbsService.queryselectlist(curid, 0);
        if (null == queryselectlist || queryselectlist.size() == 0) {
            log.info(String.format("暂无中交象山疏港高速wbs的数据需要更新" + DateUtils.now()));
            return;
        }
        int id = 0;

        for (PmsComWbs pmsComWbs : queryselectlist) {
            SysDepartproject sysDepartproject=new SysDepartproject();
            Integer ids = pmsComWbs.getIds();
            id=ids;
            String sectid = pmsComWbs.getSectid();//标段
            String wbsIsbn = pmsComWbs.getWbsIsbn();//子节点
            String wbsParent = pmsComWbs.getWbsParent();//父节点
            String wbsName = pmsComWbs.getWbsName();//wbs名称
            Integer wbsLevel = pmsComWbs.getWbsLevel();//级别
            String wbsZhBegi = pmsComWbs.getWbsZhBegi();//开始桩号
            String wbsZhEnd = pmsComWbs.getWbsZhEnd();//结束桩号
            Integer sortorder = pmsComWbs.getSortorder();//排序
            String wbsMapno = pmsComWbs.getWbsMapno();//备注
            if(wbsIsbn.length()==3&&wbsParent==null){
                sysDepartproject.setMemo(wbsIsbn);
                if(sectid.equals("8ae4829a6045082f0160450a6ef801dd")){ //TJ-1 A05A01A01A01A01A01
                    sysDepartproject.setParentId("bcac15a0796d45cea1bdccc825016027");
                }else if(sectid.equals("4046e47d70cd41aa0170d7ee372e1bf1")){//TJ-2 A05A01A01A01A01A02
                    sysDepartproject.setParentId("54126a5cd612499d9b8a8e37800cbe18");
                }else if(sectid.equals("4046e69470cc190b0170cc2b42c90369")){//TJ-3 A05A01A01A01A02A01
                    sysDepartproject.setParentId("26b8f5d263344b248b1a8674fd350b0c");
                }else if(sectid.equals("4046e47d70cd41aa0170d7f07f691bf2")){//TJ-4 A05A01A01A01A02A02
                    sysDepartproject.setParentId("c0a6f990104d4383afa99fbcbc7449eb");
                }else if(sectid.equals("4046e47d70cd41aa0170d7f69d341bf5")){//TJ-2A A05A01A01A01A01A03
                    sysDepartproject.setParentId("24a63063a75c408c8f81f1ad4284e356");
                }
            }else{
                SysDepartproject selectmemo = sysDepartprojectService.selectmemo(wbsParent,sectid);//根据子节点以及所属分部查询
                if(selectmemo!=null){
                    String id1 = selectmemo.getId();
                    sysDepartproject.setParentId(id1);
                    sysDepartproject.setMemo(wbsIsbn);
                }else{
                    log.info(String.format("中交象山疏港高速wbs父子节点查询失败" + DateUtils.now()+"目标"+ids));
                    return;
                }
            }
            sysDepartproject.setOrgCategory(String.valueOf(wbsLevel));
            sysDepartproject.setOrgType(String.valueOf(wbsLevel));
            if(wbsZhBegi!=null&&wbsZhEnd!=null){
                sysDepartproject.setAddress(wbsZhBegi+" - "+wbsZhEnd);
            }
            sysDepartproject.setDepartName(wbsName);
            sysDepartproject.setDepartOrder(sortorder);
            sysDepartproject.setPici(1);
            if(sectid.equals("8ae4829a6045082f0160450a6ef801dd")){ //TJ-1 A05A01A01A01A01A01
                sysDepartproject.setOrgCodes("A05A01A01A01A01A01");
            }else if(sectid.equals("4046e47d70cd41aa0170d7ee372e1bf1")){//TJ-2 A05A01A01A01A01A02
                sysDepartproject.setOrgCodes("A05A01A01A01A01A02");
            }else if(sectid.equals("4046e69470cc190b0170cc2b42c90369")){//TJ-3 A05A01A01A01A02A01
                sysDepartproject.setOrgCodes("A05A01A01A01A02A01");//
            }else if(sectid.equals("4046e47d70cd41aa0170d7f07f691bf2")){//TJ-4 A05A01A01A01A02A02
                sysDepartproject.setOrgCodes("A05A01A01A01A02A02");
            }else if(sectid.equals("4046e47d70cd41aa0170d7f69d341bf5")){//TJ-2A A05A01A01A01A01A03
                sysDepartproject.setOrgCodes("A05A01A01A01A01A03");
            }
//            if(wbsMapno!=null){
//                sysDepartproject.setAddress(wbsMapno);
//            }
            sysDepartproject.setDescription(sectid);
            sysDepartproject.setDelFlag("0");
            JSONObject formData = new JSONObject();
            formData.put("parentId", sysDepartproject.getParentId());
            String[] codeArray = (String[]) FillRuleUtil.executeRule(FillRuleConstant.DEPART, formData);
            //update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
            sysDepartproject.setOrgCode(codeArray[0]);
            SysDepartproject selectmemo = sysDepartprojectService.selectmemo(wbsIsbn,sectid);
            if(selectmemo!=null){
                log.info(String.format("中交象山疏港高速wbs已经存在" + DateUtils.now()+"目标"+ids));
              continue;
            }else{
                boolean save = sysDepartprojectService.save(sysDepartproject);
                if(save){
                    log.info(String.format("中交象山疏港高速wbs更新成功" + DateUtils.now()+"目标"+ids));
                    pmsComWbsService.updateids(ids,1);
                }else{
                    log.info(String.format("中交象山疏港高速wbs更新失败" + DateUtils.now()+"目标"+ids));
                    return;
                }
            }

        }
        sysConfigService.updateSysConfig(JobUtil.ZHONGJIAO_XIANGSHAN_SHUGANG, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
