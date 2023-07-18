package com.wr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wr.common.RedisCache;
import com.wr.common.constants.CacheConstants;
import com.wr.common.utils.BeanUtil;
import com.wr.common.utils.SecurityUtils;
import com.wr.common.utils.StringUtils;
import com.wr.domain.SysConfigPojo.*;
import com.wr.mapper.SysConfigMapper;
import com.wr.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigPo> implements ISysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<SysConfigVo> selectConfigList(SysConfigDto sysConfigDto) {
        return sysConfigMapper.selectConfigList(sysConfigDto);
    }

    @Override
    public SysConfigVo selectConfigById(Long configId) {
        return BeanUtil.beanToBean(sysConfigMapper.selectById(configId), new SysConfigVo());
    }

    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = redisCache.getCacheObject(getCacheKey(configKey));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfigPo retConfig = sysConfigMapper.selectOne(Wrappers.lambdaQuery(SysConfigPo.class).eq(SysConfigPo::getConfigKey, configKey).last("limit 1"));
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public boolean install(AddSysConfigDto addSysConfigDto) {
        SysConfigPo sysConfigPo = BeanUtil.beanToBean(addSysConfigDto, new SysConfigPo());
        sysConfigPo.setCreateBy(SecurityUtils.getUsername());
        sysConfigPo.setCreateTime(new Date());
        if (sysConfigMapper.insert(sysConfigPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateInfo(UpSysConfigDto upSysConfigDto) {
        SysConfigPo sysConfigPo = BeanUtil.beanToBean(upSysConfigDto, new SysConfigPo());
        sysConfigPo.setUpdateBy(SecurityUtils.getUsername());
        sysConfigPo.setUpdateTime(new Date());
        if (sysConfigMapper.updateById(sysConfigPo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String value = selectConfigByKey("sys.account.captchaEnabled");
        if (Boolean.valueOf(value)) {
            return Boolean.valueOf(value);
        }
        return false;
    }

    @Override
    public boolean resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
        return true;
    }




    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache()
    {
        List<SysConfigVo> configsList = sysConfigMapper.selectConfigList(new SysConfigDto());
        for (SysConfigVo config : configsList)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }
}
