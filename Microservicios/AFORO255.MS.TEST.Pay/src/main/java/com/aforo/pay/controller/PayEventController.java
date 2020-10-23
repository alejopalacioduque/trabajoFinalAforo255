package com.aforo.pay.controller;

import com.aforo.pay.domain.Transaction;
import com.aforo.pay.producer.PayEventProducer;
import com.aforo.pay.service.ITransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PayEventController {

    private Logger log = LoggerFactory.getLogger(PayEventController.class);

    private PayEventProducer payEventProducer;

    private ITransactionService transactionService;

    @Autowired
    public PayEventController(PayEventProducer producer, ITransactionService transactionService){
        this.payEventProducer = producer;
        this.transactionService = transactionService;
    }

    @PostMapping("/v1/payEvent")
    public ResponseEntity<Transaction> postDepositEvent(@RequestBody Transaction transactionEvent) throws JsonProcessingException {
        log.info("Before SQL transaction");
        Transaction transql = transactionService.save(transactionEvent);
        log.info("After transSQL");
        log.info("Before Send Pay Event");
        payEventProducer.sendPaytEvent(transql);
        log.info("After Send Deposit Event");
        return ResponseEntity.status(HttpStatus.CREATED).body(transql);
    }

    @GetMapping("/v1/findAll")
    public ResponseEntity<List<Transaction>> findAll(){
        List<Transaction> transactionList = transactionService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(transactionList);
    }

    @GetMapping("/v1/findById")
    public ResponseEntity<Transaction> findById(@RequestParam Integer id){

        Transaction transaction = transactionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }
}
