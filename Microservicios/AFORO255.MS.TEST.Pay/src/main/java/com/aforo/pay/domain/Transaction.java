package com.aforo.pay.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    private int operationId;
    @Column(name = "id_invoice")
    private int invoiceId;
    @Column(name = "amount")
    private double amount;
    @Column(name = "datetime")
    private Date transactionDate;
}
