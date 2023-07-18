package com.wr.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wr.common.RedisCache;
import com.wr.common.Result.AjaxResult;
import com.wr.common.annotate.LogStorage;
import com.wr.common.constants.CacheConstants;
import com.wr.common.constants.Constants;
import com.wr.common.utils.IdUtils;
import com.wr.common.utils.SecurityUtils;
import com.wr.domain.LogRegPojo.RegisterUser;
import com.wr.domain.LogRegPojo.LoginUserVo;
import com.wr.domain.SysMenuPojo.SysMenuVo;
import com.wr.service.ILogRegService;
import com.wr.service.ISysConfigService;
import com.wr.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/logReg")
@Api(tags = "登录注册")
public class LogRegController extends BaseController {

    @Autowired
    private ILogRegService iLoginService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService iSysConfigService;

    @Value("${wr-config.captchaType}")
    private String captchaType;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public AjaxResult login(@Validated @RequestBody LoginUserVo loginUser){
        //验证码校验
        String token = iLoginService.login(loginUser.getUserName(),loginUser.getPassword(),loginUser.getCode(),loginUser.getUuid());
        return AjaxResult.success("获取成功",token);
    }

    @LogStorage
    @PostMapping("/register")
    @ApiOperation("用户注册")
    @PreAuthorize("@ac.hasPermi('system:user:list')")
//    @DataSource(DataSourceType.SLAVE)
    public AjaxResult register(@RequestBody RegisterUser registerUser){
        return success(iLoginService.register(registerUser));
    }


    @ApiOperation("获取图形验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode()
    {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = iSysConfigService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String code = null;
        ajax.put("uuid", uuid);
        // 生成验证码
        if ("math".equals(captchaType))
        {
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36, 2);
            code = captcha.text();
            ajax.put("img", captcha.toBase64());
        }
        else if ("char".equals(captchaType))
        {
            SpecCaptcha captcha = new SpecCaptcha(111, 36, 4);
            code = captcha.text();
            ajax.put("img", captcha.toBase64());
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        return ajax;
    }



}
