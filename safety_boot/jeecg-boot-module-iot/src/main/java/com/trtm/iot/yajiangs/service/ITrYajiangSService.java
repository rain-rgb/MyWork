package com.trtm.iot.yajiangs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.0
 */
public interface ITrYajiangSService extends IService<TrYajiangS> {


    List<TrYajiangS> selectListTrYajiangS(String syjid);

    IPage<TrYajiangS> selectListPage(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao);


    IPage<TrYajiangS> selectChaobiaoListPage(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao);
    IPage<TrYajiangS> selectChaobiaoListPage1(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao, Integer overproofStatus);

    int updateoverproofStatus(Integer OverproofStatus, String syjid);

    List<TrYajiangSM> findBysyjid(String syjid);

    Integer findXiangMuZSss(List<String> querySheBeiList, String time);

    Integer findYuJingSs(List<String> querySheBeiList, String time);

    Integer findBiHeSs(List<String> querySheBeiList, String time);

    List<TrYajiangS> selectmList(String syjid);

    List<TrYajiangS> selectmnotList(String syjid);

    List<TrYajiangS> selectListbltozl(String shebeino);

}
