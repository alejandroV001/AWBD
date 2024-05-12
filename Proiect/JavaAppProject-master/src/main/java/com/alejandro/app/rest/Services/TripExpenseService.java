package com.alejandro.app.rest.Services;
import com.alejandro.app.rest.Models.*;
import com.alejandro.app.rest.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripExpenseService {
    @Autowired
    private TripExpenseRepository tripExpenseRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TripUserRepository tripUserRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DutyRepository dutyRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    public List<TripExpense> getExpensesForTrip(Trip trip) {
        return tripExpenseRepository.findByTrip(trip);
    }

    public void addExpenseToTrip(long tripId, long expenseId, String email) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalTrip.isPresent() && optionalExpense.isPresent()&& optionalUser.isPresent()) {
            Trip trip = optionalTrip.get();
            Expense expense = optionalExpense.get();
            User currentUser = optionalUser.get();

            TripExpense tripExpense = new TripExpense();
            tripExpense.setTrip(trip);
            tripExpense.setExpense(expense);
            tripExpense.setUser(currentUser);
            tripExpenseRepository.save(tripExpense);
            List<TripUser> tripUsers = tripUserRepository.findByTrip(trip);

            for (TripUser tripUser : tripUsers) {
                User user = tripUser.getUser();

                if (user.equals(currentUser)) {
                    continue;
                }
                Duty newDuty = new Duty();
                newDuty.setDebtor(user);
                newDuty.setCreditor(currentUser);
                newDuty.setStatus("pending");
                newDuty.setAmount(expense.getAmount() / (tripUsers.size() + 1));
                dutyRepository.save(newDuty);
                Notification notification = new Notification(user,expense,"Here is the message from duty");
                notificationRepository.save(notification);
            }


        } else {
            throw new RuntimeException("Călătoria sau cheltuiala nu există.");
        }
    }
}
