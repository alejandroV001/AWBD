package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Repository.ExpenseRepository;
import com.alejandro.app.rest.Services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void testGetAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense( "Groceries", "USD", 50.0));
        expenses.add(new Expense( "Transportation", "EUR", 30.0));

        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getAllExpenses();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(expenseRepository, times(1)).findAll();
    }



    @Test
    void testGetExpenseById_ExpenseNotExists() {
        long expenseId = 1L;

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        Optional<Expense> result = expenseService.getExpenseById(expenseId);

        assertFalse(result.isPresent());

        verify(expenseRepository, times(1)).findById(expenseId);
    }

    @Test
    void testCreateExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setName("Shopping");
        expense.setCurrency("EUR");
        expense.setAmount(100.0);

        expenseService.createExpense(expense);

        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    void testUpdateExpense_ExpenseNotExists() {
        long expenseId = 1L;
        Expense updatedExpense = new Expense("Updated Shopping", "USD", 120.0);

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> expenseService.updateExpense(expenseId, updatedExpense));

        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void testDeleteExpense_ExpenseExists() {
        long expenseId = 1L;

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(new Expense()));

        expenseService.deleteExpense(expenseId);

        verify(expenseRepository, times(1)).deleteById(expenseId);
    }

    @Test
    void testDeleteExpense_ExpenseNotExists() {
        long expenseId = 1L;

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> expenseService.deleteExpense(expenseId));

        verify(expenseRepository, never()).deleteById(expenseId);
    }
}

