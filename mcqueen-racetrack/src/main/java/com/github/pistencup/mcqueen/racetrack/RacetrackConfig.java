package com.github.pistencup.mcqueen.racetrack;

import feign.RequestInterceptor;
import org.springframework.context.annotation.*;

@Configuration
@Import(McqueenRequestFilter.class)
public class RacetrackConfig {

    @Bean
    public RequestInterceptor racetrackFeignRequestInterceptor(){
        return new RacetrackFeignRequestInterceptor();
    }

    @Bean
    @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CloudContext cloudContext(){
        return CloudContext.getCurrent();
    }
}
