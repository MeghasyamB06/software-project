
        package com.bloodlink.backend.repository;

import com.bloodlink.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(
            Integer userId
    );

    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(
            Integer userId,
            Boolean isRead
    );

    Optional<Notification> findByNotificationIdAndUserId(
            Integer notificationId,
            Integer userId
    );

    long countByUserIdAndIsRead(
            Integer userId,
            Boolean isRead
    );
}
