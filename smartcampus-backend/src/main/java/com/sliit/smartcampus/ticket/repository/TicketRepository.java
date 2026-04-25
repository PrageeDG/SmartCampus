package com.sliit.smartcampus.ticket.repository;

import com.sliit.smartcampus.ticket.entity.Ticket;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByReportedBy(String userId, Sort sort);

    List<Ticket> findByAssignedTo(String technicianId, Sort sort);
}
