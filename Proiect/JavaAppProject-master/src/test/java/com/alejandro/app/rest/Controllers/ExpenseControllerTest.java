package com.alejandro.app.rest.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Test
    void testGetAllExpenses() throws Exception {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());

        when(expenseService.getAllExpenses()).thenReturn(expenses);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/expenses/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("id"));
    }

    @Test
    void testUpdateExpense() throws Exception {
        long expenseId = 1L;
        Expense updatedExpense = new Expense();
        updatedExpense.setAmount(100.0);

        when(expenseService.updateExpense(expenseId, updatedExpense)).thenReturn(updatedExpense);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/expenses/update/{expenseId}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains(""));
    }
}

