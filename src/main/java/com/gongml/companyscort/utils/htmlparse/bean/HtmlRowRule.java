package com.gongml.companyscort.utils.htmlparse.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 10:40
 **/
@Data
public class HtmlRowRule {
    private String rowColumnName;
    private List<HtmlPathRule> pathRules;
    private HtmlLinkRule linkRule;
}
