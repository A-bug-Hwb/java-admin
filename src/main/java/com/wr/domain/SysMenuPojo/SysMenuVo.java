package com.wr.domain.SysMenuPojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wr.domain.BaseEntityPojo.BaseEntityVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuVo extends BaseEntityVo {

    private Long menuId;
    private String menuName;
    private Long parentId;
    private Long orderNum;
    private String path;
    private String component;
    private String query;
    private String isFrame;
    private String isCache;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SysMenuVo> children = new ArrayList<SysMenuVo>();

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
