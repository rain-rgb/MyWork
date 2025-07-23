package com.trtm.sy.formtoprint.service;

import com.trtm.sy.formtoprint.entity.DpsJcTestitemtype;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.formtoprint.entity.FormToPrint;

import java.util.List;

/**
 * @Description: 表单打印
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
public interface IDpsJcTestitemtypeService extends IService<DpsJcTestitemtype> {

     public List<FormToPrint> getFormTreeList();

}
