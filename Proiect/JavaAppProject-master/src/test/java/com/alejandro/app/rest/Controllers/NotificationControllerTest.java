package com.alejandro.app.rest.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alejandro.app.rest.Models.Notification;
import com.alejandro.app.rest.Services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testGetAllNotificationsForUser() throws Exception {
        long userId = 1L;
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());

        when(notificationService.getAllNotificationsForUser(userId)).thenReturn(notifications);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/notifications/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("id"));
    }

    @Test
    void testGetAllNotifications() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());

        when(notificationService.getAllNotifications()).thenReturn(notifications);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/notifications/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("id"));
    }

    @Test
    void testDeleteNotification() throws Exception {
        long notificationId = 1L;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/{notificationId}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        verify(notificationService, times(1)).deleteNotification(notificationId);
    }
}

