package com.github.pistencup.mcqueen.camera.model;

import com.alibaba.fastjson.JSON;
import com.github.pistencup.mcqueen.camera.EnumResultType;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ResultDescriptor {

    private final HttpServletResponse response;
    @Getter
    private HashMap<String, String> headers;
    @Getter
    private Integer status;
    @Getter
    private EnumResultType resultType;
    @Getter
    private String result;
    @Getter
    private HashMap<String, String> mv;
    @Getter
    private HashMap<String, Object> exception;


    public ResultDescriptor(RequestAttributes requestAttributes, Object rst){

        ServletRequestAttributes attrs = (ServletRequestAttributes)requestAttributes;
        response = attrs.getResponse();
        if (response == null){
            return;
        }
        result = "";
        headers = makeHeaderMap(response);
        status = response.getStatus();

        if (rst == null){
            resultType = EnumResultType.NONE;
        } else if (rst instanceof String){
            resultType = EnumResultType.STRING;
            result = String.valueOf(rst);
        } else if (rst instanceof Throwable){
            resultType = EnumResultType.EXCEPTION;
            exception = makeThrowableMap(rst);
        } else if (rst instanceof ModelAndView){
            resultType = EnumResultType.MODEL_AND_VIEW;
            mv = makeModelAndViewMap(rst);
        } else {
            resultType = EnumResultType.JSON;
            result = JSON.toJSONString(rst);
        }
    }

    private HashMap<String, String> makeHeaderMap(HttpServletResponse response){

        HashMap<String, String> rtn = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            rtn.put(headerName, response.getHeader(headerName));
        }

        return rtn;
    }

    private HashMap<String, Object> makeThrowableMap(Object rst){
        HashMap<String, Object> rstMap = new HashMap<>();
        Throwable throwable = (Throwable)rst;
        rstMap.put("type", throwable.getClass().getName());
        rstMap.put("message", throwable.getLocalizedMessage());

        StackTraceElement[] steArr = throwable.getStackTrace();
        ArrayList<String> steStrArray = new ArrayList<>();
        for(StackTraceElement ste : steArr){
            if (ste != null) {
                steStrArray.add(ste.toString());
            }
        }
        rstMap.put("stacktrace", steStrArray);
        return rstMap;
    }

    private HashMap<String, String> makeModelAndViewMap(Object rst){
        HashMap<String, String> rstMap = new HashMap<>();
        ModelAndView mv = (ModelAndView)rst;
        rstMap.put("viewName", mv.getViewName());
        rstMap.put("model", JSON.toJSONString(mv.getModel()));
        return rstMap;
    }

}
