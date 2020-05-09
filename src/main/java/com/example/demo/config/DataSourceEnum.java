package com.example.demo.config;

import lombok.Getter;
import lombok.ToString;

/**
 * 定义各种数据源值
 *
 * @author pax
 */
@ToString
public enum DataSourceEnum {

    // 数据源1
    DS1("ds1"),
    // 数据源2
    DS2("ds2");

    @Getter
    private String value;

    DataSourceEnum(String value) {
        this.value = value;
    }
}
