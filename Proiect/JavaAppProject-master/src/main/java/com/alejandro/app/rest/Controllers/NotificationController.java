package com.alejandro.app.rest.Controllers;

import com.alejandro.app.rest.Models.Notification;
import com.alejandro.app.rest.Services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@PathVariable long userId) {
        logger.info("Fetching all notifications for user with ID: {}", userId);
        List<Notification> notifications = notificationService.getAllNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        logger.info("Fetching all notifications");
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        try {
            logger.info("Creating notification: {}", notification);
            notificationService.createNotification(notification);
            logger.info("Notification created successfully with ID: {}", notification.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (RuntimeException e) {
            logger.error("Failed to create notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{notificationId}")
    public ResponseEntity<Notification> updateNotification(@PathVariable long notificationId, @RequestBody Notification updatedNotification) {
        try {
            logger.info("Updating notification with ID: {}", notificationId);
            Notification notification = notificationService.updateNotification(notificationId, updatedNotification);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            logger.error("Failed to update notification with ID {}: {}", notificationId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable long notificationId) {
        try {
            logger.info("Deleting notification with ID: {}", notificationId);
            notificationService.deleteNotification(notificationId);
            logger.info("Notification deleted successfully with ID: {}", notificationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Failed to delete notification with ID {}: {}", notificationId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}