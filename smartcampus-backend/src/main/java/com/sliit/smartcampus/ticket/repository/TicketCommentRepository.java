package com.sliit.smartcampus.ticket.repository;

import com.sliit.smartcampus.ticket.entity.TicketComment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketCommentRepository extends MongoRepository<TicketComment, String> {

    List<TicketComment> findByTicket_IdOrderByCreatedAtAsc(String ticketId);

    List<TicketComment> findByTicket_IdAndIsInternalFalseOrderByCreatedAtAsc(String ticketId);
}
