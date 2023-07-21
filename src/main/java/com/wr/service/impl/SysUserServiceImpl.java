package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.common.exception.ServiceException;
import com.wr.common.service.TokenService;
import com.wr.common.utils.BeanUtil;
import com.wr.common.utils.IdUtils;
import com.wr.common.utils.SecurityUtils;
import com.wr.common.utils.StringUtils;
import com.wr.domain.LogRegPojo.LoginUser;
import com.wr.domain.SysRolePojo.SysRoleBo;
import com.wr.domain.SysRolePojo.SysRolePo;
import com.wr.domain.SysUserPojo.*;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import com.wr.mapper.SysUserMapper;
import com.wr.service.ISysRoleService;
import com.wr.service.ISysUserRoleService;
import com.wr.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPo> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private TokenService tokenService;

    @Override
    public SysUserBo findUserByUsername(String username) {
        SysUserBo userBo = null;
        SysUserPo sysUserPo = sysUserMapper.selectOne(Wrappers.lambdaQuery(SysUserPo.class).eq(SysUserPo::getUserName, username).last("limit 1"));
        if (StringUtils.isNotNull(sysUserPo)) {
            userBo = BeanUtil.beanToBean(sysUserPo, new SysUserBo());
            List<SysUserRolePo> list = iSysUserRoleService.list(Wrappers.lambdaQuery(SysUserRolePo.class).eq(SysUserRolePo::getUserId, sysUserPo.getUserId()));
            if (StringUtils.isNotEmpty(list)) {
                List<Long> roleIds = list.stream().map(val -> {
                    return val.getRoleId();
                }).collect(Collectors.toList());
                List<SysRoleBo> sysRoleBos = new ArrayList<>();
                List<SysRolePo> rolePos = iSysRoleService.list(Wrappers.lambdaQuery(SysRolePo.class).in(SysRolePo::getRoleId, roleIds));
                for (SysRolePo rolePo : rolePos) {
                    SysRoleBo sysRoleBo = BeanUtil.beanToBean(rolePo, new SysRoleBo());
                    sysRoleBos.add(sysRoleBo);
                }
                userBo.setSysRoleBos(sysRoleBos);
            }
        }
        return userBo;
    }

    @Override
    public boolean registerUser(SysUserBo user) {
        SysUserPo userPo = BeanUtil.beanToBean(user, new SysUserPo());
        userPo.setUid(IdUtils.getUUID15());
        userPo.setStatus("0");
        if (sysUserMapper.insert(userPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<SysUserVo> getUserList(SysUserDto sysUserDto) {
        return sysUserMapper.getUserList(sysUserDto);
    }

    @Override
    public boolean install(AddSysUserDto addSysUserDto) {
        SysUserPo sysUserPo = BeanUtil.beanToBean(addSysUserDto, new SysUserPo());
        sysUserPo.setUid(IdUtils.getUUID());
        sysUserPo.setCreateTime(new Date());
        sysUserPo.setPassword(SecurityUtils.encryptPassword(sysUserPo.getPassword()));
        if (sysUserMapper.insert(sysUserPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserInfo(UpSysUserDto upSysUserDto) {
        SysUserPo sysUserPo = BeanUtil.beanToBean(upSysUserDto, new SysUserPo());
        sysUserPo.setUpdateBy(SecurityUtils.getUsername());
        sysUserPo.setUpdateTime(new Date());
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            SysUserBo sysUserBo = BeanUtil.beanToBean(sysUserMapper.selectById(SecurityUtils.getUserId()), new SysUserBo());
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUserBo(sysUserBo);
            tokenService.setLoginUser(loginUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPassStu(UpPwdStuDto upPwdStuDto) {
        if (upPwdStuDto.getUserId() == SecurityUtils.getUserId()){
            throw new ServiceException("不能停用当前账户");
        }
        if (SecurityUtils.isAdmin(upPwdStuDto.getUserId())){
            throw new ServiceException("不能停用超级管理员用户");
        }
        SysUserPo sysUserPo = BeanUtil.beanToBean(upPwdStuDto, new SysUserPo());
        sysUserPo.setUpdateBy(SecurityUtils.getUsername());
        sysUserPo.setUpdateTime(new Date());
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePwd(String oldPassword, String newPassword) {
        SysUserPo sysUserPo = new SysUserPo();
        sysUserPo.setUserId(SecurityUtils.getUserId());
        sysUserPo.setPassword(SecurityUtils.encryptPassword(newPassword));
        if (sysUserMapper.updateById(sysUserPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<SysUserVo> selectAllocatedList(SysUserDto sysUserDto) {
        return sysUserMapper.selectAllocatedList(sysUserDto);
    }

    @Override
    public List<SysUserVo> selectUnallocatedList(SysUserDto sysUserDto) {
        return sysUserMapper.selectUnallocatedList(sysUserDto);
    }
}
