package com.gongml.companyscort.utils.jsonparse;

import org.apache.commons.lang.StringUtils;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-24 17:28
 **/
public class JsonParseUtil {
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
}
