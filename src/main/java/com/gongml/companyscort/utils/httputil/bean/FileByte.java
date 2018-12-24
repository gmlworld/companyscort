package com.gongml.companyscort.utils.httputil.bean;

import lombok.Data;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-24 14:24
 **/
@Data
public class FileByte {
    private String contentDisposition;
    private String contentType;
    private byte[] content;
}
