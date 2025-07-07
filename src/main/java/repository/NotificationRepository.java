package repository;

import model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByTargetUserIdOrTargetUserIdIsNull(Long userId);

    List<Notification> findByTargetUserIdAndReadFalse(Long userId); // ✅ for unread filter

    // Count total notifications for a given user
    long countByTargetUserId(Long userId);

    // Count unread notifications for a given user
    long countByTargetUserIdAndReadFalse(Long userId);

    int deleteByCreatedAtBefore(LocalDateTime cutoffDate);

}
