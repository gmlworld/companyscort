package com.gongml.companyscort.utils.htmlparse.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 11:22
 **/
@Data
public class HtmlCombinationRule {
    private List<String> rowCombinationColumnNames;
    private String combinationFuncton;
    private String combinationFilter;
}
