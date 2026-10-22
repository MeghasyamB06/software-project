
        package com.bloodlink.backend.controller;

import com.bloodlink.backend.entity.Notification;
import com.bloodlink.backend.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Get all notifications belonging to a user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserNotifications(
            @PathVariable Integer userId) {

        try {
            List<Notification> notifications =
                    notificationService
                            .getUserNotifications(userId);

            return ResponseEntity.ok(notifications);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Get unread notifications belonging to a user.
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadNotifications(
            @PathVariable Integer userId) {

        try {
            List<Notification> notifications =
                    notificationService
                            .getUnreadNotifications(userId);

            return ResponseEntity.ok(notifications);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Get the unread notification count for a user.
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<?> getUnreadCount(
            @PathVariable Integer userId) {

        try {
            long count = notificationService
                    .countUnreadNotifications(userId);

            return ResponseEntity.ok(count);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Mark a notification as read.
     */
    @PutMapping("/{notificationId}/read/user/{userId}")
    public ResponseEntity<?> markAsRead(
            @PathVariable Integer notificationId,
            @PathVariable Integer userId) {

        try {
            Notification notification =
                    notificationService.markAsRead(
                            notificationId, userId);

            return ResponseEntity.ok(notification);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404)
                    .body(e.getMessage());
        }
    }
}
