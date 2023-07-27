package com.wr.common.utils;

import com.wr.common.SnowflakeIdWorker;
import com.wr.common.SnowflakeIdWorker15;
import com.wr.common.SnowflakeIdWorker18;

/**
 * ID生成器工具类
 *
 * @author
 */
public class IdUtils
{

    public static String getUUID()
    {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        return String.valueOf(idWorker.nextId());
    }


    public static String getUUID15()
    {
        SnowflakeIdWorker15 idWorker = new SnowflakeIdWorker15(1);
        return String.valueOf(idWorker.nextId());
    }

    public static String getUUID18()
    {
        return String.valueOf(SnowflakeIdWorker18.INSTANCE.nextId());
    }

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
