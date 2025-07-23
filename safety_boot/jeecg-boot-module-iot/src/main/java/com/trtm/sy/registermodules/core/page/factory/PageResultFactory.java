package com.trtm.sy.registermodules.core.page.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.registermodules.core.page.PageResult;


import java.util.ArrayList;
import java.util.List;

/**
 * 分页的返回结果创建工厂
 * <p>
 * 一般由mybatis-plus的Page对象转为PageResult
 *
 * @author wh
 */
public class PageResultFactory {

    /**
     * 将mybatis-plus的page转成自定义的PageResult，扩展了totalPage总页数
     *
     * @author wh
     */
    public static <T> PageResult<T> createPageResult(Page<T> page) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(page.getRecords());
        pageResult.setTotalRows(Convert.toInt(page.getTotal()));
        pageResult.setPageCurrent(Convert.toInt(page.getCurrent()));
        pageResult.setPageSize(Convert.toInt(page.getSize()));
        pageResult.setTotalPage(
                PageUtil.totalPage(pageResult.getTotalRows(), pageResult.getPageSize()));
        return pageResult;
    }

    /**
     * 将mybatis-plus的page转成自定义的PageResult，扩展了totalPage总页数
     *
     * @author wh
     */
    public static <T> PageResult<T> createPageResult(List<T> rows, Long count, Integer pageSize, Integer pageCurrent) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setTotalRows(Convert.toInt(count));
        pageResult.setPageCurrent(pageCurrent);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPage(PageUtil.totalPage(pageResult.getTotalRows(), pageSize));
        return pageResult;
    }

    public static <T> PageResult<T> createPageResult() {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(new ArrayList<>());
        pageResult.setTotalRows(0);
        pageResult.setPageCurrent(1);
        pageResult.setPageSize(20);
        pageResult.setTotalPage(0);
        return pageResult;
    }

}
