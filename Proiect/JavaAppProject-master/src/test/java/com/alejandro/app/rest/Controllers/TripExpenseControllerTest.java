package com.alejandro.app.rest.Controllers;

import static org.mockito.Mockito.*;

import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripExpense;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.TripRepository;
import com.alejandro.app.rest.Services.ExpenseService;
import com.alejandro.app.rest.Services.TripExpenseService;
import com.alejandro.app.rest.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TripExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripExpenseService tripExpenseService;

    @MockBean
    private UserService userService;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private TripRepository tripRepository;

    @Test
    void testGetExpensesForTrip() throws Exception {
        long tripId = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(tripExpenseService.getExpensesForTrip(trip)).thenReturn(new ArrayList<>());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trip-expenses/by-trip/{tripId}", tripId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(tripRepository, times(1)).findById(tripId);
        verify(tripExpenseService, times(1)).getExpensesForTrip(trip);
    }

    @Test
    void testAddExpenseToTrip() throws Exception {
        TripExpense tripExpense = new TripExpense();
        tripExpense.setTrip(new Trip());
        tripExpense.setExpense(new Expense());
        tripExpense.setUser(new User());

        doNothing().when(expenseService).createExpense(any(Expense.class));
        doNothing().when(tripExpenseService).addExpenseToTrip(anyLong(), anyLong(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/trip-expenses/addExpense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"trip\": {\"id\": 1}, \"expense\": {}, \"user\": {\"email\": \"user@example.com\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(expenseService, times(1)).createExpense(any(Expense.class));
        verify(tripExpenseService, times(1)).addExpenseToTrip(anyLong(), anyLong(), anyString());
    }
}
