package com.gongml.companyscort.utils.jsonparse.bean;

import lombok.Data;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-25 16:35
 **/
@Data
public class JsonPathRule {
    private String columnName;
    private String jsonPath;
    private String groovyFunc;
}
