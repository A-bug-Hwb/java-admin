package com.wr.domain.SysUserPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityBo;
import com.wr.domain.SysRolePojo.SysRoleBo;
import lombok.Data;

import java.util.List;

@Data
public class SysUserBo extends BaseEntityBo {

    private Long userId;
    private String uid;
    private String nickName;
    private String userName;
    private String avatar;
    private String sex;
    private String mobile;
    private String mailbox;
    private String password;
    private String status;

    /** 角色对象 */
    private List<SysRoleBo> sysRoleBos;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
