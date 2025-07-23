package com.trtm.iot.czsh.mapper;

import java.util.List;

import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
public interface BhzCementOverHandlerMapper extends BaseMapper<BhzCementOverHandler> {
    /**
     * 根据id添加审核信息信息
     */
    public int shenHeAddById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren);
    /**
     * 根据id修改审核信息
     */
    public int shenHeUpdateById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren);
    /**
     * 根据id添加处置信息
     */
    public int chuZhiAddById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren);
    /**
     * 根据id修改处置信息
     */
    public int chuZhiUpdateById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren);





    /**
     * 根据id查询判断是否存在审核或者处理数据
     */
    public String dataById(String hntbatch);


    List<BhzCementOverHandler> listToday(String shebeilist);

    int BhzchuZhiAddById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);

    int BhzchuZhiUpdateById(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status);

    int BhzSHAddById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);

    int BhzSHUpdateById(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status);

    int BhzZHBSHAddById(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status);

    int BhzZHBSHUpdateById(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status);

    int BhzShBoHuiAddById(String jlbh, String hntbatch, String people, int status);

    int BhzShBoHuiUpdateById(String jlbh, String hntbatch,  String people, int status);

    int BhzZHBBoHuiAddById(String zhbbh, String hntbatch,   String shenpiren, int status);

    int BhzZHBBoHuiUpdateById(String zhbbh, String hntbatch, String shenpiren, int status);
}
