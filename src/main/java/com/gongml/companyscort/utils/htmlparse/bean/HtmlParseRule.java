package com.gongml.companyscort.utils.htmlparse.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 10:39
 **/
@Data
public class HtmlParseRule {
    private List<HtmlRowRule> rowRules;
    private List<HtmlCombinationRule> combinationRules;
}
