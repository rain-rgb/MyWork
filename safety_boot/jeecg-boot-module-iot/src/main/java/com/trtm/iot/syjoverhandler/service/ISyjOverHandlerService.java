package com.trtm.iot.syjoverhandler.service;

import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 试验机处置审核表
 * @Author: jeecg-boot
 * @Date:   2021-12-30
 * @Version: V1.0
 */
public interface ISyjOverHandlerService extends IService<SyjOverHandler> {


    SyjOverHandler selectOneSyj(String syjid);
    SyjOverHandler selectone(String syjid);

    Integer syjCZAddOrUpDate(String wtyy, String clfs, String cljg, String syjbatch, String bizPath, String people, int i);

    Integer supervisorBohui(String jlbh, String syjbatch, String people, int i);

    Integer supervisorAddOrUpdate(String spyj, String spjg, String syjbatch, String bizPath, String people, int i);

    Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String syjbatch, String bizPath, String people, int i);

    Integer headquartersBohui(String zhbbh, String syjbatch, String people, int i);

    Boolean updateTableField(String tableName, String fieldName, int status, String syjbatch);
}
