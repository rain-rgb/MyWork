package com.trtm.iot.zhilianggongxu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 制梁工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-22
 * @Version: V1.0
 */
public interface ZhiliangGongxuMapper extends BaseMapper<ZhiliangGongxu> {

    @Delete("delete from zhiliang_gongxu where uuid =#{mainId}")
    public boolean deleteByMainId(@Param("mainId") String mainId);

    @Update("update zhiliang_gongxu set isdel=1 where uuid =#{uuid}")
    public boolean updateone(String uuid);

    List<ZhiliangGongxu> selectgongxu(String uuid);

    List<ZhiliangGongxu> selectgongxuPinmin(String uuid);

    ZhiliangGongxu selectuuid(String uuid, int xuhao);

    List<ZhiliangGongxu> selectuuidlist(String uuid, Integer xuhao);

}
