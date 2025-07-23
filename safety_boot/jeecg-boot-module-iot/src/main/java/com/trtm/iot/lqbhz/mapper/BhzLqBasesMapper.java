package com.trtm.iot.lqbhz.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.hntbhz.vo.BhzCementBaseRC;
import com.trtm.iot.lqbhz.entity.BhzLqBasesDayYL;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.entity.BhzLqWarnVO;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.lqbhz.vo.BhzLqCLDC;
import com.trtm.iot.lqbhz.vo.BhzLqTongJi;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 沥青主表
 * @Author: jeecg-boot
 * @Date: 2021-02-22
 * @Version: V1.0
 */

public interface BhzLqBasesMapper extends BaseMapper<BhzLqBases> {

    @Update("update bhz_lq_bases set alertstate = #{alertaste} where guid = #{guid}")
    int updatealertaste(String guid, Integer alertaste);

    /**
     * 根据超标时间修改值
     */
    @Update("update bhz_lq_bases set  timechaobiao=#{time} where guid=#{guid} ")
    void updateLqOverime(int time, String guid);

    /**
     * 根据guid修改混合料类型
     */
    @Update("update bhz_lq_bases set  hunheliaoid=#{hunheliaoid} where guid=#{guid} ")
    void updatehunheliaoidByGuid(String hunheliaoid, String guid);

    /**
     * 根据主表guid查询子表材料信息
     */
    @Select("select * from  bhz_lq_cailiao where  base_guid=#{guid}")
    List<BhzLqCailiao> selectCailiaoList(String guid);

    /**
     * 根据guid修改总产量
     */
    @Update("update bhz_lq_bases set zongchanliang=#{zongchanliang} where guid=#{guid}")
    public void updateZclByGuid(String guid, Double zongchanliang);

    /**
     * 根据guid修改油石比
     */
    @Update("update bhz_lq_bases set youshibi=#{youshibi} where guid=#{guid}")
    public void updateYsbByGuid(String guid, String youshibi);

    /**
     * 根据guid将理论油石比写入数据库
     */
    @Update("update bhz_lq_bases set llysb=#{lilunpb} where guid=#{guid}")
    void updateLlysbByGuid(String lilunpb, String guid);

    /**
     * 修改bhz_lq_bases的数据的状态
     *
     * @param id
     * @param alertstate
     */
    @Update("update bhz_lq_bases set alertstate =#{alertstate} where id=#{id}")
    void updateBaseStatus(Integer id, int alertstate);

    /**
     * 超标检测查询
     *
     * @param id
     * @param alertstate
     * @return
     */
    List<BhzLqBases> selectlqbhzones(Integer id, Integer alertstate);

    List<BhzLqBases> selectlqbhzonesg(Integer id, Integer alertstate, String shebeiNo);

    BhzLqBases queryone(String shebeiNo);

    List<BhzLqBases> selectList1(String shebeiNo, Integer id);


    List<BhzLqBases> selectListTY(String shebeiNo, Integer id);

    List<BhzLqBases> selectListSG(String shebeiNo, Integer id);

    List<BhzLqBases> selectList2(String shebeiNo, Integer id);

    List<BhzLqBases> selectListtoDJ(String shebeiNo, Integer id);

    List<BhzLqBases> selectUpdateList(String shebeiNo);

    List<BhzLqBases> selectListtoHC(String shebeiNo, Integer id);

    List<BhzLqBases> selectcbList(String shebeiNo, Integer id);

    BhzLqWarnVO selectWranCount(String orgCode);

    List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode);

    List<BhzCementWarnVO> selectWranCountByshebeino(String code, int i);

    @Update("update bhz_lq_bases set project_name=#{gcmc} where guid=#{guid}")
    void updateGcmcByGuid(String gcmc, String guid);

    List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode);

    Integer selectBiheCount(String orgCode);

    Integer findYuJingS(List<String> sheBeiNoList);

    Integer findBiHeS(List<String> sheBeiNoList);

    Integer findXiangMuZS(List<String> sheBeiNoList);

    Integer findXiangMuZSs(List<String> sheBeiNoList, String dateNowStr);

    Integer findYuJingSs(List<String> sheBeiNoList, String dateNowStr);

    Integer findBiHeSs(List<String> sheBeiNoList, String dateNowStr);

    Map selectwarnContent(String tableName, String sbjno);

    List<Map> selectProjectList(String sys_org_code, int i);

    Double selectOverCount(@Param("shebeiList") List<String> shebeiList, @Param("tableName") String tableName, @Param("sql") String sql);

    List<BhzLqBases> selectLQBHZ(Integer curid, String shebeilist);

    List<BhzLqBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<BhzLqBases> selectLQBHZRoad(Integer curid, String shebeilist);

    List<BhzLqBasesDayYL> getDosage(String formulaNo, String date);

    @SqlParser(filter = true)
    List<BhzCementBaseRC> selectTongjiData(String shebeilist);

    List<BhzLqTongJi> selectCailiaoUse(String shebei, String start, String end);

    List<BhzLqCLDC> selectdcdata(String shebeibianhao, String chuliaoshijian_begin, String chuliaoshijian_end, String projectName, String hunheliaoid);
}
