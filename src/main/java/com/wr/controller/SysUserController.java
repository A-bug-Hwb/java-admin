package com.wr.controller;

import com.alibaba.fastjson.JSONObject;
import com.wr.common.Result.AjaxResult;
import com.wr.common.page.TableDataInfo;
import com.wr.common.service.SysPermiInfoService;
import com.wr.common.utils.BeanUtil;
import com.wr.common.utils.SecurityUtils;
import com.wr.common.utils.StringUtils;
import com.wr.domain.LogRegPojo.LoginUser;
import com.wr.domain.SysRolePojo.SysRoleVo;
import com.wr.domain.SysUserPojo.*;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户信息")
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private SysPermiInfoService sysPermiInfoService;
    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysUserRoleService iSysUserRoleService;


    @GetMapping("/getInfo")
    @ApiOperation("获取登录用户信息")
    public AjaxResult getUserInfo(){

        SysUserBo userBo = getLoginUser().getUserBo();
        SysUserVo userVo = BeanUtil.beanToBean(userBo,new SysUserVo());
        // 角色集合
        Set<String> roles = sysPermiInfoService.getRolePermission(userBo);
        // 权限集合mailbox
        Set<String> permissions = sysPermiInfoService.getMenuPermission(userBo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userVo);
        jsonObject.put("roles", roles);
        jsonObject.put("permissions", permissions);
        return AjaxResult.success(jsonObject);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public TableDataInfo list(SysUserDto sysUserDto)
    {
        startPage();
        return getDataTable(iSysUserService.getUserList(sysUserDto));
    }
    @GetMapping("/{userId}")
    @ApiOperation("查询用户详细")
    public AjaxResult list(@PathVariable Long userId)
    {
        return success(BeanUtil.beanToBean(iSysUserService.getById(userId),new SysUserVo()));
    }

    @PostMapping("/add")
    @ApiOperation("新增用户")
    public AjaxResult add(@RequestBody AddSysUserDto addSysUserDto)
    {
        if (StringUtils.isNotNull(iSysUserService.findUserByUsername(addSysUserDto.getUserName()))){
            return error("用户名"+addSysUserDto.getUserName()+"已存在");
        }
        if (iSysUserService.install(addSysUserDto)){
            return success("添加成功");
        }
        return error("添加失败");
    }

    @PutMapping("/edit")
    @ApiOperation("修改用户信息")
    public AjaxResult edit(@RequestBody UpSysUserDto upSysUserDto)
    {
        if (getUserId() !=upSysUserDto.getUserId() && SecurityUtils.isAdmin(upSysUserDto.getUserId())){
            return error("不允许操作超级管理员用户");
        }else if (StringUtils.isNull(iSysUserService.findUserByUsername(upSysUserDto.getUserName()))){
            return error("用户名"+upSysUserDto.getUserName()+"已存在");
        }
        return success(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @DeleteMapping("/delete/{userId}")
    @ApiOperation("删除用户")
    public AjaxResult delete(@PathVariable List<Long> userId)
    {
        return success(iSysUserService.removeByIds(userId));
    }

    @PutMapping("/resetPwd")
    @ApiOperation("用户密码重置")
    public AjaxResult resetPwd(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        if (SecurityUtils.isAdmin(upPwdStuDto.getUserId())){
            return error("不允许操作超级管理员用户");
        }
        return success(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @PutMapping("/changeStatus")
    @ApiOperation("用户状态修改")
    public AjaxResult changeStatus(@RequestBody UpPwdStuDto upPwdStuDto)
    {
        return success(iSysUserService.updateUserPassStu(upPwdStuDto));
    }

    @GetMapping("/getProfile")
    @ApiOperation("个人中心信息")
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        SysUserVo user = BeanUtil.beanToBean(loginUser.getUserBo(),new SysUserVo());
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", iSysRoleService.selectUserRoleGroup(getUserId()));
        return ajax;
    }
    @PutMapping("/upProfile")
    @ApiOperation("修改用户信息")
    public AjaxResult upProfile(@RequestBody UpSysUserDto upSysUserDto)
    {
        return success(iSysUserService.updateUserInfo(upSysUserDto));
    }

    @PutMapping("/upProfilePwd")
    @ApiOperation("修改个人用户密码")
    public AjaxResult upProfilePwd(String oldPassword,String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        if (iSysUserService.updatePwd(oldPassword,newPassword)){
            return success("修改密码成功");
        }
        return error("修改密码失败");
    }

    @GetMapping("/authRole/{userId}")
    @ApiOperation("查询已授权角色")
    public AjaxResult authRole(@PathVariable Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUserVo user = BeanUtil.beanToBean(iSysUserService.getById(userId),new SysUserVo());
        List<SysRoleVo> roles = iSysRoleService.selectRolePermissionByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SecurityUtils.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.getRoleKey().equals("admin")).collect(Collectors.toList()));
        return ajax;
    }

    @PutMapping("/upAuthRole")
    @ApiOperation("更新授权角色")
    public AjaxResult upAuthRole(Long userId, Long[] roleIds)
    {
        if (iSysUserRoleService.insertUserAuth(userId, roleIds)){
            return success("授权成功");
        }
        return error("授权失败");
    }
}
