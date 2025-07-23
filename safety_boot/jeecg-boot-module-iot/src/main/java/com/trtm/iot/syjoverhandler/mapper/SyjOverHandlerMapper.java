package com.trtm.iot.syjoverhandler.mapper;

import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 试验机处置审核表
 * @Author: jeecg-boot
 * @Date:   2021-12-30
 * @Version: V1.0
 */
public interface SyjOverHandlerMapper extends BaseMapper<SyjOverHandler> {
    /**
     * 根据id查询判断是否存在审核或者处理数据
     */
    public String dataById(String batchno);

    int syjchuZhiAddById(String wtyy, String clfs, String cljg, String syjbatch, String bizPath, String chuzhiren, int status);

    int syjchuZhiUpdateById(String wtyy, String clfs, String cljg, String syjbatch, String bizPath, String chuzhiren, int status);

    int BhzShBoHuiAddById(String jlbh, String syjbatch, String people, int status);

    int BhzShBoHuiUpdateById(String jlbh, String syjbatch, String people, int status);

    int BhzSHAddById(String spyj, String spjg, String syjbatch, String bizPath, String shenpiren, int status);

    int BhzSHUpdateById(String spyj, String spjg, String syjbatch, String bizPath, String shenpiren, int status);

    int BhzZHBSHAddById(String zhbyj, String zhbjg, String syjbatch, String bizPath, String shenpiren, int status);

    int BhzZHBSHUpdateById(String zhbyj, String zhbjg, String syjbatch, String bizPath, String shenpiren, int status);

    int BhzZHBBoHuiAddById(String zhbbh, String syjbatch, String shenpiren, int status);

    int BhzZHBBoHuiUpdateById(String zhbbh, String syjbatch, String shenpiren, int status);

    Boolean updateTableField(@Param("tableName") String tableName, @Param("fieldName")String fieldName, @Param("status")int status,@Param("syjbatch") String syjbatch);
}
