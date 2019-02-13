package com.github.pistencup.mcqueen.racetrack;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class RacetrackFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        CloudContext cloudContext = CloudContext.getCurrent();
        if (cloudContext != null) {
            // attach cloud context to every remote call
            requestTemplate.header(CloudContext.HEADER_NAME_CALL_INDEX, cloudContext.makeNextCallIndex().toString());
            requestTemplate.header(CloudContext.HEADER_NAME_PREVIOUS_SPAN_ID, cloudContext.getSpanID());
            requestTemplate.header(CloudContext.HEADER_NAME_REQUEST_ID, cloudContext.getRequestID());
            requestTemplate.header(CloudContext.HEADER_NAME_VERSION_NAME, cloudContext.getVersionName());
        }

    }
}
