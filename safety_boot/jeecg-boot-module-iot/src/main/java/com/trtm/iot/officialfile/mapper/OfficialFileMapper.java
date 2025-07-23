package com.trtm.iot.officialfile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.officialfile.entity.OfficialFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 公文信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface OfficialFileMapper extends BaseMapper<OfficialFile> {

    @Select(" SELECT " +
            "  t1.id, " +
            "  t1.url, " +
            "  t1.name, " +
            "  t1.departname, " +
            "  t1.fileno, " +
            "  t1.filestyle, " +
            "  t1.sys_org_code, " +
            "  t1.filetime, " +
            "  t1.filetype, " +
            "  t1.description " +
            " FROM " +
            "   official_file t1 " +
            "   JOIN sys_depart t2 ON t1.sys_org_code = t2.org_code " +
            " where " +
            "   t2.org_code LIKE concat(#{orgCode},'%') and filestyle=7 "+
            " ORDER BY filetime desc " +
            " LIMIT 4 ")
    List<Map> events(String orgCode);
    @Select("SELECT " +
            "  count(t1.filestyle) as count, " +
            "  t1.filestyle " +
            "FROM " +
            "   official_file t1 " +
            "   JOIN sys_depart t2 ON t1.sys_org_code = t2.org_code " +
            "where  t2.org_code LIKE concat(#{orgCode},'%') and filestyle < 7  " +
            "GROUP BY t1.filestyle")
    List<Map> archive(String orgCode);

}
