package com.wr.domain.SysDictPojo.SysDictDataPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityDto;
import lombok.Data;

@Data
public class SysDictDataDto extends BaseEntityDto {

    private String dictLabel;
    private String dictType;
    private String status;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
