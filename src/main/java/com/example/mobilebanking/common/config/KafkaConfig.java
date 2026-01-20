package com.example.mobilebanking.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transaction")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic merchantSettlementTopic() {
        return TopicBuilder.name("merchant-settlement")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
