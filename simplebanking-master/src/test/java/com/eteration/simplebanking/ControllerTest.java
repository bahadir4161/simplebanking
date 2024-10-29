package com.eteration.simplebanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.eteration.simplebanking.controller.AccountController;
import com.eteration.simplebanking.controller.TransactionStatus;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class ControllerTests  {

    @Spy
    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService service;


    @Test
    public void givenId_Credit_thenReturnJson() throws Exception {
        Account account = new Account("Yasin Bahadır", "6715-7775");
        Optional<Account> optionalAccount = Optional.of(account);

        doReturn(optionalAccount).when(service).findAccount("6715-7775");

        ResponseEntity<TransactionStatus> result = controller.credit("6715-7775", new DepositTransaction(1000.0));

        verify(service, times(1)).findAccount("6715-7775");
        assertEquals("OK", result.getBody().getStatus());
    }
    @Test
    public void givenId_CreditAndThenDebit_thenReturnJson() throws Exception {
        Account account = new Account("Yasin Bahadır", "6715-7775");
        Optional<Account> optionalAccount = Optional.of(account);

        doReturn(optionalAccount).when(service).findAccount("6715-7775");

        ResponseEntity<TransactionStatus> result = controller.credit("6715-7775", new DepositTransaction(1000.0));
        ResponseEntity<TransactionStatus> result2 = controller.debit("6715-7775", new WithdrawalTransaction(50.0));

        verify(service, times(2)).findAccount("6715-7775");
        assertEquals("OK", result.getBody().getStatus());
        assertEquals("OK", result2.getBody().getStatus());
        assertEquals(950.0, account.getBalance(), 0.001);
    }

    @Test
    public void givenId_CreditAndThenDebitMoreGetException_thenReturnJson() throws Exception {
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            Account account = new Account("Yasin Bahadır", "6715-7775");
            Optional<Account> optionalAccount = Optional.of(account);

            doReturn(optionalAccount).when(service).findAccount("6715-7775");

            ResponseEntity<TransactionStatus> result = controller.credit("6715-7775", new DepositTransaction(1000.0));
            assertEquals("OK", result.getBody().getStatus());
            assertEquals(1000.0, account.getBalance(), 0.001);
            verify(service, times(1)).findAccount("6715-7775");

            ResponseEntity<TransactionStatus> result2 = controller.debit("6715-7775", new WithdrawalTransaction(5000.0));
        });
    }



}