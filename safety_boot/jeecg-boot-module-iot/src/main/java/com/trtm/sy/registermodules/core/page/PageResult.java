package com.trtm.sy.registermodules.core.page;



import com.trtm.sy.registermodules.core.page.factory.PageResultAbstract;


import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 * @author wh
 */
public class PageResult<T> extends PageResultAbstract implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 第几页
     */
    private Integer pageCurrent = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 20;

    /**
     * 总页数
     */
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    private Integer totalRows = 0;

    /**
     * 结果集
     */
    private List<T> rows;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
