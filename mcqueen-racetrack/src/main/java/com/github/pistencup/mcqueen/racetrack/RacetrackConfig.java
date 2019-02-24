package com.github.pistencup.mcqueen.racetrack;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class RacetrackConfig {

    @Bean
    public RequestInterceptor racetrackFeignRequestInterceptor(){
        return new RacetrackFeignRequestInterceptor();
    }

}
