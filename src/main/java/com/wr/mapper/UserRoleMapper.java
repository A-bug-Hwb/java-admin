package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysRolePojo.SysRoleVo;
import com.wr.domain.SysUserRolePojo.SysUserRolePo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<SysUserRolePo> {


}
