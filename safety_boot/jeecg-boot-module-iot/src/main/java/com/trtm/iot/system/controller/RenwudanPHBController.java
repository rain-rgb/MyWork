package com.trtm.iot.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.entity.WZCaiLiao;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="任务单和施工配合比")
@RestController
@RequestMapping("/system/phbrwd")
@Slf4j
public class RenwudanPHBController {
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;


    @AutoLog(value = "施工配合比-看板接口")
    @ApiOperation(value="施工配合比-看板接口", notes="施工配合比-看板接口")
    @GetMapping(value = "/pldinfo")
    public Result<?> queryPageList4( HttpServletRequest req, String sysOrgCode,String sbjno,Integer Station) {
        ShebeiInfo sbj = shebeiInfoService.selectshebeione(sbjno);
        Map<String, Object> res = new HashMap<>();
        res.put("sbname",sbj.getSbname());
        // 拌合站数据查询
//        QueryWrapper<BhzCementBase> bhzqw = new QueryWrapper();
//        bhzqw.select("product_datetime,formula_no");
//        bhzqw.eq("shebei_no",sbjno);
//        bhzqw.orderByDesc("product_datetime");
//        bhzqw.last("limit 1");
//        Map<String, Object> bhz = bhzCementBaseService.getMap(bhzqw);
//        String sgphbcode = (String) bhz.get("formula_no");
        // 配料单数据查询
        LambdaQueryWrapper<Shigongphb> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(!StringUtils.isEmpty(sbjno),Shigongphb::getShebeibianhao,sbjno)
                .eq(!StringUtils.isEmpty(Station),Shigongphb::getStation,Station)
                .orderByDesc(Shigongphb::getDattim);
        queryWrapper.last("limit 1");
        List<Shigongphb> phblist = shigongphbService.list(queryWrapper);
        if(phblist.size()<=0){ return  Result.OKs(res); }
        Shigongphb phb = phblist.get(0);
        if(!StringUtils.isEmpty(phb.getRenwudan())){
            Bhzrenwudan rwd = bhzrenwudanService.selectstation1(phb.getRenwudan(),phb.getSysOrgCode());
            res.put("rwd",rwd);
        }else if(!StringUtils.isEmpty(phb.getCode())){
            Bhzrenwudan rwd = bhzrenwudanService.selectstation1(phb.getCode(),phb.getSysOrgCode());
            res.put("rwd",rwd);
        }else{
            res.put("rwd",new Bhzrenwudan());
        }
         WZCaiLiao[] arrCailiao = arrCailiao(phb);
         res.put("arrCailiao",arrCailiao);
         res.put("phb",phb);

        return  Result.OKs(res);
    }

    public WZCaiLiao[] arrCailiao( Shigongphb phb){
          if(phb != null){
              WZCaiLiao m1 = new WZCaiLiao();
              m1.setName("水泥1");
              m1.setGuige(phb.getM1());
              m1.setLlyongliang(String.format("%.2f",(phb.getU1()!= null?phb.getU1():0)));
              m1.setSjyongliang(String.format("%.2f",phb.getRu1()!= null?phb.getRu1():0));


              WZCaiLiao m13 = new WZCaiLiao();
              m13.setName("水泥2");
              m13.setGuige(phb.getM13());
              m13.setLlyongliang(String.format("%.2f",(phb.getU13()!= null?phb.getU13():0)));
              m13.setSjyongliang(String.format("%.2f",(phb.getRu13()!= null?phb.getRu13():0)));
//              m13.setHslv(String.valueOf(phb.getMc13()));
//              m13.setHsliang(String.valueOf(phb.getMcl13()));


              WZCaiLiao m2 = new WZCaiLiao();
              m2.setName("粉煤灰");
              m2.setGuige(phb.getM2());
              m2.setLlyongliang(String.format("%.2f",phb.getU2()!= null?phb.getU2():0));
              m2.setSjyongliang(String.format("%.2f",(phb.getRu2()!= null?phb.getRu2():0)));


              WZCaiLiao m3 = new WZCaiLiao();
              m3.setName("矿粉");
              m3.setGuige(phb.getM3());
              m3.setLlyongliang(String.format("%.2f",phb.getU3()!= null?phb.getU3():0));
              m3.setSjyongliang(String.format("%.2f",(phb.getRu3()!= null?phb.getRu3():0)));


              WZCaiLiao m4 = new WZCaiLiao();
              m4.setName("骨料1");
              m4.setGuige(phb.getM4());
              m4.setLlyongliang(String.format("%.2f",(phb.getU4()!= null?phb.getU4():0)));
              m4.setSjyongliang(String.format("%.2f",(phb.getRu4()!= null?phb.getRu4():0)));
              m4.setHslv( String.format("%.2f", phb.getMc4()!= null?phb.getMc4():0));
              m4.setHsliang(String.valueOf(Math.round( phb.getMcl4()!= null?phb.getMcl4():0)));

              WZCaiLiao m5 = new WZCaiLiao();
              m5.setName("骨料2");
              m5.setGuige(phb.getM5());
              m5.setLlyongliang(String.format("%.2f",(phb.getU5()!= null?phb.getU5():0)));
              m5.setSjyongliang(String.format("%.2f",(phb.getRu5()!= null?phb.getRu5():0)));
              m5.setHslv(String.format("%.2f", phb.getMc5()!= null?phb.getMc5():0));
              m5.setHsliang(String.valueOf(Math.round( phb.getMcl5()!= null?phb.getMcl5():0)));

              WZCaiLiao m6 = new WZCaiLiao();
              m6.setName("骨料3");
              m6.setGuige(phb.getM6());
              m6.setLlyongliang(String.format("%.2f",(phb.getU6()!= null?phb.getU6():0)));
              m6.setSjyongliang(String.format("%.2f",(phb.getRu6()!= null?phb.getRu6():0)));
              m6.setHslv(String.format("%.2f", phb.getMc6()!= null?phb.getMc6():0));
              m6.setHsliang(String.valueOf(Math.round( phb.getMcl6()!= null?phb.getMcl6():0)));

              WZCaiLiao m7 = new WZCaiLiao();
              m7.setName("骨料4");
              m7.setGuige(phb.getM7());
              m7.setLlyongliang(String.format("%.2f",(phb.getU7()!= null?phb.getU7():0)));
              m7.setSjyongliang(String.format("%.2f",(phb.getRu7()!= null?phb.getRu7():0)));
              m7.setHslv(String.format("%.2f", phb.getMc7()!= null?phb.getMc7():0));
              m7.setHsliang(String.valueOf(Math.round( phb.getMcl7()!= null?phb.getMcl7():0)));

              WZCaiLiao m12 = new WZCaiLiao();
              m12.setName("骨料5");
              m12.setGuige(phb.getM12());
              m12.setLlyongliang(String.format("%.2f",(phb.getU12()!= null?phb.getU12():0)));
              m12.setSjyongliang(String.format("%.2f",(phb.getRu12()!= null?phb.getRu12():0)));
              m12.setHslv(String.format("%.2f", phb.getMc12()!= null?phb.getMc12():0));
              m12.setHsliang(String.valueOf(Math.round( phb.getMcl12()!= null?phb.getMcl12():0)));


              WZCaiLiao m8 = new WZCaiLiao();
              m8.setName("外加剂1");
              m8.setGuige(phb.getM8());
              m8.setLlyongliang(String.valueOf(phb.getU8()!= null?phb.getU8():0));
              m8.setSjyongliang(String.valueOf(phb.getRu8()!= null?phb.getRu8():0));
              m8.setHslv(String.format("%.2f", phb.getMc8()!= null?phb.getMc8():0));
              m8.setHsliang(String.format("%.2f", phb.getMcl8()!= null?phb.getMcl8():0));

              WZCaiLiao m9 = new WZCaiLiao();
              m9.setName("外加剂2");
              m9.setGuige(phb.getM9());
              m9.setLlyongliang(String.valueOf(phb.getU9()!= null?phb.getU9():0));
              m9.setSjyongliang(String.valueOf(phb.getRu9()!= null?phb.getRu9():0));
              m9.setHslv(String.format("%.2f", phb.getMc9()!= null?phb.getMc9():0));
              m9.setHsliang(String.format("%.2f", phb.getMcl9()!= null?phb.getMcl9():0));

              WZCaiLiao m10 = new WZCaiLiao();
              m10.setName("外加剂3");
              m10.setGuige(phb.getM10());
              m10.setLlyongliang(String.valueOf(phb.getU10()!= null?phb.getU10():0));
              m10.setSjyongliang(String.valueOf(phb.getRu10()!= null?phb.getRu10():0));
              m10.setHslv(String.format("%.2f", phb.getMc10()!= null?phb.getMc10():0));
              m10.setHsliang(String.format("%.2f", phb.getMcl10()!= null?phb.getMcl10():0));

              WZCaiLiao m11 = new WZCaiLiao();
              m11.setName("水");
              m11.setGuige(phb.getM11());
              m11.setLlyongliang(String.valueOf(Math.round(phb.getU11()!= null?phb.getU11():0)));
              m11.setSjyongliang(String.valueOf(Math.round(phb.getRu11()!= null?phb.getRu11():0)));
//              m11.setHslv(String.valueOf(phb.getMc11()));
//              m11.setHsliang(String.valueOf(phb.getMcl11()));

              WZCaiLiao cailiao[] = {m1,m13,m2 ,m3 ,m4 ,m5 ,m6 ,m7 ,m12,m8 ,m9 ,m10,m11};
              List<WZCaiLiao> tmp = new ArrayList<WZCaiLiao>();
              for (WZCaiLiao str : cailiao) {
                  if (StringUtil.isNotEmpty(str.getGuige())) {
                      tmp.add(str);
                  }
              }
              cailiao = tmp.toArray(new WZCaiLiao[0]);
              return cailiao;

          }
        return new WZCaiLiao[0];
    }

}
