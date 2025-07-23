package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swbhzJob2 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzSwCailiaoService swCailiaoService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_SWBHZ_TCP);//柯诸水稳拌合站数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到柯诸水稳拌合站数据推送" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输柯诸水稳拌合站数据推送" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwBases> bhzSwBasesList = bhzSwBasesService.selectListToTCP(shebeilist, curid);
        if (null == bhzSwBasesList || bhzSwBasesList.isEmpty()) {
            log.info(String.format("柯诸水稳拌合站数据推送没有数据" + DateUtils.now()));
            return;
        }


        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (BhzSwBases bhzSwBases : bhzSwBasesList) {
            String guid = bhzSwBases.getGuid();
            String[] pushdata = new String[91];
            for (int i = 0; i < pushdata.length; i++) {
                pushdata[i] = ""; // 将每个元素设置为""
            }
            id = bhzSwBases.getId();
            pushdata[0] = bhzSwBases.getId().toString();// ID(0,"ID","唯一编码"),
            pushdata[1] = bhzSwBases.getShebeibianhao();// MPNUM(1,"MPNUM","设备编号"),
            pushdata[2] = bhzSwBases.getChuliaoshijian();// CLTIME(2,"CLTIME","出料时间"),
            pushdata[3] = bhzSwBases.getBaocunshijian();// CJTIME(3,"CJTIME","采集时间"),
            pushdata[4] = bhzSwBases.getBanheshijian().toString();// JBTIME(4,"JBTIME","搅拌时间"),
            pushdata[5] = "2";// ISP(5,"ISP","配比类型 0-系统录入理论配合比；1-拌和站传理论用量；2-拌和站传理论配合比"),
            pushdata[6] = bhzSwBases.getFormulaNo();// FORLUMA(6,"FORLUMA","配方"),
            pushdata[7] = "柯诸高速";// PROJECTNAME(7,"PROJECTNAME","项目名称"),
//            pushdata[8]= "";// STRENGTHGRADE(8,"STRENGTHGRADE","强度等级"),
//            pushdata[9]= "";// PLACEPART(9,"PLACEPART","浇筑部位"),
//            pushdata[10]= "";// TLD(10,"TLD","坍落度"),
//            pushdata[11]= "";// LQTEM(11,"LQTEM","沥青温度"),
//            pushdata[12]= "";// GLTEM(12,"GLTEM","骨料温度"),
//            pushdata[13]= "";// CLTEM(13,"CLTEM","出料温度"),
//            pushdata[14]= "";// LCTEM(14,"LCTEM","溜槽温度"),
//            pushdata[15]= "";// VOLUME(15,"VOLUME","方量"),
//            pushdata[16]= "";// AR(16,"AR","油石比"),
//            pushdata[17]= "";// LQ(17,"LQ","沥青"),
//            pushdata[18]= "";// GL1(18,"GL1","骨料1"),
//            pushdata[19]= "";// GL2(19,"GL2","骨料2"),
//            pushdata[20]= "";// GL3(20,"GL3","骨料3"),
//            pushdata[21]= "";// GL4(21,"GL4","骨料4"),
//            pushdata[22]= "";// GL5(22,"GL5","骨料5"),
//            pushdata[23]= "";// GL6(23,"GL6","骨料6"),
//            pushdata[24]= "";// GL7(24,"GL7","骨料7"),
//            pushdata[25]= "";// RLGL(25,"RLGL","再生冷料"),
//            pushdata[26]= "";// RRGL(26,"RRGL","再生热料"),
//
//            pushdata[28]= "";// SZ2(28,"SZ2","沙子2"),
//            pushdata[29]= "";// SZ3(29,"SZ3","沙子3"),
//            pushdata[30]= "";// SZ4(30,"SZ4","沙子4"),
//            pushdata[31]= "";// ORE1(31,"ORE1","矿粉1"),
//            pushdata[32]= "";// ORE2(32,"ORE2","矿粉2"),
//            pushdata[33]= "";// ORE3(33,"ORE3","矿粉3"),
//            pushdata[34]= "";// ORE4(34,"ORE4","矿粉4"),
//            pushdata[35]= "";// MH1(35,"MH1","煤灰1"),
//            pushdata[36]= "";// MH2(36,"MH2","煤灰2"),
//            pushdata[37]= "";// MH3(37,"MH3","煤灰3"),
//            pushdata[38]= "";// WJJ1(38,"WJJ1","外加剂1"),
//            pushdata[39]= "";// WJJ2(39,"WJJ2","外加剂2"),
//            pushdata[40]= "";// WJJ3(40,"WJJ3","外加剂3"),
//            pushdata[41]= "";// WJJ4(41,"WJJ4","外加剂4"),
//            pushdata[42]= "";// SN1(42,"SN1","水泥1"),
//            pushdata[43]= "";// SN2(43,"SN2","水泥2"),
//            pushdata[44]= "";// SN3(44,"SN3","水泥3"),
//            pushdata[45]= "";// SN4(45,"SN4","水泥4"),
//            pushdata[46]= "";// SN5(46,"SN5","水泥5"),
//            pushdata[47]= "";// SN6(47,"SN6","水泥6"),
//            pushdata[48]= "";// WATER1(48,"WATER1","水1"),
//            pushdata[49]= "";// WATER2(49,"WATER2","水2"),
//            pushdata[50]= "";// TAR(50,"TAR","理论油石比"),
//            pushdata[51]= "";// TLQ(51,"TLQ","理论沥青"),
//
//            pushdata[53]= "";// TGL2(53,"TGL2","理论骨料2"),
//            pushdata[54]= "";// TGL3(54,"TGL3","理论骨料3"),
//            pushdata[55]= "";// TGL4(55,"TGL4","理论骨料4"),
//            pushdata[56]= "";// TGL5(56,"TGL5","理论骨料5"),
//            pushdata[57]= "";// TGL6(57,"TGL6","理论骨料6"),
//            pushdata[58]= "";// TGL7(58,"TGL7","理论骨料7"),
//            pushdata[59]= "";// TRLGL(59,"TRLGL","理论再生冷料"),
//            pushdata[60]= "";// TRRGL(60,"TRRGL","理论再生热料"),
//
//            pushdata[62]= "";// TSZ2(62,"TSZ2","理论沙子2"),
//            pushdata[63]= "";// TSZ3(63,"TSZ3","理论沙子3"),
//            pushdata[64]= "";// TSZ4(64,"TSZ4","理论沙子4"),
//            pushdata[65]= "";// TORE1(65,"TORE1","理论矿粉1"),
//            pushdata[66]= "";// TORE2(66,"TORE2","理论矿粉2"),
//            pushdata[67]= "";// TORE3(67,"TORE3","理论矿粉3"),
//            pushdata[68]= "";// TORE4(68,"TORE4","理论矿粉4"),
//            pushdata[69]= "";// TMH1(69,"TMH1","理论煤灰1"),
//            pushdata[70]= "";// TMH2(70,"TMH2","理论煤灰2"),
//            pushdata[71]= "";// TMH3(71,"TMH3","理论煤灰3"),
//            pushdata[72]= "";// TWJJ1(72,"TWJJ1","理论外加剂1"),
//            pushdata[73]= "";// TWJJ2(73,"TWJJ2","理论外加剂2"),
//            pushdata[74]= "";// TWJJ3(74,"TWJJ3","理论外加剂3"),
//            pushdata[75]= "";// TWJJ4(75,"TWJJ4","理论外加剂4"),
//            pushdata[76]= "";// TSN1(76,"TSN1","理论水泥1"),
//            pushdata[77]= "";// TSN2(77,"TSN2","理论水泥2"),
//            pushdata[78]= "";// TSN3(78,"TSN3","理论水泥3"),
//            pushdata[79]= "";// TSN4(79,"TSN4","理论水泥4"),
//            pushdata[80]= "";// TSN5(80,"TSN5","理论水泥5"),
//            pushdata[81]= "";// TSN6(81,"TSN6","理论水泥6"),
//            pushdata[82]= "";// TWATER1(82,"TWATER1","理论水1"),
//            pushdata[83]= "";// TWATER2(83,"TWATER2","理论水2"),
//            pushdata[84]= "";// POUR(84,"POUR","浇筑方式"),
//            pushdata[85]= "";// UNIT(85,"UNIT","合同单位"),
//            pushdata[86]= "";// MARK1(86,"MARK1","出料时间戳"),
//            pushdata[87]= "";// MARK2(87,"MARK2","车标识"),
//            pushdata[88]= "";// MARK3(88,"MARK3","盘号"),
//            pushdata[89]= "";// GCDZ(89,"GCDZ","工程地址 "),
            pushdata[90] = "路面标";// HTBH(90,"HTBH","合同编号");


            LambdaQueryWrapper<BhzSwCailiao> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BhzSwCailiao::getBaseGuid, guid);
            List<BhzSwCailiao> swCailiaoList = swCailiaoService.list(queryWrapper);
            if (null != swCailiaoList && !swCailiaoList.isEmpty()) {
                for (BhzSwCailiao swCailiao : swCailiaoList) {
                    //替换
                    if (StringUtils.isBlank(swCailiao.getLilunpb())) {
                        continue;
                    }
                    String llphb = swCailiao.getLilunpb().replace("外掺", "");
                    switch (swCailiao.getCailiaoming()) {
                        case "材料1":
                            pushdata[18] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料1"),
                            pushdata[52] = llphb;
                            ;// TGL1(52,"TGL1","理论骨料1"),
                            break;
                        case "材料2":
                            pushdata[19] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料2"),
                            pushdata[53] = llphb;
                            ;// TGL1(53,"TGL1","理论骨料2"),
                            break;
                        case "材料3":
                            pushdata[20] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料3"),
                            pushdata[54] = llphb;
                            ;// TGL1(54,"TGL1","理论骨料3"),
                            break;
                        case "材料4":
                            pushdata[21] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料4"),
                            pushdata[55] = llphb;
                            ;// TGL1(55,"TGL1","理论骨料4"),
                            break;
                        case "材料5":
                            pushdata[22] = String.valueOf(swCailiao.getShijiyongliang() * 1000);// SZ1(27,"SZ1","沙子1"),
                            pushdata[56] = llphb;// TSZ1(61,"TSZ1","理论沙子1"),
                            break;
                        case "碎石":
                            pushdata[42] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料1"),
                            pushdata[76] = llphb;
                            ;// TGL1(52,"TGL1","理论骨料1"),
                            break;
                        case "水泥":
                            pushdata[42] = String.valueOf(swCailiao.getShijiyongliang() * 1000);
                            ;// GL1(18,"GL1","骨料1"),
                            pushdata[76] = llphb;
                            ;// TGL1(52,"TGL1","理论骨料1"),
                            break;
                    }


                }
            }

            String ps = "*" + StringUtil.join(pushdata, ",") + "*";

            String msg = send(ps);
            if (msg.contains("success")){
                bhzSwBases.setIssend(1);
            }else {
                bhzSwBases.setIssend(2);
            }
            bhzSwBasesService.updateById(bhzSwBases);
            pushandreturnService.saveData(id, ps, selectsysconfigone.getRemark(), msg);
            sysConfigService.updateSysConfig(JobUtil.KZ_SWBHZ_TCP, id);
        }
    }

    private String send(String ps) {
        String msg = "";
        String host = "39.103.168.214"; // 服务器地址
        int port = 10202; // 服务器端口
        try {
            Socket socket = new Socket(host, port); // 创建Socket连接
            InputStream inputStream = socket.getInputStream();
            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();
            // 使用GBK编码写入数据
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "GBK");
            PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
            // 要发送的数据
//            byte[] data = ps.getBytes(); // 将数据转换为字节数组
//            outputStream.write(data); // 发送数据
            printWriter.println(ps);
            printWriter.flush();
            System.out.println("Data sent successfully.");
            // 设置接收数据缓存区
            byte[] buffer = new byte[1024];
            int br = inputStream.read(buffer);
            msg = new String(buffer, 0, br);
            System.out.println("接收到服务端信息：" + msg);
            // 关闭输入流和Socket连接
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            msg = "发送报错";
            e.printStackTrace();
        }
        return msg;
    }

}
