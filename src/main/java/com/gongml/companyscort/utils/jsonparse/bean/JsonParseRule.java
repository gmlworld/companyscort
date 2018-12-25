package com.gongml.companyscort.utils.jsonparse.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-25 16:00
 **/
@Data
public class JsonParseRule {
    List<JsonPathRule> jsonPathRuleList;
}
