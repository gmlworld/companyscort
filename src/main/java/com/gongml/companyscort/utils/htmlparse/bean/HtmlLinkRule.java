package com.gongml.companyscort.utils.htmlparse.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 10:49
 **/
@Data
public class HtmlLinkRule {
    private String linkColumnName;
    private String linkFunction;
    private List<HtmlLinkTitleRule> htmlLinkTitleRules;
    private String linkFilter;
}
