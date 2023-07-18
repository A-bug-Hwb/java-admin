package com.wr.controller;

import com.github.pagehelper.PageInfo;
import com.wr.common.Result.AjaxResult;
import com.wr.common.constants.HttpStatus;
import com.wr.common.page.PageDomain;
import com.wr.common.utils.PageUtils;
import com.wr.common.page.TableDataInfo;
import com.wr.common.page.TableSupport;
import com.wr.common.utils.SecurityUtils;
import com.wr.domain.LogRegPojo.LoginUser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BaseController {


    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Long total = new PageInfo(list).getTotal();
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        rspData.setSize(pageDomain.getPageSize().toString());
        rspData.setCurrent(pageDomain.getPageCurrent().toString());
        rspData.setPages( String.valueOf(total / pageDomain.getPageSize() + (total % pageDomain.getPageSize() != 0 ? 1 : 0)) );
        return rspData;
    }















    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

}
