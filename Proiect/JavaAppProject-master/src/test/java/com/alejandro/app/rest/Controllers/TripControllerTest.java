package com.alejandro.app.rest.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Services.TripService;
import com.alejandro.app.rest.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    @MockBean
    private UserService userService;

    @Test
    void testCreateTrip() throws Exception {
        Trip trip = new Trip();
        trip.setAdmin(new User());

        when(userService.getUserByEmail(anyString())).thenReturn(new User());
        doNothing().when(tripService).createTrip(any(Trip.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/trips/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"admin\": {\"email\": \"admin@example.com\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(userService, times(1)).getUserByEmail("admin@example.com");
        verify(tripService, times(1)).createTrip(any(Trip.class));
    }

    @Test
    void testGetAllTrips() throws Exception {
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip());

        when(tripService.getAllTrips()).thenReturn(trips);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trips/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("id"));
    }

    @Test
    public void testGetTripById() throws Exception {
        long tripId = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);

        Mockito.when(tripService.getTripById(tripId)).thenReturn(trip);

        mockMvc.perform(MockMvcRequestBuilders.get("/trips/{id}", tripId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(trip.getId()));
    }

    @Test
    public void testAddMemberToTrip() throws Exception {
        long tripId = 1L;
        long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.post("/trips/addMember")
                        .param("tripId", String.valueOf(tripId))
                        .param("userId", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Member added to trip successfully!"));
    }

    @Test
    public void testRemoveMemberFromTrip() throws Exception {
        long tripId = 1L;
        long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/trips/removeMember")
                        .param("tripId", String.valueOf(tripId))
                        .param("userId", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Member removed from trip successfully!"));
    }

    @Test
    public void testDeleteTrip() throws Exception {
        long tripId = 1L;

        Mockito.when(tripService.deleteTrip(tripId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/trips/{id}", tripId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trip deleted successfully!"));
    }

}
