package com.wr.controller;

import com.wr.common.Result.AjaxResult;
import com.wr.common.utils.BeanUtil;
import com.wr.domain.SysMenuPojo.*;
import com.wr.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService iSysMenuService;

    @ApiOperation("获取菜单路由信息")
    @GetMapping("/getRouters")
    public AjaxResult getRouters()
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuTreeByUserId(getUserId());
        return success(iSysMenuService.buildMenus(menus));
    }

    @ApiOperation("获取菜单列表")
    @PreAuthorize("@ac.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenuDto sysMenuDto)
    {
        return success(iSysMenuService.selectMenuList(sysMenuDto, getUserId()));
    }

    @ApiOperation("菜单添加")
    @PreAuthorize("@ac.hasPermi('system:menu:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AddSysMenuDto addSysMenuDto)
    {
        if (iSysMenuService.install(addSysMenuDto)){
            return success("添加成功");
        }
        return error("添加失败");
    }

    @ApiOperation("根据菜单id获取菜单详情")
    @PreAuthorize("@ac.hasPermi('system:menu:edit')")
    @GetMapping("/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(BeanUtil.beanToBean(iSysMenuService.getById(menuId),new SysMenuVo()));
    }

    @ApiOperation("菜单修改")
    @PreAuthorize("@ac.hasPermi('system:menu:edit')")
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody UpSysMenuDto upSysMenuDto)
    {
        if (iSysMenuService.update(upSysMenuDto)){
            return success("修改成功");
        }
        return error("修改失败");
    }

    @ApiOperation("菜单删除")
    @PreAuthorize("@ac.hasPermi('system:menu:edit')")
    @DeleteMapping("/delete/{menuId}")
    public AjaxResult delete(@PathVariable Long menuId)
    {
        if (iSysMenuService.deleteMenu(menuId)){
            return success("删除成功");
        }
        return error("当前菜单下有子菜单，不允许删除");
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenuDto sysMenuDto)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(sysMenuDto, getUserId());
        return success(iSysMenuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation("根据角色ID查询菜单下拉树结构")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable Long roleId)
    {
        List<SysMenuVo> menus = iSysMenuService.selectMenuList(new SysMenuDto(),getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", iSysMenuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", iSysMenuService.buildMenuTreeSelect(menus));
        return ajax;
    }
}
