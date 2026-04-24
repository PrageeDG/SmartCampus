package com.sliit.smartcampus.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Page<Notification> findByRecipient_IdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByRecipient_IdAndReadFalse(UUID userId);

    List<Notification> findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(UUID userId);

    default void markAllReadForUser(UUID userId) {
        List<Notification> unread = new ArrayList<>(findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(userId));
        unread.forEach(n -> n.setRead(true));
        saveAll(unread);
    }
}
