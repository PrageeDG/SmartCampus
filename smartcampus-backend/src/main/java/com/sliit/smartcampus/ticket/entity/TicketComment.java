package com.sliit.smartcampus.ticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket_comments")
@Data
public class TicketComment {

    @Id
    private String id;

    private String content;

    private String authorId;

    private String authorName;
    private String authorRole;

    private boolean isInternal = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    private Ticket ticket;
}
