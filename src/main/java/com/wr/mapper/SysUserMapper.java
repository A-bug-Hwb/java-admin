package com.wr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.domain.SysUserPojo.SysUserDto;
import com.wr.domain.SysUserPojo.SysUserPo;
import com.wr.domain.SysUserPojo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPo> {

    List<SysUserVo> getUserList(SysUserDto sysUserDto);

    List<SysUserVo> selectAllocatedList(SysUserDto sysUserDto);

    List<SysUserVo> selectUnallocatedList(SysUserDto sysUserDto);
}
