package com.github.pistencup.mcqueen.racetrack;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RacetrackRegistrar {

    @Bean
    public RequestInterceptor racetrackFeignRequestInterceptor(){
        return new RacetrackFeignRequestInterceptor();
    }
}
