package com.wr.auth;


import com.alibaba.fastjson2.JSON;
import com.wr.common.Result.AjaxResult;
import com.wr.common.constants.HttpStatus;
import com.wr.common.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, "请求访问："+request.getRequestURI()+"，认证失败，无法访问系统资源")));
    }
}
