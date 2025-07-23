package com.trtm.sy.formtoprint.service.impl;

import com.trtm.sy.formtoprint.entity.DpsJcTestitemtype;
import com.trtm.sy.formtoprint.entity.FormToPrint;
import com.trtm.sy.formtoprint.entity.ChildrenForm;
import com.trtm.sy.formtoprint.entity.ParentForm;
import com.trtm.sy.formtoprint.mapper.DpsJcTestitemtypeMapper;
import com.trtm.sy.formtoprint.service.IDpsJcTestitemtypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 表单打印
 * @Author: jeecg-boot
 * @Date: 2022-09-05
 * @Version: V1.0
 */
@Service
public class DpsJcTestitemtypeServiceImpl extends ServiceImpl<DpsJcTestitemtypeMapper, DpsJcTestitemtype> implements IDpsJcTestitemtypeService {
    @Autowired
    private DpsJcTestitemtypeMapper dpsJcTestitemtypeMapper;

    @Override
    public List<FormToPrint> getFormTreeList() {
        List<DpsJcTestitemtype> list = dpsJcTestitemtypeMapper.getFormTreeList();
        List<FormToPrint> formToPrintList = new ArrayList<>();
        List<FormToPrint> resultList = new ArrayList<>();
        ArrayList<ParentForm> parentList = new ArrayList<>();
        for (DpsJcTestitemtype ftp : list) {
            if ("true".equals(ftp.getIsParent()) || ftp.getParentId() == null) {
                parentList.add(new ParentForm(ftp.getResultId(), ftp.getResultName()));
            }
            FormToPrint formToPrint = new FormToPrint();
            BeanUtils.copyProperties(ftp,formToPrint);
            formToPrintList.add(formToPrint);
        }

        //&& ftp2.getParentId() != null && !"".equals(ftp2.getParentId())
        for (FormToPrint ftp2 : formToPrintList) {
            if ("false".equals(ftp2.getIsParent())) {
                ChildrenForm children = new ChildrenForm();
                children.setId(ftp2.getResultId());
                children.setName(ftp2.getResultName());
                ftp2.setChildren(children);
                for (ParentForm pf : parentList) {
                    if (pf.getParentId().equals(ftp2.getParentId())) {
                        ftp2.setParentName(pf.getParentName());
                        break;
                    }
                }
                resultList.add(ftp2);
            }
        }
        return resultList;
    }
}
