package com.github.pistencup.mcqueen.camera.config;

import lombok.Getter;
import lombok.Setter;


public class KafkaConfiguration {

    @Getter @Setter
    private String topic = "pistencup-camera";
    @Getter @Setter
    private Integer batchSize = 1000;
    @Getter @Setter
    private Integer linger = 0;
    @Getter @Setter
    private String bootstrapServers = "localhost:9092";
}
