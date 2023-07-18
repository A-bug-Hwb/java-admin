package com.wr.domain.SysUserPojo;

import lombok.Data;

@Data
public class AddSysUserDto {
    private String nickName;
    private String userName;
    private String sex;
    private String mobile;
    private String mailbox;
    private String password;
    private String status;
    private String remark;
}
