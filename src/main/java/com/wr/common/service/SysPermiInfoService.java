package com.wr.common.service;

import com.wr.common.utils.SecurityUtils;
import com.wr.domain.SysUserPojo.SysUserBo;
import com.wr.service.ISysMenuService;
import com.wr.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SysPermiInfoService {

    @Autowired
    private ISysMenuService iSysMenuService;

    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUserBo user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getUserId()))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(iSysRoleService.getRoleKeys(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUserBo user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getUserId()))
        {
            perms.add("*:*:*");
        }
        else
        {
            perms.addAll(iSysMenuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }


}
