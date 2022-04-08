package com.jhelper.jserve.web;

import com.jhelper.jserve.web.entity.RestProxyVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/rest-proxy")
public class RestProxyController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/")
    public RestProxyVO restProxy(@RequestBody RestProxyVO restProxyVO) {

        logger.debug("url: {}", restProxyVO.getUrl());
        logger.debug("method: {}", restProxyVO.getMethod());
        logger.debug("headers: {}", restProxyVO.getHeaders());
        logger.debug("params: {]", restProxyVO.getParams());
        logger.debug("bodyValue: {}", restProxyVO.getBodyValue());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        restProxyVO.getHeaders().forEach((k, v) -> {
            headers.add(k, v);
        });

        HttpEntity<String> requestEntity = new HttpEntity<>(restProxyVO.getBodyValue(), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(restProxyVO.getUrl(),
                HttpMethod.resolve(restProxyVO.getMethod()), requestEntity,
                String.class, restProxyVO.getParams());

        RestProxyVO result = new RestProxyVO();

        result.setHeaders(responseEntity.getHeaders().toSingleValueMap());
        result.setBodyValue(responseEntity.getBody());
        result.setStatusCode(responseEntity.getStatusCodeValue());

        return result;
    }
}
