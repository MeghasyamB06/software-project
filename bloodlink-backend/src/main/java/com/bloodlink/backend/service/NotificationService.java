
        package com.bloodlink.backend.service;

import com.bloodlink.backend.entity.Notification;
import com.bloodlink.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Creates a notification for the hospital user when
     * a donor accepts or declines an emergency request.
     */
    @Transactional
    public Notification createDonorResponseNotification(
            Integer hospitalUserId,
            Integer requestId,
            String donorName,
            String responseStatus,
            String bloodGroup) {

        if (hospitalUserId == null || requestId == null) {
            throw new IllegalArgumentException(
                    "Hospital user ID and request ID are required.");
        }

        boolean accepted =
                "ACCEPTED".equalsIgnoreCase(responseStatus);

        if (!accepted &&
                !"DECLINED".equalsIgnoreCase(responseStatus)) {
            throw new IllegalArgumentException(
                    "Response must be ACCEPTED or DECLINED.");
        }

        String safeDonorName =
                (donorName == null || donorName.isBlank())
                        ? "A donor"
                        : donorName.trim();

        String safeBloodGroup =
                (bloodGroup == null || bloodGroup.isBlank())
                        ? "the requested blood group"
                        : bloodGroup.trim();

        Notification notification = new Notification();

        notification.setUserId(hospitalUserId);
        notification.setRequestId(requestId);

        notification.setTitle(
                accepted
                        ? "Donor Accepted Blood Request"
                        : "Donor Declined Blood Request"
        );

        notification.setMessage(
                safeDonorName
                        + (accepted
                        ? " accepted your emergency blood request for "
                        : " declined your emergency blood request for ")
                        + safeBloodGroup
                        + ". Request ID: "
                        + requestId
                        + "."
        );

        notification.setNotificationType(
                accepted
                        ? "DONOR_ACCEPTED"
                        : "DONOR_DECLINED"
        );

        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    /**
     * Creates a notification for a donor when a new compatible
     * emergency request is matched to that donor.
     */
    @Transactional
    public Notification createDonorEmergencyRequestNotification(
            Integer donorUserId,
            Integer requestId,
            String bloodGroup,
            String emergencyLevel,
            String location) {

        if (donorUserId == null || requestId == null) {
            throw new IllegalArgumentException(
                    "Donor user ID and request ID are required.");
        }

        String safeBloodGroup =
                (bloodGroup == null || bloodGroup.isBlank())
                        ? "blood"
                        : bloodGroup.trim().toUpperCase();

        String safeLevel =
                (emergencyLevel == null || emergencyLevel.isBlank())
                        ? "EMERGENCY"
                        : emergencyLevel.trim().toUpperCase();

        String safeLocation =
                (location == null || location.isBlank())
                        ? ""
                        : " Location: " + location.trim() + ".";

        Notification notification = new Notification();
        notification.setUserId(donorUserId);
        notification.setRequestId(requestId);
        notification.setTitle("New Emergency Blood Request");
        notification.setMessage(
                safeLevel + " request for " + safeBloodGroup
                        + " blood matches your donor profile. Please open BloodLink "
                        + "to review the request and respond."
                        + safeLocation
                        + " Request ID: " + requestId + "."
        );
        notification.setNotificationType("EMERGENCY_REQUEST_MATCH");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    /**
     * Retrieves all notifications for a user.
     */
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(
            Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required.");
        }

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Retrieves unread notifications for a user.
     */
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(
            Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required.");
        }

        return notificationRepository
                .findByUserIdAndIsReadOrderByCreatedAtDesc(
                        userId, false);
    }

    /**
     * Counts unread notifications for a user.
     */
    @Transactional(readOnly = true)
    public long countUnreadNotifications(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required.");
        }

        return notificationRepository
                .countByUserIdAndIsRead(userId, false);
    }

    /**
     * Marks a notification as read, but only when it
     * belongs to the specified user.
     */
    @Transactional
    public Notification markAsRead(
            Integer notificationId,
            Integer userId) {

        Notification notification =
                notificationRepository
                        .findByNotificationIdAndUserId(
                                notificationId, userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found."));

        notification.setIsRead(true);

        return notificationRepository.save(notification);
    }
}
