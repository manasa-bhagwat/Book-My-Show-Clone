package com.movie.ticket.booking.system.payment.service.service.impl;

import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.entity.PaymentEntity;
import com.movie.ticket.booking.system.payment.service.enums.BookingStatus;
import com.movie.ticket.booking.system.payment.service.enums.PaymentStatus;
import com.movie.ticket.booking.system.payment.service.repo.PaymentRepo;
import com.movie.ticket.booking.system.payment.service.service.PaymentService;
import com.movie.ticket.booking.system.payment.service.service.StripeApiPaymentGateway;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private StripeApiPaymentGateway stripeApiPaymentGateway;


    @Override
    @Transactional
    public BookingDTO makePayment(BookingDTO bookingDTO) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .bookingId(bookingDTO.getBookingId())
                .paymentAmount(bookingDTO.getBookingAmount())
                .build();
        this.paymentRepo.save(paymentEntity);

        bookingDTO = this.stripeApiPaymentGateway.processPayment(bookingDTO);
        if (bookingDTO.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            paymentEntity.setPaymentStatus(PaymentStatus.APPROVED);
            paymentEntity.setPaymentDateTime(LocalDateTime.now());
        } else {
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
            paymentEntity.setPaymentDateTime(LocalDateTime.now());
        }

        return bookingDTO;
    }

}
