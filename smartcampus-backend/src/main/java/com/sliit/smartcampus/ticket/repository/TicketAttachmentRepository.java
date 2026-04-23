package com.sliit.smartcampus.ticket.repository;

import com.sliit.smartcampus.ticket.entity.TicketAttachment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketAttachmentRepository extends MongoRepository<TicketAttachment, String> {

    long countByTicket_Id(String ticketId);

    List<TicketAttachment> findByTicket_Id(String ticketId);
}
