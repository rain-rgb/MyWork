package com.trtm.sy.formtoprint.entity;

import lombok.Data;

/**
 * @author lis1
 * @date 2022/9/7
 * @time 9:37
 */
@Data
public class FormToPrint {


    private String resultId;

    private String resultName;

    private String isParent;

    private String parentId;

    private String parentName;

    private ChildrenForm children;

}
