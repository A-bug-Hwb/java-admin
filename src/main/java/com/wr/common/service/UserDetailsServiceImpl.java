package com.wr.common.service;

import com.wr.common.exception.ServiceException;
import com.wr.common.utils.StringUtils;
import com.wr.domain.LogRegPojo.LoginUser;
import com.wr.domain.SysUserPojo.SysUserBo;
import com.wr.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * 用户登录认证信息查询
 *
 * @author
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private ISysUserService iUserService;

    @Autowired
    private SysPermiInfoService getPermiInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<String> Permissions = new HashSet<String>();
        // 根据用户名查询用户是否存在
        SysUserBo userBo = iUserService.findUserByUsername(username);
        if (StringUtils.isNull(userBo)) {
            // 用户不存在
            throw new ServiceException("登录用户：" + username + " 不存在");
        }else if(!userBo.getStatus().equals("0")){
            throw new ServiceException("登录用户：" + username + " 账号被停用");
        }else if (StringUtils.isEmpty(userBo.getSysRoleBos())){
            throw new ServiceException("登录用户：" + username + " 没有登录权限");
        }else {
            // 用户存在，继续查询用户所拥有的所有权限
            Permissions = getPermiInfoService.getMenuPermission(userBo);
        }
//        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
//        if (Permissions != null && Permissions.size() > 0) {
//            // 遍历权限集合，将权限标识符添加到安全认证中心
//            for (String  nameStr : Permissions) {
//                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(nameStr);
//                grantedAuthorities.add(simpleGrantedAuthority);
//            }
//        }
        return createLoginUser(userBo,Permissions);
    }

    public UserDetails createLoginUser(SysUserBo userBo, Set<String> Permissions )
    {
        return new LoginUser(userBo.getUserId(),userBo, Permissions);
    }
}