package com.wr.domain.SysRolePojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;

import java.util.Set;

@Data
public class SysRoleVo extends BaseEntityVo {

    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleSort;
    private boolean menuCheckStrictly;
    private String status;
    private boolean flag = false;

    private Long[] menuIds;

    /** 角色菜单权限 */
    private Set<String> permissions;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
