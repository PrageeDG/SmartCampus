package com.sliit.smartcampus.booking.waitlist.model;

import com.sliit.smartcampus.resource.entity.Resource;
import com.sliit.smartcampus.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "waitlist_entries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaitlistEntry {

    @Id
    private UUID id;

    private Resource resource;

    private User user;

    private String userEmail;

    private String userName;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String purpose;

    private Integer expectedAttendees;

    @Builder.Default
    private WaitlistStatus status = WaitlistStatus.WAITING;

    private Integer position;

    private LocalDateTime notifiedAt;

    private LocalDateTime expiresAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
