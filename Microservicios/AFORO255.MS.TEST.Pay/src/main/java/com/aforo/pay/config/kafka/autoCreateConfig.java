package com.aforo.pay.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class autoCreateConfig {

    @Value("${app.topic.name}")
    private String topicName;

    @Value("${app.topic.partitions:1}")
    private int partitions;

    @Value("${app.topic.replicas:1}")
    private int replicas;

    @Bean
    public NewTopic createTransactionTopic() {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
