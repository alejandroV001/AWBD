package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripUser;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.TripUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripUserServiceTest {

    @Mock
    private TripUserRepository tripUserRepository;

    @InjectMocks
    private TripUserService tripUserService;

    @Test
    void testAddMemberToTrip() {
        User user = new User();
        Trip trip = new Trip();
        tripUserService.addMemberToTrip(user, trip);
        verify(tripUserRepository, times(1)).save(any(TripUser.class));
    }


}

