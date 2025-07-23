package com.trtm.iot.ycltesttaizhang.mapper;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.ycltesttaizhang.entity.YclTestTaizhang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: ycl_test_taizhang
 * @Author: jeecg-boot
 * @Date: 2023-05-15
 * @Version: V1.0
 */
public interface YclTestTaizhangMapper extends BaseMapper<YclTestTaizhang> {

    List<YclTestTaizhang> getList(String time, String orgCode);

    List<YclTestTaizhang> getHgList(String time, String orgCode);

    void updateByPici(String cailiaoname, String guige, String nodetype, String gongyingshang, Date jinchangtime,
                      String cunfangplace, String shuliang, String usepart, String sysOrgCode, Date createTime,
                      String reslut, String zjpdf, String cjreslut, String cjpdf, String pici);
    @Update("update ycl_test_taizhang set zgzt=#{zgzt} where pici=#{pici}")
    void updateZgztByPici(String zgzt,String pici);


    int getBhCount(String time, String orgCode);
}
