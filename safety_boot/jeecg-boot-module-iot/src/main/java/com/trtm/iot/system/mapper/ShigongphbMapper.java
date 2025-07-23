package com.trtm.iot.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.system.entity.Shigongphb;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date:   2021-05-19
 * @Version: V1.0
 */
//@SqlParser(filter=true)
@InterceptorIgnore(tenantLine = "1")
public interface ShigongphbMapper extends BaseMapper<Shigongphb> {

    List<Shigongphb> selectLists(String shebeilist, Integer curid);

    List<Shigongphb> selectListsYJQS(String shebeilist, Integer curid);

    List<Shigongphb> selectListytwnd(String shebeilist, Integer curid);

    List<Shigongphb> selectPhbList(Integer curid);

    @Select("select * from shigongphb where id > #{curid} and statistic = #{statistic} limit 100")
    List<Shigongphb> selectlistjz(Integer curid, int statistic);

    List<Shigongphb> selectListSs(List<String> sbs);

    void updateTjById(Integer id, int i);
}
