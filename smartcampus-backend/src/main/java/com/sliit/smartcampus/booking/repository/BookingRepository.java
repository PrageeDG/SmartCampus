package com.sliit.smartcampus.booking.repository;

import com.sliit.smartcampus.booking.model.Booking;
import com.sliit.smartcampus.booking.model.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends MongoRepository<Booking, UUID> {

    List<Booking> findByResource_IdAndDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID resourceId,
            LocalDate date,
            List<BookingStatus> statuses,
            LocalTime endTime,
            LocalTime startTime
    );

    default List<Booking> findConflictingBookings(
            UUID resourceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return findByResource_IdAndDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
                resourceId,
                date,
                List.of(BookingStatus.PENDING, BookingStatus.APPROVED),
                endTime,
                startTime
        );
    }

    List<Booking> findByUser_IdOrderByCreatedAtDesc(UUID userId);

    default List<Booking> findAllWithFilters(
            BookingStatus status,
            UUID resourceId,
            LocalDate date,
            UUID userId
    ) {
        Stream<Booking> stream = findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream();

        if (status != null) {
            stream = stream.filter(b -> status.equals(b.getStatus()));
        }
        if (resourceId != null) {
            stream = stream.filter(b -> b.getResource() != null && resourceId.equals(b.getResource().getId()));
        }
        if (date != null) {
            stream = stream.filter(b -> date.equals(b.getDate()));
        }
        if (userId != null) {
            stream = stream.filter(b -> b.getUser() != null && userId.equals(b.getUser().getId()));
        }

        return stream.sorted(Comparator.comparing(Booking::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
