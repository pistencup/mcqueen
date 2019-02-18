package com.github.pistencup.mcqueen.camera;

import com.github.pistencup.mcqueen.camera.config.CameraConfiguration;
import com.github.pistencup.mcqueen.camera.config.KafkaConfiguration;
import com.github.pistencup.mcqueen.camera.model.EndpointDescriptor;
import com.github.pistencup.mcqueen.camera.saver.DataSaver;
import com.github.pistencup.mcqueen.camera.saver.DataWriter;
import com.github.pistencup.mcqueen.camera.saver.DefaultDataWriter;
import com.github.pistencup.mcqueen.camera.saver.KafkaDataWriter;
import com.github.pistencup.mcqueen.racetrack.EnableMcqueenRacetrack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableMcqueenRacetrack
@EnableKafka
@EnableConfigurationProperties(CameraConfiguration.class)
@Import({
        CameraAspect.class,
        McqueenRequestFilter.class
})
public class CameraConfig {

    private final CameraConfiguration cameraConfiguration;

    @Value("${spring.application.name}")
    private String applicationName;

    CameraConfig(CameraConfiguration cameraConfiguration){
        this.cameraConfiguration = cameraConfiguration;
    }

    @Bean
    public EndpointDescriptor endpointDescriptor(){
        return EndpointDescriptor.instance(applicationName);
    }

    @Bean
    public DataWriter dataWriter(EndpointDescriptor endpoint){
        KafkaConfiguration kafkaConfiguration = cameraConfiguration.getKafka();
        if (kafkaConfiguration != null){
            return new KafkaDataWriter(kafkaConfiguration, endpoint);
        }

        return new DefaultDataWriter();
    }

    @EventListener
    @Bean
    public DataSaver dataSaver(DataWriter dataWriter){
        DataSaver dataSaver = new DataSaver(dataWriter);
        dataSaver.start();

        return dataSaver;
    }
}
