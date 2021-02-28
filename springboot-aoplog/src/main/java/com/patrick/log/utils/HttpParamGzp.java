package com.patrick.log.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author GP
 * @date 2019/5/4 11:45 AM
 */
@Slf4j
public class HttpParamGzp {


    /**
     * 获取Body中所有参数 并且参数按照ascll排序
     */
    public static Map<String, String> getSortedBodyParms(final HttpServletRequest request)
            throws IOException {
        SortedMap<String, String> result = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder wholeStr = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            wholeStr.append(str);
        }
        wholeStr.trimToSize();
        String s = wholeStr.toString();
        if (StringUtils.hasLength(s)) {
            Map<String, String> allRequestParam = JSONObject.parseObject(s, Map.class);
            for (Map.Entry entry : allRequestParam.entrySet()) {
                result.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return result;
    }

    /**
     * 获取请求体中的参数，请求方式post Content-Type:application/json;
     */
    public static Map<String, String> getBodyParams(HttpServletRequest request) throws IOException {
        Map<String, String> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder wholeStr = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            wholeStr.append(str);
        }
        wholeStr.trimToSize();
        String s = wholeStr.toString();
        if (!StringUtils.isEmpty(s)) {
            map = JSONObject.parseObject(s, Map.class);
            log.debug("商户请求参数：{}", map);
        }
        return map;
    }


    /**
     * 将URL中的参数和body中的参数合并 适合MD5方式加密
     */
    public static SortedMap<String, String> getAllParams(HttpServletRequest request) throws IOException {
        SortedMap<String, String> result = new TreeMap<>();
        // 获取URL上的参数
        getUrlParams(request, result);
        // 获取body参数
        getAllRequestParam(request, result);
        return result;
    }

    /**
     * 获取Body中所有参数 并且参数按照ascll排序
     */
    public static void getAllRequestParam(final HttpServletRequest request, SortedMap<String, String> result)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder wholeStr = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            wholeStr.append(str);
        }
        wholeStr.trimToSize();
        String s = wholeStr.toString();
        if (!StringUtils.isEmpty(s)) {
            Map<String, String> allRequestParam = JSONObject.parseObject(s, Map.class);
            for (Map.Entry entry : allRequestParam.entrySet()) {
                result.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    /**
     * 获取路径上的参数
     */
    public static void getUrlParams(HttpServletRequest request, SortedMap<String, String> result) {
        String param = "";
        try {
            String urlParam = request.getQueryString();
            if (urlParam != null) {
                param = URLDecoder.decode(urlParam, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] params = param.split("&");
        for (String s : params) {
            int index = s.indexOf('=');
            if (index != -1) {
                result.put(s.substring(0, index), s.substring(index + 1));
            }
        }
    }
}
