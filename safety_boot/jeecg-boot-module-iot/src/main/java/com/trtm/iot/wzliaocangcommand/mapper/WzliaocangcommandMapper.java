package com.trtm.iot.wzliaocangcommand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzliaocangcommand.entity.Wzliaocangcommand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 料仓门禁表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
public interface WzliaocangcommandMapper extends BaseMapper<Wzliaocangcommand> {
    @Update("update wzliaocangcommand set command = 0,isdo = 1,indexs=#{index},onecode=#{code} where id=#{id}")
    int updateone(Integer id,Integer index,String code);

    @Update("update wzliaocangcommand set command = 1,isdo = 1,indexs=#{index},onecode=#{code} where id=#{id}")
    int updateone1(Integer id,Integer index,String code);
}
