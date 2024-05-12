package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.User;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.DutyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DutyServiceTest {

    @Autowired
    private DutyService dutyService;

    @MockBean
    private DutyRepository dutyRepository;

    @Test
    void testCreateDuty() {
        Duty duty = new Duty();
        duty.setId(1L);
        duty.setAmount(100.0);

        dutyService.createDuty(duty);
        var x = dutyService.getDutyById(1L);
        verify(dutyRepository, times(1)).save(duty);
    }

    @Test
    void testGetAllDebtsForUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        List<Duty> debts = new ArrayList<>();
        debts.add(new Duty(1L, "Loan", 100.0, user, null));

        when(dutyRepository.findByDebtor(user)).thenReturn(debts);

        List<Duty> result = dutyService.getAllDebtsForUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dutyRepository, times(1)).findByDebtor(user);
    }

    @Test
    void testGetAllCreditsForUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        List<Duty> credits = new ArrayList<>();
        credits.add(new Duty(1L, "Borrowed", 50.0, null, user));

        when(dutyRepository.findByCreditor(user)).thenReturn(credits);

        List<Duty> result = dutyService.getAllCreditsForUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(dutyRepository, times(1)).findByCreditor(user);
    }

    @Test
    void testGetAllDebtsAndCreditsForUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        List<Duty> debtsAsDebtor = new ArrayList<>();
        debtsAsDebtor.add(new Duty(1L, "Loan", 100.0, user, null));

        List<Duty> debtsAsCreditor = new ArrayList<>();
        debtsAsCreditor.add(new Duty(2L, "Borrowed", 50.0, null, user));

        when(dutyRepository.findByDebtor(user)).thenReturn(debtsAsDebtor);
        when(dutyRepository.findByCreditor(user)).thenReturn(debtsAsCreditor);

        List<Duty> result = dutyService.getAllDebtsAndCreditsForUser(user);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(dutyRepository, times(1)).findByDebtor(user);
        verify(dutyRepository, times(1)).findByCreditor(user);
    }

    @Test
    void testGetDutyById_DutyExists() {
        long dutyId = 1L;
        Duty duty = new Duty(dutyId, "Loan", 100.0, new User(), null);

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(duty));

        Optional<Duty> result = dutyService.getDutyById(dutyId);

        assertTrue(result.isPresent());
        assertEquals(dutyId, result.get().getId());

        verify(dutyRepository, times(1)).findById(dutyId);
    }

    @Test
    void testGetDutyById_DutyNotExists() {
        long dutyId = 1L;

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.empty());

        Optional<Duty> result = dutyService.getDutyById(dutyId);

        assertFalse(result.isPresent());

        verify(dutyRepository, times(1)).findById(dutyId);
    }

    @Test
    void testUpdateDuty_DutyNotExists() {
        long dutyId = 1L;
        Duty updatedDuty = new Duty(dutyId, "Updated Loan", 150.0, new User(), null);

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dutyService.updateDuty(dutyId, updatedDuty));

        verify(dutyRepository, never()).save(updatedDuty);
    }

    @Test
    void testDeleteDuty_DutyExists() {
        long dutyId = 1L;

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(new Duty()));

        dutyService.deleteDuty(dutyId);

        verify(dutyRepository, times(1)).deleteById(dutyId);
    }

    @Test
    void testDeleteDuty_DutyNotExists() {
        long dutyId = 1L;

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dutyService.deleteDuty(dutyId));

        verify(dutyRepository, never()).deleteById(dutyId);
    }

}