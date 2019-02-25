package com.github.pistencup.mcqueen.racetrack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mcqueen.racetrack")
public class RacetrackConfiguration {

    @Getter
    @Setter
    private Boolean enableRequestWrapper = true;
}
