package com.gongml.companyscort.utils.jsonparse;

import com.gongml.companyscort.utils.groovy.GroovyContext;
import com.gongml.companyscort.utils.jsonparse.bean.JsonParseRule;
import com.gongml.companyscort.utils.jsonparse.bean.JsonPathRule;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-24 17:28
 **/
@Component
public class JsonParseUtil {
    @Autowired
    private GroovyContext groovyContext;

    private String trimJsonStr(String jsonStr) {
        int oTag = jsonStr.indexOf("{");
        int aTag = jsonStr.indexOf("[");
        if (oTag >= 0 && (aTag < 0 || oTag < aTag)) {
            if (!jsonStr.startsWith("{")) {
                jsonStr = StringUtils.substringBeforeLast(jsonStr.substring(oTag), "}") + "}";
            }
        } else {
            if (aTag < 0)
                return jsonStr;
            if (!jsonStr.startsWith("[")) {
                jsonStr = StringUtils.substringBeforeLast(jsonStr.substring(aTag), "]") + "]";
            }
        }
        return jsonStr;
    }

    /**
    * @Description: 通过规则解析json
    * @Param: [jsonStr, jsonParseRule]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: Gongml
    * @Date: 2018-12-25
    */
    public List<Map<String, Object>> parseJsonData(String jsonStr, JsonParseRule jsonParseRule) throws Exception {
        List<Map<String, Object>> listData = new ArrayList<>();
        try {
            String json = trimJsonStr(jsonStr);
            int size = 0;
            List<List<Object>> anserList = new ArrayList<>();
            for (JsonPathRule jsonPathRule : jsonParseRule.getJsonPathRuleList()) {
                String jsonPath = jsonPathRule.getJsonPath();
                String colName = jsonPathRule.getColumnName();
                String script = jsonPathRule.getGroovyFunc();
                List<Object> mList = new ArrayList<>();
                if (StringUtils.isEmpty(jsonPath)) {
                    mList.add("");
                } else {
                    Object obj = JsonPath.read(json, jsonPath);
                    if (obj instanceof List) {
                        mList = (List) obj;
                    } else {
                        mList.add(obj);
                    }
                }
                if (!StringUtils.isEmpty(script)) {
                    for (int i = 0; i < mList.size(); i++) {
                        Map<String, String> args = new HashMap<>();
                        Object originalValue = mList.get(i);
                        args.put(colName, originalValue.toString());
                        String val = groovyContext.execExpr(script, args, String.class);
                        mList.set(i, val);
                    }
                }
                size = mList.size() > size ? mList.size() : size;
                anserList.add(mList);
            }
            for (int i = 0; i < size; i++) {
                listData.add(new LinkedHashMap<>());
            }
            for (int ruleIndex = 0; ruleIndex < jsonParseRule.getJsonPathRuleList().size(); ruleIndex++) {
                JsonPathRule jsonPathRule = jsonParseRule.getJsonPathRuleList().get(ruleIndex);
                List<Object> mList = anserList.get(ruleIndex);
                boolean isObject = mList.size() == 1 && size != 1;
                for (int i = 0; i < mList.size(); i++) {
                    Map<String, Object> map = listData.get(i);
                    map.put(jsonPathRule.getColumnName(), mList.get(i));
                }
                for (int index = mList.size(); index < size; index++) {
                    Map<String, Object> map = listData.get(index);
                    if (isObject) {
                        map.put(jsonPathRule.getColumnName(), mList.get(0));
                    } else {
                        map.put(jsonPathRule.getColumnName(), "");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return listData;
    }
}
