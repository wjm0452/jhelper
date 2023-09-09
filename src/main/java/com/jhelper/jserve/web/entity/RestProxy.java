package com.jhelper.jserve.web.entity;

import java.util.Map;

import lombok.Data;

@Data
public class RestProxy {
    String url;
    String method;
    Map<String, String> headers;
    Map<String, String> params;
    String bodyValue;
    int statusCode;

}
