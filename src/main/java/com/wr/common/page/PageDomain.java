package com.wr.common.page;

/**
 * 分页数据
 *
 * @author wr
 */
public class PageDomain
{
    /** 当前记录起始索引 */
    private Integer pageCurrent;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 分页参数合理化 */
    private Boolean reasonable = true;


    public Integer getPageCurrent()
    {
        return pageCurrent;
    }

    public void setPageCurrent(Integer pageCurrent)
    {
        this.pageCurrent = pageCurrent;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public Boolean getReasonable()
    {
        if (reasonable == null)
        {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable)
    {
        this.reasonable = reasonable;
    }
}
