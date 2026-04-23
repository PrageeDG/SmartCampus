package com.sliit.smartcampus.ticket.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@Data
public class Ticket {

    @Id
    private String id;

    private String title;

    private String description;

    private String category;
    private String priority;

    private String status;

    private String location;

    private String contactDetails;

    private String reportedBy;
    private String assignedTo;

    private String resolutionNote;

    private LocalDateTime firstResponseAt;
    private LocalDateTime resolvedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
