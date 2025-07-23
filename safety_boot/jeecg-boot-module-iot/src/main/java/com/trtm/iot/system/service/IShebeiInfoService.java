package com.trtm.iot.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备审核表
 * @Author: jeecg-boot
 * @Date: 2021-03-15
 * @Version: V1.0
 */
public interface IShebeiInfoService extends IService<ShebeiInfo> {

    public List<ShebeiInfo> arrayOneshebei(String sys_org_code);

    public List<ShebeiInfo> usershebeiList(String sys_org_code, List list);

    List<ShebeiInfo> usershebeiListByname(String sys_org_code, List sbtype);
    List<ShebeiInfo> usershebeiListByname(String sys_org_code, List sbtype,String name);


    public List<ShebeiInfo> arrayOneshebeis();

    /**
     * 根据设备编号以及类型查询设备
     */
    public List<ShebeiInfo> arrayOneshebeis(List<String> shebei, Integer sbtype);

    /**
     * 根据设备编号查询设备中是否存在这个数据
     *
     * @param sbjno
     * @return
     */
    ShebeiInfo selectshebeione(String sbjno);

    /**
     * 查询出已审核的设备信息
     */
    List<ShebeiInfo> SBlist();

    /**
     * 根据设备编号查询
     */
    ShebeiInfo SBJWD(String sbjno);

    /**
     * 根据设备编号修改推送状态
     */
    Boolean updatestatus1(String sbjno, Integer status);

    List<ShebeiInfo> selectshebei();

    List<ShebeiInfo> selectbhzone(Integer sbtype);

    List<ShebeiInfo> shebeilist(int sblx, String sysOrgCode);

    public List<ShebeiInfo> arrayshebei(@Param("sys_org_code") String sys_org_code);

    Map<String, Object> selectshebeiones(String shebeibianhao);

    List<ShebeiInfo> selectbhzonelist();

    List<ShebeiInfo> usershebeiLists(String sys_depart_orgcode);
    List<ShebeiInfo> usershebeiLists(String sys_depart_orgcode,String name);

    List<ShebeiInfo> shebeilists(int sbtype, String orgcode);

    List<ShebeiInfo> selectBhzshebei();

    List<ShebeiInfo> selectLists(String orgcode, String curid);

    List<String> selectShebeiList(String sysOrgCode, int sbtype);

    List<String> selectSbjnoListLikeOrgcode(String sysOrgCode, int sbtype);

    List<String> selectShebeiNoList(String orgCode);

    List<Map> getDataToInitPM(String code, int[] nums);

    String selectSbnameBySbno(String shebeibianhao);

    List<ShebeiInfo> getDataToInitCrew(String code, int[] nums);

    List<ShebeiInfo> selectbyorgcode(String orgCode);

    void upadteTuisongid(String sbid, Integer tsid);

    List<String> selectSBListByCodeList(String strsToList1);

    List<ShebeiInfo> selecegcsb(int sbtype, String orgcode);

    Boolean updatestatus(String sbjno, Integer status);

    String getEquipType(Integer sbtype);
}
