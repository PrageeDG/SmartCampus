package com.sliit.smartcampus.ticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket_attachments")
@Data
public class TicketAttachment {

    @Id
    private String id;

    private String fileName;

    private String storedName;

    private String mimeType;
    private long fileSize;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    private Ticket ticket;
}
