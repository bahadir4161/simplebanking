package com.eteration.simplebanking.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@Data
public class Account {

    @Id
    private String accountNumber;
    private String owner;
    private double balance;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.executeOn(this);
        this.transactions.add(transaction);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if(balance < amount){
            throw new InsufficientBalanceException();
        }
        balance -= amount;
    }
}