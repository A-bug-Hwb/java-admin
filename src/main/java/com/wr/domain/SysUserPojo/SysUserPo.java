package com.wr.domain.SysUserPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wr.domain.BaseEntityPojo.BaseEntityPo;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUserPo extends BaseEntityPo {

    @TableId(type = IdType.AUTO)
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

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
