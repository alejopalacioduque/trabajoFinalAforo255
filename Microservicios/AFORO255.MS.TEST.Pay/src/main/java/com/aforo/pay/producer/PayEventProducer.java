package com.aforo.pay.producer;

import com.aforo.pay.domain.Transaction;
import com.aforo.pay.service.ITransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Component
public class PayEventProducer {

    private Logger log = LoggerFactory.getLogger(PayEventProducer.class);

    private final String TOPIC_NAME;

    private KafkaTemplate<Integer, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    private ITransactionService transactionService;

    @Autowired
    public PayEventProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper,
                            ITransactionService transactionService, @Value("${app.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
        this.TOPIC_NAME = topicName;
    }

    public ListenableFuture<SendResult<Integer, String>> sendPaytEvent(Transaction depositEvent)
            throws JsonProcessingException {
        Integer key = depositEvent.getOperationId();
        String value = objectMapper.writeValueAsString(depositEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, TOPIC_NAME);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(key, value, throwable);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                try {
                    handleSuccess(key, value, result);
                } catch (JsonProcessingException e) {
                    log.error("Error to save redis Transaction", e);
                }
            }
        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("pay-event-source", "scaner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleFailure(Integer key, String value,  Throwable ex) {
        log.error("Error sending the Message and exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable e){
            log.error("Error in OnFailure : {}", e.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) throws JsonProcessingException {

        log.info("Message sent Successfully for the key {} and value {}, partition is {}",
                key, value, result.getRecordMetadata().partition());
    }
}
