package com.wr.common.page;

import cn.hutool.core.convert.Convert;
import com.wr.common.utils.ServletUtils;

/**
 * 表格数据处理
 *
 * @author wr
 */
public class TableSupport
{
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_CURRENT = "pageCurrent";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageCurrent(Convert.toInt(ServletUtils.getParameter(PAGE_CURRENT), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
