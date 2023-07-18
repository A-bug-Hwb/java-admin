package com.wr.domain.SysDictPojo.SysDictTypePojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;

@Data
public class SysDictTypeVo extends BaseEntityVo {

    private Long dictId;
    private String dictName;
    private String dictType;
    private String status;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
