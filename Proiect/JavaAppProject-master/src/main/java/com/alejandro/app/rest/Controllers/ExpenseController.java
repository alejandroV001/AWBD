package com.alejandro.app.rest.Controllers;
import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Services.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        logger.info("Fetching all expenses");
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable long expenseId) {
        logger.info("Fetching expense with ID: {}", expenseId);
        return expenseService.getExpenseById(expenseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
        try {
            expenseService.createExpense(expense);
            logger.info("Expense created successfully!");
            return ResponseEntity.ok("Expense created successfully!");
        } catch (RuntimeException e) {
            logger.error("Failed to create expense: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable long expenseId, @RequestBody Expense updatedExpense) {
        try {
            Expense expense = expenseService.updateExpense(expenseId, updatedExpense);
            logger.info("Expense with ID {} updated successfully", expenseId);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            logger.error("Failed to update expense with ID {}: {}", expenseId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long expenseId) {
        try {
            expenseService.deleteExpense(expenseId);
            logger.info("Expense with ID {} deleted successfully", expenseId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Failed to delete expense with ID {}: {}", expenseId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
