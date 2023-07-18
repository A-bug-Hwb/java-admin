package com.wr.common.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建对象copy工具类 复制对象采用cglib beanCopier, cglib性能高于spring BeanUtils
 * 注意：当被复制的实体类使用lombok @Accessors(chain = true)注解时 使用BeanUtil.copy方法失败，请使用 org.springframework.beans.BeanUtils.copyProperties 方法
 *
 * @author zhaoyang10
 * @date 2018/10/25
 */
@Slf4j
public class BeanUtil {

    private BeanUtil() {
    }

    /**
     * 缓存copier
     */
    private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     * 对象复制
     *
     * @param source 源对象-被复制的对象
     * @param target 目标对象-新对象
     * @author zhaoyang10
     */
    public static void copy(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIERS.put(beanKey, copier);
        } else {
            copier = BEAN_COPIERS.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + ":" + targetClass.getName();
    }

    public static <T> T beanToBean(Object object,T bean) {
        return mapToBean(beanToMap(object),bean);
    }

    /**
     * 将对象装换为map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object bean) {
        return BeanMap.create(bean);
    }

    /**
     * 将map装换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
    /**
     * 将List<T>转换为List<Map<String, Object>>
     */
    public static <T> List<Map> objectsToMaps(List<T> objList) {
        List<Map> list = Lists.newArrayList();
        if (objList != null && !objList.isEmpty()) {
            Map map;
            for (Object item : objList) {
                map = beanToMap(item);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) {
        try {
            List<T> list = Lists.newArrayList();
            if (maps != null && !maps.isEmpty()) {
                T bean;
                for (Map<String, Object> item : maps) {
                    bean = clazz.newInstance();
                    mapToBean(item, bean);
                    list.add(bean);
                }
            }
            return list;
        } catch (Exception e) {
            log.error("转换出错", e);
            return Collections.emptyList();
        }
    }

    /**
     * 通过对象属性名获取对象中的属性值
     */
    public static Object getProp(Object object, String name) {
        if (object == null || name == null || "".equals(name.trim())) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String tempName = property.getName();

                if (name.equals(tempName)) {
                    Method getter = property.getReadMethod();
                    Object value = null;
                    if (getter != null) {
                        value = getter.invoke(object);
                    }
                    return value;
                }
            }
        } catch (Exception e) {
            log.error("getProp出错", e);
        }
        return null;
    }

}
