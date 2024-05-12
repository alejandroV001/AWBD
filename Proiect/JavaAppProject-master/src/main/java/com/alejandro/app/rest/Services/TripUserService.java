package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripUser;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.TripUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripUserService {

    @Autowired
    private TripUserRepository tripUserRepository;

    public void addMemberToTrip(User user, Trip trip) {
        TripUser tripUser = new TripUser();
        tripUser.setUser(user);
        tripUser.setTrip(trip);
        tripUserRepository.save(tripUser);
    }

}
