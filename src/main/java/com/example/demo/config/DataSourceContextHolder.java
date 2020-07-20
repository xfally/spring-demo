package com.example.demo.config;

/**
 * 存放当前的数据源值
 *
 * @author pax
 * @since 2020-03-19
 */
public class DataSourceContextHolder {
    private static final ThreadLocal CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param dataSourceEnum 数据源
     */
    public static void setDataSource(DataSourceEnum dataSourceEnum) {
        CONTEXT_HOLDER.set(dataSourceEnum.getValue());
    }

    /**
     * 取得当前数据源
     *
     * @return 数据源名字
     */
    public static String getDataSource() {
        return (String) CONTEXT_HOLDER.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}
