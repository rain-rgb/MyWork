package com.trtm.iot.chaoshengbo.mapper;

import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSyjbsjs;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: chaoshengbo_syjbsj
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface ChaoshengboSyjbsjMapper extends BaseMapper<ChaoshengboSyjbsj> {


    List<ChaoshengboSyjbsj> selectLists(String shebeino, Integer id);

    void insertjbsj(ChaoshengboSyjbsjs chaoshengboSyjbsj);

    @Update("update chaoshengbo_syjbsj set level = #{level} , file = #{file} , file_info = #{fileInfo} where id = #{id} ")
    void updateByIdToYb(Integer level, String file, String fileInfo, Integer id);
}
