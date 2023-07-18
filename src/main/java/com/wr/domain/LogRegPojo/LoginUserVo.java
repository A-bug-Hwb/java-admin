package com.wr.domain.LogRegPojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("用户登录")
public class LoginUserVo {

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotNull
    @NotBlank(message = "用户昵称不能为空")
    private String userName;
    @ApiModelProperty(value = "用户密码", required = true)
    @NotNull
    @NotBlank(message = "用户密码不能为空")
    private String password;
    @ApiModelProperty(value = "验证码", required = true)
    @NotNull
    @NotBlank(message = "验证码不能为空")
    private String code;
    @ApiModelProperty(value = "获取验证码时返回的uuid", required = true)
    private String uuid;
}
