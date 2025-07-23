package com.trtm.sy.formtoprint.mapper;

import java.util.List;

import com.trtm.sy.formtoprint.entity.FormToPrint;
import com.trtm.sy.formtoprint.entity.DpsJcTestitemtype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 表单打印
 * @Author: jeecg-boot
 * @Date:   2022-09-05
 * @Version: V1.0
 */
public interface DpsJcTestitemtypeMapper extends BaseMapper<DpsJcTestitemtype> {

   List<DpsJcTestitemtype> getFormTreeList();
}
