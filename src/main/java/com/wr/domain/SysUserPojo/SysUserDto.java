package com.wr.domain.SysUserPojo;

import com.wr.domain.BaseEntityPojo.BaseEntityDto;
import lombok.Data;


@Data
public class SysUserDto extends BaseEntityDto {

    private Long roleId;
    private String userName;
    private String mobile;
    private String status;
}
