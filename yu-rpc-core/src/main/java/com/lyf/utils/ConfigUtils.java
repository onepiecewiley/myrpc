package com.lyf.utils;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/18 17:47
 */
public class ConfigUtils {
    public static <T> T loadConfig(Class<T> clz,String prefix,String environment){
        StringBuilder path = new StringBuilder(("application"));
        if(!StrUtil.isBlankIfStr(environment)){
            path.append("-" + environment);
        }
        path.append(".properties");
        Props props = new Props(path.toString()); // 相当于创建一个读取path文件下的流 Props会找到和类路径相同下的名称是path的文件
        T bean = props.toBean(clz, prefix);
        return bean;
    }

    public static <T> T loadConfig(Class<T> clz,String prefix){
        return loadConfig(clz,prefix,"");
    }
}
