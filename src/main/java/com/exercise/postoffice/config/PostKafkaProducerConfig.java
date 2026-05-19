package com.exercise.postoffice.config;

import com.exercise.postoffice.model.dto.DeliveryLetter;
import com.exercise.postoffice.model.dto.SendLetter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PostKafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, SendLetter> sendLetterProducerFactory() {
        Map<String, Object> props =
                kafkaProperties.buildProducerProperties();

        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, SendLetter> sendLetterKafkaTemplate() {
        return new KafkaTemplate<>(sendLetterProducerFactory());
    }

    @Bean
    public ProducerFactory<String, DeliveryLetter> getDeliveryproducerFactory() {
        Map<String, Object> props =
                kafkaProperties.buildProducerProperties();

        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, DeliveryLetter> deliveryLetterKafkaTemplate() {
        return new KafkaTemplate<>(getDeliveryproducerFactory());
    }
}
