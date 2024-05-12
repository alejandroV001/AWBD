package com.alejandro.app.rest.Controllers;
import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripExpense;
import com.alejandro.app.rest.Repository.TripRepository;
import com.alejandro.app.rest.Services.ExpenseService;
import com.alejandro.app.rest.Services.TripExpenseService;
import com.alejandro.app.rest.Services.TripService;
import com.alejandro.app.rest.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/trip-expenses")
public class TripExpenseController {
    @Autowired
    private TripExpenseService tripExpenseService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private TripRepository tripRepository;
    @GetMapping("/by-trip/{tripId}")
    public ResponseEntity<List<TripExpense>> getExpensesForTrip(@PathVariable long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        Trip trip = optionalTrip.get();
        return ResponseEntity.ok(tripExpenseService.getExpensesForTrip(trip));
    }

    @PostMapping("/addExpense")
    public ResponseEntity<String> addExpenseToTrip(@RequestBody TripExpense tripExpense){
        try {
            expenseService.createExpense(tripExpense.getExpense());
            tripExpenseService.addExpenseToTrip(tripExpense.getTrip().getId(), tripExpense.getExpense().getId(), tripExpense.getUser().getEmail());
            return ResponseEntity.ok("Expense added to trip successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
