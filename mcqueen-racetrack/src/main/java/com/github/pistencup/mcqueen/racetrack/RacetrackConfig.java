package com.github.pistencup.mcqueen.racetrack;

import com.github.pistencup.mcqueen.racetrack.config.RacetrackConfiguration;
import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties(RacetrackConfiguration.class)
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
