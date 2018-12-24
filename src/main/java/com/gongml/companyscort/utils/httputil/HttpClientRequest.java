package com.gongml.companyscort.utils.httputil;

import com.gongml.companyscort.utils.httputil.bean.Request;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-24 14:58
 **/
@Component
public class HttpClientRequest {
    @Autowired
    private HttpClientUtil httpClientUtil;

    /**
     * @Description: httpget请求数据
     * @Param: [requestParams]
     * @return: java.lang.String
     * @Author: Gongml
     * @Date: 2018-12-24
     */
    public String httpGet(Request requestParams) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        String url = requestParams.getUri();
        HttpRequestBase request = httpClientUtil.createHttpGetRequest(url, 60000, null);
        request.setHeader("Referer", url);
        Map<String, String> headers = requestParams.getHeaders();
        if (!MapUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> {
                request.setHeader(k, v);
            });
        }
        return httpClientUtil.executeHtmlFor(httpclient, request);
    }

    /**
     * @Description: httpPost请求数据
     * @Param: [requestParams]
     * @return: java.lang.String
     * @Author: Gongml
     * @Date: 2018-12-24
     */
    public String httpPost(Request requestParams) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        Map<String, String> headers = requestParams.getHeaders();
        String urlStr = requestParams.getUri();
        Map<String, String> param = requestParams.getParams();
        URI uri = httpClientUtil.getUri(urlStr);
        HttpRequestBase request = httpClientUtil.createHttpPostRequest(uri, 60000, null);
        HttpPost httppost = (HttpPost) request;
        httppost.setHeader("Referer", urlStr);
        if (!MapUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> {
                httppost.setHeader(k, v);
            });
        }
        if (!MapUtils.isEmpty(param)) {
            if (param.containsKey("RequestBody")) {
                StringEntity stringEntity = new StringEntity(MapUtils.getString(param, "RequestBody"));
                httppost.setEntity(stringEntity);
            } else {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                param.forEach((k, v) -> {
                    list.add(new BasicNameValuePair(k, v));
                });
                // 参数的解码所使用的编码
                UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
                httppost.setEntity(uefEntity);
            }
        }
        return httpClientUtil.executeHtmlFor(httpclient, httppost);
    }
}
