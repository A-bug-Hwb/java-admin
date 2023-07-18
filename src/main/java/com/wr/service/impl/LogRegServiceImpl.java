package com.wr.service.impl;

import com.wr.common.RedisCache;
import com.wr.common.annotate.AuthenticationContextHolder;
import com.wr.common.constants.CacheConstants;
import com.wr.common.exception.ServiceException;
import com.wr.common.service.TokenService;
import com.wr.common.utils.StringUtils;
import com.wr.domain.LogRegPojo.LoginUser;
import com.wr.domain.LogRegPojo.RegisterUser;
import com.wr.domain.SysUserPojo.SysUserBo;
import com.wr.service.ILogRegService;
import com.wr.service.ISysConfigService;
import com.wr.service.ISysUserService;
import com.wr.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class LogRegServiceImpl implements ILogRegService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService iUserService;

    @Autowired
    private ISysConfigService iSysConfigService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public String login(String username, String password, String code, String uuid) {

        boolean captchaEnabled = iSysConfigService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(code, uuid);
        }
        Authentication authentication = null;
        try {
            // 用户验证
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new ServiceException("用户名或密码错误");
            } else {
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }


    @Override
    public boolean register(RegisterUser registerUser) {
        String username = registerUser.getUsername();
        String password = registerUser.getPassword();
        String confirmPassword = registerUser.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("两次密码不一样");
        } else if (StringUtils.isNotNull(iUserService.findUserByUsername(username))) {
            throw new ServiceException("保存用户'" + username + "'失败，注册账号已存在");
        } else {
            SysUserBo user = new SysUserBo();
            user.setUserName(username);
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = iUserService.registerUser(user);
            if (!regFlag) {
                throw new ServiceException("注册失败,请联系系统管理人员");
            }
        }
        return true;
    }


    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new ServiceException("请填写验证码");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException("验证码错误");
        }
    }
}
