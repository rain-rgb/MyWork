package com.trtm.iot.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 设备审核表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
public interface ShebeiInfoMapper extends BaseMapper<ShebeiInfo> {

    @Select("select sbjno from shebei_info  where  sys_org_code like '%#{sys_org_code}%'")
    public List<ShebeiInfo> arrayOneshebei(@Param("sys_org_code") String sys_org_code);

    @Select("select testid from shebei_info  order by testid desc  limit 1")
    public List<ShebeiInfo> arrayOneshebeis();

    @Select("SELECT * FROM `shebei_info` where `shebei_status` = 1")
    public List<ShebeiInfo> SBList();

    @Select("SELECT * FROM `shebei_info` where sbjno = #{sbjno}")
    public ShebeiInfo SBJWD(@Param("sbjno") String sbjno);

    @Update("update `shebei_info` set status1=#{status} where sbjno = #{sbjno}")
    public Boolean updatestatus1(@Param("sbjno") String sbjno,@Param("status") Integer status);

    @Update("update `shebei_info` set status=#{status} where sbjno = #{sbjno}")
    Boolean updatestatus(@Param("sbjno") String sbjno,@Param("status") Integer status);

    @Select("SELECT * FROM `shebei_info` where `shebei_status` = 1 and sbtype = 0")
    List<ShebeiInfo> selectshebei();

    @Select("select sbjno from shebei_info  where  sys_org_code like concat(#{sys_org_code},'%')")
    public List<ShebeiInfo> arrayshebei(@Param("sys_org_code") String sys_org_code);

    List<ShebeiInfo> selectbhzone(Integer sbtype);

    ShebeiInfo selectshebeione(String sbjno);

    List<ShebeiInfo> selectbhzonelist();

    List<ShebeiInfo> shebeilists(int sbtype, String orgcode);

    @Select("SELECT sbjno FROM shebei_info WHERE sbname like '%瑞苍%' and sbtype = 0")
    List<ShebeiInfo> selectBhzshebei();

    List<ShebeiInfo> selectLists(String orgcode, String curid);

    @Select("SELECT sbjno FROM shebei_info WHERE sys_org_code = #{orgcode} and sbtype = #{sbtype}")
    List<String> selectShebeiList(String orgcode,int sbtype);

    @Select("SELECT sbjno FROM shebei_info WHERE sys_org_code like concat(#{orgcode},'%') and sbtype = #{sbtype}")
    List<String> selectSbjnoListLikeOrgcode(String orgcode,int sbtype);


    List<ShebeiInfo> queryShebeis(String code);//

    String querySheBeiMC(String sheBeiNo);//

    String queryShebeiCode(String orgCode);//

    List<String> selectShebeNoiList(String orgCode);


    List<String> findSheBeis(String orgCode);

    List<String> findSBeis();

    List<Map> getDataByOrgCode(@Param("list") List<String> list);

    @Select("select * from shebei_info where sys_org_code like #{orgCode}")
    List<ShebeiInfo> selectbyorgcode(String orgCode);

    List<String> selectSBListByCodeList(String strsToList1);


    String getEquipType(Integer sbtype);
}
