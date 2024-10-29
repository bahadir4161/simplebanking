
package com.eteration.simplebanking;

import com.eteration.simplebanking.services.AccountService;
import com.eteration.simplebanking.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testGetAccount() throws Exception {
        Account account = new Account("Yasin Bahadır", "6715-7775");
        given(accountService.findAccount("6715-7775")).willReturn(Optional.of(account));

        mockMvc.perform(get("/account/v1/6715-7775"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.owner").value("Yasin Bahadır"))
                .andExpect(jsonPath("$.accountNumber").value("6715-7775"));
    }

    @Test
    public void testCreditAccount() throws Exception {
        Account account = new Account("Yasin Bahadır", "6715-7775");
        given(accountService.findAccount("6715-7775")).willReturn(Optional.of(account));

        mockMvc.perform(post("/account/v1/credit/6715-7775")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"deposit\", \"amount\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"));
    }



    @Test
    public void testDebitAccount() throws Exception {
        Account account = new Account("Yasin Bahadır", "6715-7775");
        given(accountService.findAccount("6715-7775")).willReturn(Optional.of(account));

        mockMvc.perform(post("/account/v1/credit/6715-7775")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"deposit\", \"amount\": 1000.0}"))
                .andExpect(jsonPath("$.status").value("OK"));


        mockMvc.perform(post("/account/v1/debit/6715-7775")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"withdrawal\", \"amount\": 50.0}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"));
    }

}
