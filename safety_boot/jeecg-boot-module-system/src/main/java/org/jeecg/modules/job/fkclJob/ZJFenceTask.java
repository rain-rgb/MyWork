package org.jeecg.modules.job.fkclJob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.dianzhiweilan.entity.Dianzhiweilan;
import com.trtm.iot.dianzhiweilan.service.IDianzhiweilanService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.trtm.iot.util.GPSUtil.gps84_To_Gcj02;

/**
 * 设备实施数据更新
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZJFenceTask implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IFrontDeviceRealdataService frontDeviceRealdataService;

    @Autowired
    private ICarmilesService carmilesService;

    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Autowired
    private IDianzhiweilanService dianzhiweilanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FKYSC_XZDZWL);//新增电子围栏判断方法
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到金途车联,车辆实时信息更新定时任务的配置信息" + DateUtils.now());
            return;
        }
        List<String> shebeilist = dianzhiweilanService.selectshebei();//电子围栏设备编号
        if (shebeilist.size() > 0){
            for (String shebei :shebeilist){
                FrontDeviceRealdata queryone = frontDeviceRealdataService.queryone(shebei);//车辆当前位置
                List<Map<String, Object>> areas = dianzhiweilanService.selectjinduweidu(shebei,1);//一级电子围栏的全部坐标
                List<Map<String, Object>> areas2 = dianzhiweilanService.selectjinduweidu(shebei,2);//二级电子围栏的全部坐标
                if (areas.size() > 0 && areas2.size() > 0){
                    // 判断车位置是否进入电子范围内
                    double[] doubles = gps84_To_Gcj02(queryone.getLatitude(),queryone.getLongitude());
                    double latitude1 = doubles[0];
                    double longitude1 = doubles[1];
                    Boolean flag =isPointInPolygon(longitude1, latitude1,areas);
                    Boolean flag2 =isPointInPolygon(longitude1, latitude1,areas2);
                    if (flag){
                        queryone.setStatus(3);
                    }else {
                        if (flag2){
                            queryone.setStatus(1);
                        }else{
                            queryone.setStatus(5);
                        }
                    }
                    frontDeviceRealdataService.updateById(queryone);
                    log.info("新增电子围栏判断成功！！！");
                }else {
                    log.info("请检查该设备电子围栏"+shebei);
                }
            }
        }else {
            log.info("暂无检测数据！！！");
        }
    }

    /**
     * 传入某个坐标判断是否在指定区域（这里使用4个点做区域范围，此算法可以是5个点，6个点，甚至更多！）
     */
    private Boolean isPointInPolygon(double px, double py,List<Map<String, Object>> areas){

        /**
         * 经度x,纬度y
         * 通常读法是读作“经纬度”（经度在前，纬度在后；但一般书写是纬度在前，经度在后，也有人带上单位把经度写在前面，总之这里哪个是x，哪个是y，自己注意一下对应好，千万别弄错！自己就中过坑！！）
         * 【重点：点的顺序必须顺时钟或者逆时钟添加，不可随便排列！！否则当点数多了的时候，同样的点可能出现不同的形状，导致定位出错！】
         */
        //组成多边形!!
        //List<Map<String, Object>> areas = systemService.findForJdbc("select jindu,weidu from WZfencepoints where shebeibianhao='"+shebeino+"'");
        if (areas.size()>0) {
            ArrayList<Double> polygonXA = new ArrayList<Double>();
            ArrayList<Double> polygonYA = new ArrayList<Double>();
            for (Map<String, Object> map : areas) {
                System.out.println(map);
                polygonXA.add((Double) map.get("jindu"));
                polygonYA.add((Double) map.get("weidu"));
            }
            //	FenceTask otg = new FenceTask(); otg.
            //true在区域内，false不在区域内
            return inPointInPolygon(px, py, polygonXA, polygonYA);
        }else {
            return true;
        }
    }


    /**
     * 判断目标点是否在多边形内(由多个点组成)
     *
     * @param px
     *            目标点的经度坐标
     * @param py
     *            目标点的纬度坐标
     * @param polygonXA
     *            多边形的经度坐标集合
     * @param polygonYA
     *            多边形的纬度坐标集合
     * @return
     */
    public boolean inPointInPolygon(double px, double py, ArrayList<Double> polygonXA, ArrayList<Double> polygonYA) {
        boolean isInside = false;
        double ESP = 1e-9;
        int count = 0;
        double linePoint1x;
        double linePoint1y;
        double linePoint2x = 180;
        double linePoint2y;

        linePoint1x = px;
        linePoint1y = py;
        linePoint2y = py;

        for (int i = 0; i < polygonXA.size() - 1; i++) {
            double cx1 = polygonXA.get(i);
            double cy1 = polygonYA.get(i);
            double cx2 = polygonXA.get(i + 1);
            double cy2 = polygonYA.get(i + 1);
            // 如果目标点在任何一条线上
            if (isPointOnLine(px, py, cx1, cy1, cx2, cy2)) {
                return true;
            }
            // 如果线段的长度无限小(趋于零)那么这两点实际是重合的，不足以构成一条线段
            if (Math.abs(cy2 - cy1) < ESP) {
                continue;
            }
            // 第一个点是否在以目标点为基础衍生的平行纬度线
            if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                // 第二个点在第一个的下方,靠近赤道纬度为零(最小纬度)
                if (cy1 > cy2)
                    count++;
            }
            // 第二个点是否在以目标点为基础衍生的平行纬度线
            else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                // 第二个点在第一个的上方,靠近极点(南极或北极)纬度为90(最大纬度)
                if (cy2 > cy1)
                    count++;
            }
            // 由两点组成的线段是否和以目标点为基础衍生的平行纬度线相交
            else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                count++;
            }
        }
        if (count % 2 == 1) {
            isInside = true;
        }
        return isInside;
    }

    /**
     * 是否有 横断（横断的意思就是 4(n)个点的8(n*2)个坐标 的8(n*2)个数字(每个点都有横纵坐标)只要有任意两个数字(横和横比较纵和纵比较)相等，就是横断的意思）
     * 参数为四个点的坐标
     *
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @param px3
     * @param py3
     * @param px4
     * @param py4
     * @return
     */
    public boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4,
                               double py4) {
        boolean flag = false;
        double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
        if (d != 0) {
            double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
            double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
            if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 目标点是否在目标区域边上
     *
     * @param px0
     *            目标点的经度坐标
     * @param py0
     *            目标点的纬度坐标
     * @param px1
     *            目标线的起点(终点)经度坐标
     * @param py1
     *            目标线的起点(终点)纬度坐标
     * @param px2
     *            目标线的终点(起点)经度坐标
     * @param py2
     *            目标线的终点(起点)纬度坐标
     * @return
     */
    public boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2) {
        boolean flag = false;
        double ESP = 1e-9;// 无限小的正数
        if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0)
                && ((py0 - py1) * (py0 - py2) <= 0)) {
            flag = true;
        }
        return flag;
    }
    public double Multiply(double px0, double py0, double px1, double py1, double px2, double py2) {
        return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
    }
}