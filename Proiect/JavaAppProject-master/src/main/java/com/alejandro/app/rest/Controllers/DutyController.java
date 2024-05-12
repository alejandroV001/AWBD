package com.alejandro.app.rest.Controllers;

import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.UserRepository;
import com.alejandro.app.rest.Services.DutyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/duties")
public class DutyController {

    @Autowired
    private DutyService dutyService;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(DutyController.class);

    @GetMapping("/debts/{userId}")
    public ResponseEntity<List<Duty>> getDebtsForUser(@PathVariable long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return ResponseEntity.ok(dutyService.getAllDebtsForUser(user));

        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/credits/{userId}")
    public ResponseEntity<List<Duty>> getCreditsForUser(@PathVariable long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return ResponseEntity.ok(dutyService.getAllCreditsForUser(user));

        }
        return ResponseEntity.ok(null);
    }
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Duty>> getCreditsAndDebtsForUser(@PathVariable long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return ResponseEntity.ok(dutyService.getAllDebtsAndCreditsForUser(user));

        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/create")
    public ResponseEntity<Duty> createDuty(@RequestBody Duty duty) {
        try {
            dutyService.createDuty(duty);
            logger.info("Duty created successfully with ID: {}", duty.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(duty);
        } catch (RuntimeException e) {
            logger.error("Failed to create duty: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{dutyId}")
    public ResponseEntity<Duty> updateDuty(@PathVariable long dutyId, @RequestBody Duty updatedDuty) {
        try {
            Duty duty = dutyService.updateDuty(dutyId, updatedDuty);
            logger.info("Duty with ID {} updated successfully", dutyId);
            return ResponseEntity.ok(duty);
        } catch (RuntimeException e) {
            logger.error("Failed to update duty with ID {}: {}", dutyId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{dutyId}")
    public ResponseEntity<Void> deleteDuty(@PathVariable long dutyId) {
        try {
            dutyService.deleteDuty(dutyId);
            logger.info("Duty with ID {} deleted successfully", dutyId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Failed to delete duty with ID {}: {}", dutyId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
