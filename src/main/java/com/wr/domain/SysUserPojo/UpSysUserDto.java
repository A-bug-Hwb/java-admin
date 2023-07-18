package com.wr.domain.SysUserPojo;

import lombok.Data;

@Data
public class UpSysUserDto {


    private Long userId;
    private String nickName;
    private String userName;
    private String sex;
    private String mobile;
    private String mailbox;
    private String status;
    private String remark;
}
