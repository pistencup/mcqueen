package com.github.pistencup.mcqueen.camera.model;

import lombok.Getter;
import com.github.pistencup.mcqueen.camera.config.CameraConfiguration;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestDescriptor {

    private CameraConfiguration cameraConfiguration;

    @Getter
    private final HashMap<String, String> headers;
    @Getter
    private final String queryString;
    @Getter
    private final HashMap<String, String> params;
    @Getter
    private final String body;
    @Getter
    private final String remoteAddr;
    @Getter
    private final String url;
    @Getter
    private final String localAddr;
    @Getter
    private final String localName;
    @Getter
    private final Integer port;
    @Getter
    private Boolean isParamTruncated = false;
    @Getter
    private Boolean isBodyTruncated = false;

    public RequestDescriptor(RequestAttributes requestAttributes, CameraConfiguration cameraConfiguration) throws IOException {

        this.cameraConfiguration = cameraConfiguration;
        ServletRequestAttributes attrs = (ServletRequestAttributes)requestAttributes;
        HttpServletRequest request = attrs.getRequest();

        remoteAddr = request.getRemoteAddr();
        queryString = request.getQueryString();
        url = request.getRequestURL().toString();
        localAddr = request.getLocalAddr();
        localName = request.getLocalName();
        port = request.getLocalPort();

        headers = makeHeaderMap(request);

        params = makeParamMap(request);

        body = makeBodyString(request);

    }

    private String makeBodyString(HttpServletRequest request) throws IOException {
        String bodyStr = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
        if (bodyStr.length() > cameraConfiguration.getMaxBodyLogLength()){
            isBodyTruncated = true;
            return bodyStr.substring(0, cameraConfiguration.getMaxBodyLogLength());
        }
        return bodyStr;
    }

    private HashMap<String, String> makeHeaderMap(HttpServletRequest request){

        HashMap<String, String> rtn = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            rtn.put(headerName, request.getHeader(headerName));
        }

        return rtn;
    }

    private HashMap<String, String> makeParamMap(HttpServletRequest request){

        HashMap<String, String> rtn = new HashMap<>();

        for(Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()){
            if (rtn.size() > cameraConfiguration.getMaxParamCount()){
                isParamTruncated = true;
                break;
            }

            String[] valArr = entry.getValue();
            String val = (valArr == null ? "" : String.join(",", valArr));
            if (val.length() > cameraConfiguration.getMaxParamValueLength()){
                val = val.substring(0, cameraConfiguration.getMaxParamValueLength());
                isParamTruncated = true;
            }

            String key = entry.getKey();
            if (key.length() > cameraConfiguration.getMaxParamKeyLength()){
                if (StringUtils.isEmpty(val)){ //except NON-HUMAN parameters
                    continue;
                } else {
                    key = key.substring(0, cameraConfiguration.getMaxParamKeyLength()); //except some foolish requests
                    isParamTruncated = true;
                }
            }

            rtn.put(key, val);
        }

        return rtn;
    }

}
