package com.trtm.iot.zhiliangrenwudan.service;

import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 任务单（制梁）表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-13
 * @Version: V1.0
 */
public interface IZhiliangrenwudanService extends IService<Zhiliangrenwudan> {

    /**
     * 根据制梁台座编号查询
     *
     */
    List<Zhiliangrenwudan> selectone(String taizuono);
    /**
     * 根据制梁台座编号查询
     *
     */
    List<Zhiliangrenwudan> selectone1(String taizuono);
    /**
     * 添加一对多
     *
     */
    public void saveMain(Zhiliangrenwudan zhiliangrenwudan, List<ZhiliangGongxu> zhiliangGongxuList) ;

    public void updateMain(Zhiliangrenwudan zhiliangrenwudan, List<ZhiliangGongxu> zhiliangGongxuList);
    /**
     * 根据生产线查询
     *
     */
    public List<Zhiliangrenwudan> selectones(Integer itemValue,String sysOrgCOde);

    int selectBetlevSum(String betlev);

    Zhiliangrenwudan selectuuid(String uuid);

    List<Zhiliangrenwudan> getzhilianglist();

    Zhiliangrenwudan selectbyorgCode(String sys_depart_orgcode);
}
