package com.github.pistencup.mcqueen.camera.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pistencup.camera")
public class CameraConfiguration {

    @Getter @Setter
    private Boolean enableRequestWrapper = true;
    @Getter @Setter
    private Integer maxBodyLogLength = Integer.MAX_VALUE;
    @Getter @Setter
    private Integer maxParamKeyLength = Integer.MAX_VALUE;
    @Getter @Setter
    private Integer maxParamValueLength = Integer.MAX_VALUE;
    @Getter @Setter
    private Integer maxParamCount = Integer.MAX_VALUE;
    @Getter @Setter
    private KafkaConfiguration kafka = null;

}
