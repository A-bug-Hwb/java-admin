package com.wr.domain.SysRolePojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityBo;
import lombok.Data;

@Data
public class SysRoleBo extends BaseEntityBo {

    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleSort;
    private boolean menuCheckStrictly;
    private String status;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
