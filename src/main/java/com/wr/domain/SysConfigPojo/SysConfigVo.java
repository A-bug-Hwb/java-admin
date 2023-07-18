package com.wr.domain.SysConfigPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;

@Data
public class SysConfigVo extends BaseEntityVo {


    private Long configId;
    /** 参数名称 */
    private String configName;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 系统内置（Y是 N否） */
    private String configType;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
