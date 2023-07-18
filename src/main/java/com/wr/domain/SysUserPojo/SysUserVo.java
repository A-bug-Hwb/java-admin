package com.wr.domain.SysUserPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;



@Data
public class SysUserVo extends BaseEntityVo {

    private Long userId;
    private String uid;
    private String nickName;
    private String userName;
    private String avatar;
    private String sex;
    private String mobile;
    private String mailbox;
    private String status;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
