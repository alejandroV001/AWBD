package com.alejandro.app.rest.Controllers;
import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.User;
import com.alejandro.app.rest.Repository.UserRepository;
import com.alejandro.app.rest.Services.DutyService;
import com.alejandro.app.rest.Services.DutyServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(DutyController.class)
public class DutyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DutyService dutyService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetDebtsForUser() throws Exception {
        long userId = 1L;
        User user = new User();
        Duty duty = new Duty();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(dutyService.getAllDebtsForUser(user)).thenReturn(Arrays.asList(duty));

        mockMvc.perform(MockMvcRequestBuilders.get("/duties/debts/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(duty.getId()));
    }

    @Test
    public void testGetCreditsForUser() throws Exception {
        long userId = 1L;
        User user = new User();
        Duty duty = new Duty();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(dutyService.getAllCreditsForUser(user)).thenReturn(Arrays.asList(duty));

        mockMvc.perform(MockMvcRequestBuilders.get("/duties/credits/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(duty.getId()));
    }

    @Test
    public void testGetCreditsAndDebtsForUser() throws Exception {
        long userId = 1L;
        User user = new User();
        Duty duty = new Duty();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(dutyService.getAllDebtsAndCreditsForUser(user)).thenReturn(Arrays.asList(duty));

        mockMvc.perform(MockMvcRequestBuilders.get("/duties/all/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(duty.getId()));
    }

    @Test
    public void testDeleteDuty() throws Exception {
        long dutyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/duties/delete/{dutyId}", dutyId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(dutyService, Mockito.times(1)).deleteDuty(dutyId);
    }
}
