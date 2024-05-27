package com.aninfo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long number;

    public Double amount;

    public Long accountCbu;

    public Transaction(){
    }

    public Transaction(Long accountCbu, Double amount){
        this.accountCbu = accountCbu;
        this.amount = amount;
    }

}
