package com.trtm.sy.sysbjdjz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sysb.entity.SyDpsJcShebei;
import com.trtm.sy.sysb.service.ISyDpsJcShebeiService;
import com.trtm.sy.sysbjdjz.entity.SbJianDingJiaoZhunResponse;
import com.trtm.sy.sysbjdjz.entity.SyDpsJcShebeiJiandingjiaozhun;
import com.trtm.sy.sysbjdjz.mapper.SyDpsJcShebeiJiandingjiaozhunMapper;
import com.trtm.sy.sysbjdjz.service.ISyDpsJcShebeiJiandingjiaozhunService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: sy_dps_jc_shebei_jiandingjiaozhun
 * @Author: jeecg-boot
 * @Date: 2023-10-17
 * @Version: V1.0
 */
@Service
public class SyDpsJcShebeiJiandingjiaozhunServiceImpl extends ServiceImpl<SyDpsJcShebeiJiandingjiaozhunMapper, SyDpsJcShebeiJiandingjiaozhun> implements ISyDpsJcShebeiJiandingjiaozhunService {

    @Autowired
    private ISyDpsJcShebeiService shebeiService;

    @Override
    public IPage<SbJianDingJiaoZhunResponse> getJdJz(String sheBeiIds, String startTime, String endTime, Integer type, Integer pageNo, Integer pageSize) {
        Page<SbJianDingJiaoZhunResponse> page = new Page<>(pageNo, pageSize);
        String[] strings = sheBeiIds.split(",");
        List<String> list = Arrays.asList(strings);
        return this.baseMapper.getJdJz(list, startTime, endTime, type, page);
    }

    @Override
    @Transactional
    public void addOrUpdate(SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun) throws ParseException {
        if (oConvertUtils.isEmpty(syDpsJcShebeiJiandingjiaozhun.getId())) {
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            syDpsJcShebeiJiandingjiaozhun.setChuangjianren(user.getId());
            syDpsJcShebeiJiandingjiaozhun.setChuangjianriqi(DateUtils.getDate("yyyy-MM-dd"));
            this.save(syDpsJcShebeiJiandingjiaozhun);
        } else {
            SyDpsJcShebeiJiandingjiaozhun jiandingjiaozhun = this.getById(syDpsJcShebeiJiandingjiaozhun.getId());
            jiandingjiaozhun.setJiandingjiaozhunbeizhu(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunbeizhu());
            jiandingjiaozhun.setJiandingjiaozhundanwei(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhundanwei());
            jiandingjiaozhun.setJiandingjiaozhunjieguo(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunjieguo());
            jiandingjiaozhun.setJiandingjiaozhunleibie(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunleibie());
            jiandingjiaozhun.setJiandingjiaozhunren(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunren());
            jiandingjiaozhun.setJiandingjiaozhunriqi(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunriqi());
            this.updateById(jiandingjiaozhun);
        }
        SyDpsJcShebei shebei = shebeiService.getOne(Wrappers.lambdaQuery(new SyDpsJcShebei())
                .eq(SyDpsJcShebei::getId, syDpsJcShebeiJiandingjiaozhun.getShebeiid())
                .eq(SyDpsJcShebei::getShebeiisdel, 0));
        if (0 == syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunleibie()) {
            shebei.setShebeijiandingdanwei(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhundanwei());
            shebei.setShebeizuijinjiandingriqi(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunriqi());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunriqi());
            Integer Shebeijiandingzhouqi=num(shebei.getShebeijiaozhunzhouqi());
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.MONTH,Shebeijiandingzhouqi );
            date = cl.getTime();
            shebei.setShebeixiacijiaozhunriqi(new SimpleDateFormat("yyyy-MM-dd").format(date));
        } else if (1 == syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunleibie()) {
            shebei.setShebeijiaozhundanwei(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhundanwei());
            shebei.setShebeizuijinjiaozhunriqi(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunriqi());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(syDpsJcShebeiJiandingjiaozhun.getJiandingjiaozhunriqi());
            Integer Shebeijiandingzhouqi=num(shebei.getShebeijiandingzhouqi());
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.MONTH,Shebeijiandingzhouqi );
            date = cl.getTime();
            shebei.setShebeixiacijiaozhunriqi(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        shebeiService.updateById(shebei);
    }

    public  Integer num(String name) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Pattern pattern = Pattern.compile(".*\\d+.*");
        if (pattern.matcher(name).matches()) {
            Matcher m = p.matcher(name);
            String sort=m.replaceAll("").trim();
            Integer t1=Integer.valueOf(sort);
            return t1;
        }else {
            return null;
        }

    }
}
