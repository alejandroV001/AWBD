package com.alejandro.app.rest.Controllers;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Services.TripService;
import com.alejandro.app.rest.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private TripService tripService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);

    @PostMapping("/create")
    public ResponseEntity<String> createTrip(@RequestBody Trip trip) {
        try {
            String adminEmail = trip.getAdmin().getEmail();
            User adminUser = userService.getUserByEmail(adminEmail);
            if (adminUser == null) {
                String message = "Admin user with email " + adminEmail + " not found.";
                logger.error(message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Admin user with email " + adminEmail + " not found.");
            }
            trip.setAdmin(adminUser);
            tripService.createTrip(trip);
            logger.info("Trip created successfully!");

            return ResponseEntity.ok("Trip created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        logger.info("Fetching trip with id: {}", id);
        Trip trip = tripService.getTripById(id);
        if (trip != null) {
            return ResponseEntity.ok(trip);
        } else {
            logger.warn("Trip with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> addMemberToTrip(@RequestParam long tripId, @RequestParam long userId) {
        try {
            tripService.addParticipantToTrip(tripId, userId);
            logger.info("Member with userId {} added to trip with tripId {}", userId, tripId);
            return ResponseEntity.ok("Member added to trip successfully!");
        } catch (RuntimeException e) {
            logger.error("Error adding member to trip: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getMembers")
    public List<Long> getTripMembers(@RequestParam long tripId) {
        var ids = tripService.getUsers(tripId);
        return ids;
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<String> removeMemberFromTrip(@RequestParam long tripId, @RequestParam long userId) {
        try {
            tripService.removeParticipantFromTrip(tripId, userId);
            logger.info("Member with userId {} removed from trip with tripId {}", userId, tripId);
            return ResponseEntity.ok("Member removed from trip successfully!");
        } catch (RuntimeException e) {
            logger.error("Error removing member from trip: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @RequestBody Trip tripDetails) {
        logger.info("Updating trip with id: {}", id);
        Trip updatedTrip = tripService.updateTrip(id, tripDetails);
        if (updatedTrip != null) {
            return ResponseEntity.ok(updatedTrip);
        } else {
            logger.warn("Trip with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable Long id) {
        logger.info("Deleting trip with id: {}", id);
        boolean deleted = tripService.deleteTrip(id);
        if (deleted) {
            return ResponseEntity.ok("Trip deleted successfully!");
        } else {
            logger.warn("Trip with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}