package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.TripRepository;
import com.alejandro.app.rest.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void testCreateTrip() {
        Trip trip = new Trip();
        tripService.createTrip(trip);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testGetAllTrips() {
        when(tripRepository.findAll()).thenReturn(List.of(new Trip(), new Trip()));
        assertEquals(2, tripService.getAllTrips().size());
        verify(tripRepository, times(1)).findAll();
    }

    @Test
    void testGetTripById_TripExists() {
        long tripId = 1L;
        Trip trip = new Trip(tripId, "Vacation", "Beach", 4, new User());

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        Trip result = tripService.getTripById(tripId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());

        verify(tripRepository, times(1)).findById(tripId);
    }

    @Test
    void testGetTripById_TripNotExists() {
        long tripId = 1L;

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        Trip result = tripService.getTripById(tripId);

        assertNull(result);

        verify(tripRepository, times(1)).findById(tripId);
    }

    @Test
    void testUpdateTrip_TripNotExists() {
        long tripId = 1L;
        Trip updatedTrip = new Trip(tripId, "Updated Vacation", "Mountain", 6, new User());

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        Trip result = tripService.updateTrip(tripId, updatedTrip);

        assertNull(result);

        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testDeleteTrip_TripExists() {
        long tripId = 1L;

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(new Trip()));

        boolean result = tripService.deleteTrip(tripId);

        assertTrue(result);

        verify(tripRepository, times(1)).delete(any(Trip.class));
    }

    @Test
    void testAddParticipantToTrip() {
        long tripId = 1L;
        long userId = 1L;

        Trip trip = new Trip(tripId, "Vacation", "Beach", 4, new User());
        User user = new User("email", "John");

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        tripService.addParticipantToTrip(tripId, userId);

        assertTrue(trip.getParticipants().contains(user));

        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testRemoveParticipantFromTrip() {
        long tripId = 1L;
        long userId = 1L;

        Trip trip = new Trip(tripId, "Vacation", "Beach", 4, new User("email", "John"));
        User user = new User("email", "John");

        trip.addParticipant(user);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        tripService.removeParticipantFromTrip(tripId, userId);

        assertFalse(trip.getParticipants().contains(user));

        verify(tripRepository, times(1)).save(trip);
    }
}
