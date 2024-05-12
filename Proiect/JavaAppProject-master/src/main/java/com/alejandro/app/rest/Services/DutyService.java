package com.alejandro.app.rest.Services;
import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.Expense;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DutyService {

    @Autowired
    private DutyRepository dutyRepository;
    public void createDuty(Duty duty) {
        dutyRepository.save(duty);
    }

    public List<Duty> getAllDebtsForUser(User user) {
        return dutyRepository.findByDebtor(user);
    }
    public List<Duty> getAllCreditsForUser(User user) {
        return dutyRepository.findByCreditor(user);
    }
    public List<Duty> getAllDebtsAndCreditsForUser(User user) {
        List<Duty> debtsAsDebtor = dutyRepository.findByDebtor(user);
        List<Duty> debtsAsCreditor = dutyRepository.findByCreditor(user);
        debtsAsDebtor.addAll(debtsAsCreditor);
        return debtsAsDebtor;
    }
    public Optional<Duty> getDutyById(long dutyId) {
        return dutyRepository.findById(dutyId);
    }

    public Duty updateDuty(long dutyId, Duty updatedDuty) {
        Optional<Duty> existingDuty = dutyRepository.findById(dutyId);
        if (existingDuty.isPresent()) {
            updatedDuty.setId(dutyId); // Ensure the ID matches
            return dutyRepository.save(updatedDuty);
        } else {
            throw new RuntimeException("Duty not found with ID: " + dutyId);
        }
    }

    public void deleteDuty(long dutyId) {
        Optional<Duty> existingDuty = dutyRepository.findById(dutyId);
        if (existingDuty.isPresent()) {
            dutyRepository.deleteById(dutyId);
        } else {
            throw new RuntimeException("Duty not found with ID: " + dutyId);
        }
    }
}
