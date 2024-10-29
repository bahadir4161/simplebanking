package com.eteration.simplebanking.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = WithdrawalTransaction.class, name = "withdrawal"),
        @Type(value = DepositTransaction.class, name = "deposit")
})
@NoArgsConstructor
@Data
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected double amount;
    @ManyToOne
    protected Account account;
    protected Date date;
    private String approvalCode;
    public abstract void executeOn(Account account) throws InsufficientBalanceException;

    public Transaction(double amount) {
        this.date = new Date();
        this.amount = amount;
    }
}