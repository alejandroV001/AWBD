package com.alejandro.app.rest.Services;
import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getExpenseById(long expenseId) {
        return expenseRepository.findById(expenseId);
    }

    public void createExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public Expense updateExpense(long expenseId, Expense updatedExpense) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isPresent()) {
            Expense existingExpense = optionalExpense.get();
            existingExpense.setName(updatedExpense.getName());
            existingExpense.setCurrency(updatedExpense.getCurrency());
            existingExpense.setAmount(updatedExpense.getAmount());
            return expenseRepository.save(existingExpense);
        } else {
            throw new RuntimeException("Cheltuiala nu există.");
        }
    }

    public void deleteExpense(long expenseId) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isPresent()) {
            expenseRepository.deleteById(expenseId);
        } else {
            throw new RuntimeException("Cheltuiala nu există.");
        }
    }
}
