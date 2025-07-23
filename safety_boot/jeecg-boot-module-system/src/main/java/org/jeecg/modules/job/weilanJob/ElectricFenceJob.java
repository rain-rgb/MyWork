package org.jeecg.modules.job.weilanJob;

import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.trtm.iot.frontDeviceWeilan.service.IFrontDeviceWeilanService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.WeiLanUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2021/6/29
 * \* Time: 10:42
 * \* Description:运输车电子围栏(设备类型 35 运输车)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ElectricFenceJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IFrontDeviceWeilanService iFrontDeviceWeilanService;
    @Autowired
    private WeiLanUtil weiLanUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //服务的 sid  当前固定为  362318  服务 name   weilan
        //0 未创建 1创建围栏  2创建终端  3增加监测对象 4检测对象与围栏关系 10 创建失败  20 创建终端失败 30增加监测对象失败 40监测对象与围栏关系失败
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FRONT_DEVICE_WEILAN_CREATE);//创建圆形电子围栏
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到创建电子围栏定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<FrontDeviceWeilan> querylist = iFrontDeviceWeilanService.querylist(0,0,0);//未创建的电子围栏 未删除的
        if (null == querylist || querylist.size() == 0){
            log.info(String.format("暂无未创建电子围栏的数据"));
            //30 监测对象删除失败  20 终端删除失败  10围栏删除失败 3监测对象删除成功 2终端删除成功 1围栏删除成功
            List<FrontDeviceWeilan> querylist1 = iFrontDeviceWeilanService.querylist(3,1,0);//已创建的电子围栏 需要删除的监测对象
            if (null == querylist1 || querylist1.size() == 0) {
                log.info(String.format("暂无需要删除的监测对象"));
                return;
            }
            for (FrontDeviceWeilan frontDeviceWeilan : querylist1) {
                Integer gfid = frontDeviceWeilan.getGfid();
                Integer id = frontDeviceWeilan.getId();
                String sid = frontDeviceWeilan.getSid();
                Integer tid = frontDeviceWeilan.getTid();

                //删除检测对象
                String s = weiLanUtil.PostdelDetectionObject(WeiLanUtil.delDetectionObject, WeiLanUtil.KEY,sid,tid,gfid);
                if(s.equals("500")){
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id);
                    frontDeviceWeilan1.setDelstatus(30);
                    boolean b = iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    log.info(String.format("删除监测对象失败"));
                    continue;
                }else{
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id);
                    frontDeviceWeilan1.setDelstatus(3);
                    frontDeviceWeilan1.setStatus(2);
                    boolean b = iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    if(b){
                        log.info(String.format("删除监测对象成功!"));
                    }

                    //删除终端
                    String s1 = weiLanUtil.PostdelTerminal(WeiLanUtil.delTerminal, WeiLanUtil.KEY, sid, tid);
                    if(s1.equals("500")){
                        FrontDeviceWeilan frontDeviceWeilan2=new FrontDeviceWeilan();
                        frontDeviceWeilan2.setId(id);
                        frontDeviceWeilan2.setDelstatus(20);
                        boolean b1 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan2);
                        log.info(String.format("删除终端失败！"));
                        continue;
                    }else{
                        FrontDeviceWeilan frontDeviceWeilan2=new FrontDeviceWeilan();
                        frontDeviceWeilan2.setId(id);
                        frontDeviceWeilan2.setDelstatus(2);
                        frontDeviceWeilan2.setStatus(1);
                        boolean b1 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan2);
                        log.info(String.format("删除终端成功！"));

                        //删除围栏
                        String s2 = weiLanUtil.PostdelWEILan(WeiLanUtil.delWEILan, WeiLanUtil.KEY, sid, gfid);
                        if(s2.equals("500")){
                            FrontDeviceWeilan frontDeviceWeilan3=new FrontDeviceWeilan();
                            frontDeviceWeilan3.setId(id);
                            frontDeviceWeilan3.setDelstatus(10);
                            boolean b2 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan3);
                            log.info(String.format("删除围栏失败！"));
                            continue;
                        }else{
                            FrontDeviceWeilan frontDeviceWeilan3=new FrontDeviceWeilan();
                            frontDeviceWeilan3.setId(id);
                            frontDeviceWeilan3.setDelstatus(1);
                            frontDeviceWeilan3.setStatus(0);
                            boolean b2 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan3);
                            log.info(String.format("删除围栏成功！"));
                        }

                    }

                }
            }
        }
        int id = 0;
        for (FrontDeviceWeilan selectlqbhzone:querylist) {
            String center = selectlqbhzone.getCenter();//中点经纬度
            Integer radius = selectlqbhzone.getRadius();//半径
            String name = selectlqbhzone.getName();
            String sid = selectlqbhzone.getSid();
            String descs = selectlqbhzone.getDescs();
            Integer id1 = selectlqbhzone.getId();
            Integer s = weiLanUtil.PostaddWEILan(WeiLanUtil.addWEILan, WeiLanUtil.KEY, sid, name, center, radius, descs);
            if(s==500){
                FrontDeviceWeilan frontDeviceWeilan=new FrontDeviceWeilan();
                frontDeviceWeilan.setId(id1);
                frontDeviceWeilan.setStatus(10);
                iFrontDeviceWeilanService.updateById(frontDeviceWeilan);
                log.info(String.format("创建电子围栏失败!"));
                continue;
            }else{
                FrontDeviceWeilan frontDeviceWeilan=new FrontDeviceWeilan();
                frontDeviceWeilan.setId(id1);
                frontDeviceWeilan.setGfid(s);
                frontDeviceWeilan.setStatus(1);
                boolean b = iFrontDeviceWeilanService.updateById(frontDeviceWeilan);
                if(b){
                    log.info(String.format("创建电子围栏成功!"));
                }
                //创建终端
                Map map = weiLanUtil.PostaddTerminal(WeiLanUtil.addTerminal, WeiLanUtil.KEY, sid, name, descs);
                if(map==null){
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id1);
                    frontDeviceWeilan1.setStatus(20);
                    iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    log.info(String.format("创建终端失败!"));
                    continue;
                }else{
                    Integer tid = (Integer) map.get("tid");
                    String name1 = (String) map.get("name");
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id1);
                    frontDeviceWeilan1.setStatus(2);
                    frontDeviceWeilan1.setTname(name1);
                    frontDeviceWeilan1.setTid(tid);
                    boolean b1 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    if(b1){
                        log.info(String.format("创建终端成功!"));
                    }
                }
                //增加监测对象
                FrontDeviceWeilan byId = iFrontDeviceWeilanService.getById(id1);
                Integer gfid = byId.getGfid();
                Integer tid = byId.getTid();
                if (tid==0){
                    log.info(String.format("未获取对的参数去添加监测对象!"));
                    continue;
                }
                String s1 = weiLanUtil.PostaddDetectionObject(WeiLanUtil.addDetectionObject, WeiLanUtil.KEY, sid, gfid, tid);
                if(s1.equals("200")){
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id1);
                    frontDeviceWeilan1.setStatus(3);
                    boolean b1 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    if(b1){
                        log.info(String.format("增加监测对象成功!"));
                    }
                }else{
                    FrontDeviceWeilan frontDeviceWeilan1=new FrontDeviceWeilan();
                    frontDeviceWeilan1.setId(id1);
                    frontDeviceWeilan1.setStatus(30);
                    boolean b1 = iFrontDeviceWeilanService.updateById(frontDeviceWeilan1);
                    log.info(String.format("增加监测对象失败!"));
                }

            }

        }
    }
}
