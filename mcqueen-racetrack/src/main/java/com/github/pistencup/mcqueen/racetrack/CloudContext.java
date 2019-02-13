package com.github.pistencup.mcqueen.racetrack;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudContext {
    static final String HEADER_NAME_REQUEST_ID = "pc-req-id";
    static final String HEADER_NAME_PREVIOUS_SPAN_ID = "pc-prev-span-id";
    static final String HEADER_NAME_CALL_INDEX = "pc-call-index";
    static final String HEADER_NAME_VERSION_NAME = "pc-ver-name";

    private final AtomicInteger callOutCount = new AtomicInteger(0);
    /**
     * Get the integer "CallIndex" value in the header of the next remote call
     */
    Integer makeNextCallIndex(){
        return callOutCount.incrementAndGet();
    }

    @Getter
    private final String requestID;
    @Getter
    private final String previousSpanID;
    @Getter
    private final String spanID;
    @Getter
    private final String versionName;
    /**
     * The "CallIndex" value of current span
     */
    @Getter
    private final String callIndex;

    /**
     * DO NOT create CloudContext object from constructor
     * Use CloudContext.getCurrent
     * @param request HttpServletRequest in current context
     */
    private CloudContext(@NonNull HttpServletRequest request){

        String requestIDInHeader = request.getHeader(HEADER_NAME_REQUEST_ID);
        if (StringUtils.isEmpty(requestIDInHeader)){
            requestID = UUID.randomUUID().toString();
            spanID = requestID;
        } else {
            requestID = requestIDInHeader;
            spanID = UUID.randomUUID().toString();
        }

        String previousSpanIDInHeader = request.getHeader(HEADER_NAME_PREVIOUS_SPAN_ID);
        previousSpanID = StringUtils.isEmpty(previousSpanIDInHeader) ? "" : previousSpanIDInHeader;

        String versionNameInHeader = request.getHeader(HEADER_NAME_VERSION_NAME);
        versionName = StringUtils.isEmpty(versionNameInHeader) ? "" : versionNameInHeader;

        String callIndexInHeader = request.getHeader(HEADER_NAME_CALL_INDEX);
        callIndex = StringUtils.isEmpty(callIndexInHeader) ? "1" : callIndexInHeader;

    }

    /**
     * Get CloudContext object for current request, return null when request context is not available
     * @return current CloudContext object
     */
    public static CloudContext getCurrent(){
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        if (attrs == null){
            return null;
        }

        CloudContext context = (CloudContext)attrs.getAttribute(CloudContext.class.getName(),
                RequestAttributes.SCOPE_REQUEST);

        if (context == null){
            context = new CloudContext(attrs.getRequest());
            attrs.setAttribute(CloudContext.class.getName(), context, RequestAttributes.SCOPE_REQUEST);
        }

        return context;
    }



}
