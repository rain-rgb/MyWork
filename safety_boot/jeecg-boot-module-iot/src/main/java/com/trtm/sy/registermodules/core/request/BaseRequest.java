package com.trtm.sy.registermodules.core.request;


import java.io.Serializable;

/**
 * 接口请求模板公共基类
 *
 * @author wh
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
    private String searchBeginTime;

    /**
     * 结束时间
     */
    private String searchEndTime;

    /**
     * 分页：每页大小（默认20）
     */
    private Integer pageSize = 20;

    /**
     * 分页：第几页（从1开始）
     */
    private Integer pageCurrent = 1;

    /**
     * 唯一请求号
     */
    private String requestNo;

    /**
     * 搜索内容，通用查询条件的值
     */
    private String searchText;

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }


    public String getSearchBeginTime() {
        return searchBeginTime;
    }

    public void setSearchBeginTime(String searchBeginTime) {
        this.searchBeginTime = searchBeginTime;
    }

    public String getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(String searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
