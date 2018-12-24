package com.gongml.companyscort.utils.httputil.bean;

import lombok.Data;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @program: companyscort
 * @description: 请求参数
 * @author: Gongml
 * @create: 2018-12-24 14:35
 **/
@Data
public class Request {
    private String uri;
    private String requestProtocol;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Charset charset;
}
