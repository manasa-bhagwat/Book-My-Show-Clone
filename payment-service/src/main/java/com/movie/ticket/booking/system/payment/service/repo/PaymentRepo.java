package com.movie.ticket.booking.system.payment.service.repo;

import com.movie.ticket.booking.system.payment.service.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PaymentRepo extends CrudRepository<PaymentEntity, UUID> {
}
