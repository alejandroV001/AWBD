package com.alejandro.app.rest.Services;

import com.alejandro.app.rest.Models.Notification;
import com.alejandro.app.rest.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotificationsForUser(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Notification updateNotification(long notificationId, Notification updatedNotification) {
        Optional<Notification> existingNotification = notificationRepository.findById(notificationId);
        if (existingNotification.isPresent()) {
            updatedNotification.setId(notificationId); // Ensure the ID matches
            return notificationRepository.save(updatedNotification);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }

    public void deleteNotification(long notificationId) {
        Optional<Notification> existingNotification = notificationRepository.findById(notificationId);
        if (existingNotification.isPresent()) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }

    public Optional<Notification> getNotificationById(long notificationId) {
        return notificationRepository.findById(notificationId);
    }
}
