package com.wr.common.handle;


import com.wr.common.RedisCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            context = applicationContext;
            // 项目初始化完成后，手动检验数据库和redis
            DataSource dataSource = (DataSource) context.getBean("masterDataSource");
            dataSource.getConnection().close();
            redisCache.getCacheObject("测试");
        } catch (Exception e) {
            e.printStackTrace();
            // 当检测数据库连接失败时, 停止项目启动
            System.exit(-1);
        }
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

}
