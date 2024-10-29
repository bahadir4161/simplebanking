package com.eteration.simplebanking.controller;

import lombok.Data;

@Data
public class TransactionStatus {

    private String status;
    private String approvalCode;

    public TransactionStatus(String status,String approvalCode) {
        this.status = status;
        this.approvalCode = approvalCode;
    }
}