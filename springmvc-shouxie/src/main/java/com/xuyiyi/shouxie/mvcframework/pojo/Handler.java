package com.xuyiyi.shouxie.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 封装handler的信息
 */
public class Handler {

    private Object controller;


    private Method method;


    private Pattern pattern;//spring的url 支持正则


    private Map<String, Integer> paramMapping;//参数顺序，跟url进行进行绑定，key是参数名 value是代表第几个参数


    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramMapping = new HashMap<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamMapping() {
        return paramMapping;
    }

    public void setParamMapping(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }
}
