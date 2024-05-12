package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.*;
import com.alejandro.app.rest.Repository.TripExpenseRepository;
import com.alejandro.app.rest.Repository.TripRepository;
import com.alejandro.app.rest.Repository.ExpenseRepository;
import com.alejandro.app.rest.Repository.UserRepository;
import com.alejandro.app.rest.Repository.TripUserRepository;
import com.alejandro.app.rest.Repository.DutyRepository;
import com.alejandro.app.rest.Repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripExpenseServiceTest {

    @Mock
    private TripExpenseRepository tripExpenseRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripUserRepository tripUserRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DutyRepository dutyRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private TripExpenseService tripExpenseService;

    @Test
    void testGetExpensesForTrip() {
        long tripId = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);

        when(tripExpenseRepository.findByTrip(trip)).thenReturn(new ArrayList<>());
        List<TripExpense> tripExpenses = tripExpenseService.getExpensesForTrip(trip);
        verify(tripExpenseRepository, times(1)).findByTrip(trip);
    }

    @Test
    void testAddExpenseToTrip() {
        long tripId = 1L;
        long expenseId = 2L;
        String email = "user@example.com";

        Trip trip = new Trip();
        trip.setId(tripId);

        Expense expense = new Expense();
        expense.setId(expenseId);

        User currentUser = new User();
        currentUser.setEmail(email);

        List<TripUser> tripUsers = new ArrayList<>();

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(tripUserRepository.findByTrip(trip)).thenReturn(tripUsers);

        tripExpenseService.addExpenseToTrip(tripId, expenseId, email);

        verify(tripRepository, times(1)).findById(tripId);
        verify(expenseRepository, times(1)).findById(expenseId);
        verify(userRepository, times(1)).findByEmail(email);
        verify(tripExpenseRepository, times(1)).save(any(TripExpense.class));
        verify(dutyRepository, times(tripUsers.size())).save(any(Duty.class));
        verify(notificationRepository, times(tripUsers.size())).save(any(Notification.class));
    }
}

