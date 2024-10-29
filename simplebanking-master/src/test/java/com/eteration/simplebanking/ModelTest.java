package com.eteration.simplebanking;



import static org.junit.jupiter.api.Assertions.assertTrue;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelTest {

    @Test
    public void testCreateAccountAndSetBalance0() {
        Account account = new Account("Fatih Çakar", "1784592");
        assertTrue(account.getOwner().equals("Fatih Çakar"));
        assertTrue(account.getAccountNumber().equals("1784592"));
        assertTrue(account.getBalance() == 0);
    }

    @Test
    public void testWithdrawFromBankAccount() throws InsufficientBalanceException {
        Account account = new Account("Ömer Demircan", "98341");
        account.deposit(100);
        assertTrue(account.getBalance() == 100);
        account.withdraw(50);
        assertTrue(account.getBalance() == 50);
    }
    @Test
    public void testDepositIntoBankAccount() {
        Account account = new Account("Ömer Demircan", "98341");
        account.deposit(100);
        assertTrue(account.getBalance() == 100);
    }


    @Test
    public void testWithdrawException() {
        Assertions.assertThrows( InsufficientBalanceException.class, () -> {
            Account account = new Account("Ömer Demircan", "98341");
            account.deposit(100);
            account.withdraw(500);
        });

    }

    @Test
    public void testTransactions() throws InsufficientBalanceException {
        // Create account
        Account account = new Account("Rabia Gök", "123456");
        assertTrue(account.getTransactions().size() == 0);

        // Deposit Transaction
        DepositTransaction depositTrx = new DepositTransaction(100);
        assertTrue(depositTrx.getDate() != null);
        account.post(depositTrx);
        assertTrue(account.getBalance() == 100);
        assertTrue(account.getTransactions().size() == 1);

        // Withdrawal Transaction
        WithdrawalTransaction withdrawalTrx = new WithdrawalTransaction(60);
        assertTrue(withdrawalTrx.getDate() != null);
        account.post(withdrawalTrx);
        assertTrue(account.getBalance() == 40);
        assertTrue(account.getTransactions().size() == 2);
    }
}