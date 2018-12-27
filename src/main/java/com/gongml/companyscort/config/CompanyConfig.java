package com.gongml.companyscort.config;

import com.gongml.companyscort.utils.jsonparse.bean.JsonParseRule;
import com.gongml.companyscort.utils.jsonparse.bean.JsonPathRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-27 15:06
 **/
@Configuration
public class CompanyConfig {

    @Bean("companyInfoRule")
    public JsonParseRule createCompanyInfoRule(){
        JsonPathRule jsonPathRule = null;
        JsonParseRule companyInfoRule = new JsonParseRule();
        companyInfoRule.setJsonPathRuleList(new ArrayList<>());

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("gpdm");
        jsonPathRule.setJsonPath("$[*].label");
        jsonPathRule.setGroovyFunc("gpdm.split(' ')[0]");
        companyInfoRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("gsjc");
        jsonPathRule.setJsonPath("$[*].label");
        jsonPathRule.setGroovyFunc("gsjc.split(' ')[1]");
        companyInfoRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("mtcc");
        jsonPathRule.setJsonPath("$[*].label");
        jsonPathRule.setGroovyFunc("mtcc.split(' ')[2]");
        companyInfoRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("pyjc");
        jsonPathRule.setJsonPath("$[*].label");
        jsonPathRule.setGroovyFunc("pyjc.split(' ')[3]");
        companyInfoRule.getJsonPathRuleList().add(jsonPathRule);

        return companyInfoRule;
    }

    @Bean("companyNewsRule")
    public JsonParseRule createCompanyNewsRule(){
        JsonPathRule jsonPathRule = null;
        JsonParseRule companyNewsRule = new JsonParseRule();
        companyNewsRule.setJsonPathRuleList(new ArrayList<>());

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("gpdm");
        jsonPathRule.setJsonPath("$.mainRank.SecuCode");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("endDate");
        jsonPathRule.setJsonPath("$.mainRank.EndDate");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("score");
        jsonPathRule.setJsonPath("$.mainRank.FinalValue");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("totalNum");
        jsonPathRule.setJsonPath("$.mainRank.TotalNum");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("marketRank");
        jsonPathRule.setJsonPath("$.mainRank.MarketRank");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("fl");
        jsonPathRule.setJsonPath("$.mainRank.SecuAbbr");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("hy");
        jsonPathRule.setJsonPath("$.mainRank.SecondIndustryName");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("hyNum");
        jsonPathRule.setJsonPath("$.mainRank.hyCount");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("hyRank");
        jsonPathRule.setJsonPath("$.mainRank.ValueRank");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("jcRank");
        jsonPathRule.setJsonPath("$.basicRank.ValueRank");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("jsRank");
        jsonPathRule.setJsonPath("$.technicalRank.ValueRank");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("xxRank");
        jsonPathRule.setJsonPath("$.messageRank.ValueRank");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("zhps");
        jsonPathRule.setJsonPath("$.zhps");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("jcps");
        jsonPathRule.setJsonPath("$.fundamentalComment");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("jsps");
        jsonPathRule.setJsonPath("$.technicalComment");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        jsonPathRule = new JsonPathRule();
        jsonPathRule.setColumnName("xxps");
        jsonPathRule.setJsonPath("$.infoComment");
        companyNewsRule.getJsonPathRuleList().add(jsonPathRule);

        return companyNewsRule;
    }
}
