package com.wr.domain.SysRolePojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityDto;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;

import java.util.Set;

@Data
public class SysRoleDto extends BaseEntityDto {

    private String roleName;
    private String roleKey;
    private String status;
}
