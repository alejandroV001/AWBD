package com.alejandro.app.rest.Services;
import com.alejandro.app.rest.Models.*;
import com.alejandro.app.rest.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TripUserRepository tripUserRepository;

    public void createTrip(Trip trip) {
        tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long id) {
        Optional<Trip> tripOptional = tripRepository.findById(id);
        return tripOptional.orElse(null);
    }
    public List<Long> getUsers(Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        List<Long> ids = new ArrayList<>();

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            var tripUsers  = tripUserRepository.findByTrip(trip);
            for (TripUser tripUser : tripUsers) {
                User user1 = tripUser.getUser();
                if (user1 != null) {
                    ids.add(user1.getId());
                }
            }
        }
        return ids;
    }

    public Trip updateTrip(Long id, Trip tripDetails) {
        Optional<Trip> tripOptional = tripRepository.findById(id);
        if (tripOptional.isPresent()) {
            Trip existingTrip = tripOptional.get();
            existingTrip.setTripName(tripDetails.getTripName());
            existingTrip.setLocation(tripDetails.getLocation());
            existingTrip.setGroupSize(tripDetails.getGroupSize());
            existingTrip.setAdmin(tripDetails.getAdmin());
            return tripRepository.save(existingTrip);
        } else {
            return null;
        }
    }

    public boolean deleteTrip(Long id) {
        Optional<Trip> tripOptional = tripRepository.findById(id);
        if (tripOptional.isPresent()) {
            tripRepository.delete(tripOptional.get());
            return true;
        } else {
            return false;
        }
    }
    @Transactional
    public void addParticipantToTrip(long tripId, long userId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalTrip.isPresent() && optionalUser.isPresent()) {
            Trip trip = optionalTrip.get();
            User user = optionalUser.get();

            if (!isUserAlreadyMember(trip, user)) {
                TripUser tripUser = new TripUser();
                tripUser.setTrip(trip);
                tripUser.setUser(user);

                tripUserRepository.save(tripUser);
            } else {
                throw new RuntimeException("Utilizatorul este deja membru al călătoriei.");
            }
        } else {
            throw new RuntimeException("Călătoria sau utilizatorul nu există.");
        }
    }

    @Transactional
    public void removeParticipantFromTrip(long tripId, long userId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalTrip.isPresent() && optionalUser.isPresent()) {
            Trip trip = optionalTrip.get();
            User user = optionalUser.get();

            if (isUserAlreadyMember(trip, user)) {
                TripUser tripUser = tripUserRepository.findByTripAndUser(trip, user);

                tripUserRepository.delete(tripUser);
            }
        } else {
            throw new RuntimeException("Călătoria sau utilizatorul nu există.");
        }
    }

    private boolean isUserAlreadyMember(Trip trip, User user) {
        return tripUserRepository.findByTrip(trip).stream().anyMatch(tripUser -> tripUser.getUser().equals(user));
    }
}
