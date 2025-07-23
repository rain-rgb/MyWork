package com.trtm.iot.weiyan.mapper;

import java.util.List;

import com.trtm.iot.weiyan.vo.IotWallRock;
import com.trtm.iot.weiyan.vo.IotWallRockTemporary;
import com.trtm.iot.weiyan.vo.IotWeiYanNormCfg;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.weiyan.entity.WeiyanBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 围岩量测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface WeiyanBaseMapper extends BaseMapper<WeiyanBase> {
    public  List<IotWallRock>  selectWallRockOne(IotWallRock iotWallRock);

    public   boolean updateById(IotWallRock iotWallRock);

    /**
     *  查找方法
     * @param iot
     * @return
     */
    List<IotWeiYanNormCfg> select(IotWeiYanNormCfg iot);

    /**
     * @param iotWallRockTemporary
     * @return
     */
    public List<IotWallRockTemporary> select2(IotWallRockTemporary iotWallRockTemporary);
    /**
     *
     *
     * @param iotWallRockTemporary
     * @return
     */
    public boolean insert1(IotWallRockTemporary iotWallRockTemporary);
    public   boolean update(IotWallRockTemporary iotWallRockTemporary);
}
