package com.github.pistencup.mcqueen.camera.saver;

import com.alibaba.fastjson.JSON;
import com.github.pistencup.mcqueen.camera.config.KafkaConfiguration;
import com.github.pistencup.mcqueen.camera.model.ActionRecord;
import com.github.pistencup.mcqueen.camera.model.EndpointDescriptor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;

import java.util.HashMap;

public class KafkaDataWriter implements DataWriter {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfiguration kafkaConfiguration;

    public KafkaDataWriter(@NonNull KafkaConfiguration kafkaConfiguration, EndpointDescriptor endpoint){
        HashMap<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfiguration.getBatchSize());
        config.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfiguration.getLinger());

        config.put(ProducerConfig.CLIENT_ID_CONFIG, endpoint.getNodeIP() + "-" + endpoint.getServiceName());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        this.kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config));
        this.kafkaConfiguration = kafkaConfiguration;
    }

    @Override
    public void write(ActionRecord record) {
        kafkaTemplate.send(kafkaConfiguration.getTopic(), JSON.toJSONString(record));
    }

    @Override
    public void flush() {
        kafkaTemplate.flush();
    }
}
