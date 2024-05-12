package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Notification;
import com.alejandro.app.rest.Repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testGetAllNotificationsForUser() {
        long userId = 1L;

        when(notificationRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
        List<Notification> notifications = notificationService.getAllNotificationsForUser(userId);
        verify(notificationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(new ArrayList<>());
        List<Notification> notifications = notificationService.getAllNotifications();
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testDeleteNotification() {
        long notificationId = 1L;
        var x = notificationService.getAllNotifications();
        doNothing().when(notificationRepository).deleteById(notificationId);
        notificationService.deleteNotification(notificationId);
        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

}

