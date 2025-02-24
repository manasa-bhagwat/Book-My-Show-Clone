package com.movie.ticket.booking.system.service.repo;

import com.movie.ticket.booking.system.service.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepo extends CrudRepository<BookingEntity, UUID> {
}
