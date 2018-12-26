package com.gongml.companyscort.utils.htmlparse.bean;

import lombok.Data;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 10:37
 **/
@Data
public class HtmlPathRule {
    private String columnName;
    private String xpath;
    private String function;
    private String filter;
    private Integer ruleType;
}
