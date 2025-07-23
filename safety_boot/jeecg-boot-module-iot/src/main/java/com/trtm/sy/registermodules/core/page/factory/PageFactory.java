package com.trtm.sy.registermodules.core.page.factory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.registermodules.core.request.BaseRequest;
import com.trtm.sy.registermodules.core.tool.HttpServletUtil;


import javax.servlet.http.HttpServletRequest;

/**
 * 分页参数快速获取
 *
 * @author wh
 */
public class PageFactory {

    /**
     * 每页大小（默认20）
     */
    private static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    /**
     * 第几页（从1开始）
     */
    private static final String PAGE_NO_PARAM_NAME = "pageCurrent";

    /**
     * 默认分页，在使用时PageFactory.defaultPage会自动获取pageSize和pageNo参数
     */
    public static <T> Page<T> defaultPage() {

        int pageSize = 20;
        int pageCurrent = 1;

        HttpServletRequest request = HttpServletUtil.getRequest();

        //每页条数
        String pageSizeString = request.getParameter(PAGE_SIZE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageSizeString)) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        //第几页
        String pageCurrentString = request.getParameter(PAGE_NO_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageCurrentString)) {
            pageCurrent = Integer.parseInt(pageCurrentString);
        }

        return new Page<>(pageCurrent, pageSize);
    }

    /**
     * 从baseRequest中获取分页参数
     */
    public static <T> Page<T> defaultPage(BaseRequest baseRequest) {
        int pageSize = 20;
        int pageNo = 1;

        if (ObjectUtil.isNotEmpty(baseRequest)) {
            pageNo = baseRequest.getPageCurrent() == null ? pageNo : baseRequest.getPageCurrent();
            pageSize = baseRequest.getPageSize() == null ? pageSize : baseRequest.getPageSize();
        }
        return new Page<>(pageNo, pageSize);
    }

}
