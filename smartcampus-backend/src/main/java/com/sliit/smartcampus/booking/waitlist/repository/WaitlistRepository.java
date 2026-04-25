package com.sliit.smartcampus.booking.waitlist.repository;

import com.sliit.smartcampus.booking.waitlist.model.WaitlistEntry;
import com.sliit.smartcampus.booking.waitlist.model.WaitlistStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistRepository extends MongoRepository<WaitlistEntry, UUID> {

    List<WaitlistEntry> findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusOrderByPositionAsc(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            WaitlistStatus status
    );

    default Optional<WaitlistEntry> findFirstWaiting(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusOrderByPositionAsc(
                resourceId,
                date,
                startTime,
                endTime,
                WaitlistStatus.WAITING
        ).stream().findFirst();
    }

    boolean existsByUser_IdAndResource_IdAndDateAndStartTimeAndEndTimeAndStatusIn(
            UUID userId,
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            List<WaitlistStatus> statuses
    );

    default boolean existsActiveEntryForUserAndSlot(
            UUID userId,
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return existsByUser_IdAndResource_IdAndDateAndStartTimeAndEndTimeAndStatusIn(
                userId,
                resourceId,
                date,
                startTime,
                endTime,
                List.of(WaitlistStatus.WAITING, WaitlistStatus.NOTIFIED)
        );
    }

    List<WaitlistEntry> findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusInOrderByPositionDesc(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            List<WaitlistStatus> statuses
    );

    default Integer findMaxPosition(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusInOrderByPositionDesc(
                resourceId,
                date,
                startTime,
                endTime,
                List.of(WaitlistStatus.WAITING, WaitlistStatus.NOTIFIED)
        ).stream().findFirst().map(WaitlistEntry::getPosition).orElse(0);
    }

    List<WaitlistEntry> findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusAndPositionGreaterThanOrderByPositionAsc(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            WaitlistStatus status,
            Integer position
    );

    default List<WaitlistEntry> findWaitingEntriesAfterPosition(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Integer position
    ) {
        return findByResource_IdAndDateAndStartTimeAndEndTimeAndStatusAndPositionGreaterThanOrderByPositionAsc(
                resourceId,
                date,
                startTime,
                endTime,
                WaitlistStatus.WAITING,
                position
        );
    }

    List<WaitlistEntry> findByStatusAndExpiresAtBefore(WaitlistStatus status, LocalDateTime now);

    default List<WaitlistEntry> findExpiredNotifications(LocalDateTime now) {
        return findByStatusAndExpiresAtBefore(WaitlistStatus.NOTIFIED, now);
    }

    List<WaitlistEntry> findByUser_IdOrderByCreatedAtDesc(UUID userId);

    default List<WaitlistEntry> findAllWithFilters(WaitlistStatus status, UUID resourceId, LocalDate date) {
        Stream<WaitlistEntry> stream = findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream();

        if (status != null) {
            stream = stream.filter(w -> status.equals(w.getStatus()));
        }
        if (resourceId != null) {
            stream = stream.filter(w -> w.getResource() != null && resourceId.equals(w.getResource().getId()));
        }
        if (date != null) {
            stream = stream.filter(w -> date.equals(w.getDate()));
        }

        return stream.sorted(Comparator.comparing(WaitlistEntry::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    long countByUser_IdAndStatusIn(UUID userId, List<WaitlistStatus> statuses);

    default long countActiveForUser(UUID userId) {
        return countByUser_IdAndStatusIn(userId, List.of(WaitlistStatus.WAITING, WaitlistStatus.NOTIFIED));
    }
}
