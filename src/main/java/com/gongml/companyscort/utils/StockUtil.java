package com.gongml.companyscort.utils;

import com.gongml.companyscort.utils.httputil.HttpClientRequest;
import com.gongml.companyscort.utils.httputil.bean.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: companyscort
 * @description: 股票代码爬取
 * @author: Gongml
 * @create: 2018-12-24 17:19
 **/
@Component
public class StockUtil {
    @Value("${stock.url}")
    private String url;

    @Autowired
    private HttpClientRequest httpClientRequest;

    public List<Map<String, Object>> getStocksInfo() {
        try {
            Request request = new Request();
            request.setUri(url);
            String json = httpClientRequest.httpGet(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
