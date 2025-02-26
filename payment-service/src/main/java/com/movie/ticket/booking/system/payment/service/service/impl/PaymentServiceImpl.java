package com.movie.ticket.booking.system.payment.service.service.impl;

import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.entity.PaymentEntity;
import com.movie.ticket.booking.system.payment.service.enums.BookingStatus;
import com.movie.ticket.booking.system.payment.service.enums.PaymentStatus;
import com.movie.ticket.booking.system.payment.service.kafka.publisher.PaymentServiceKafkaPublisher;
import com.movie.ticket.booking.system.payment.service.repo.PaymentRepo;
import com.movie.ticket.booking.system.payment.service.service.PaymentService;
import com.movie.ticket.booking.system.payment.service.service.StripeApiPaymentGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final StripeApiPaymentGateway stripeApiPaymentGateway;
    private final PaymentServiceKafkaPublisher paymentServiceKafkaPublisher;

    @Override
    @Transactional
    public void processPayment(BookingDTO bookingDTO) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .bookingId(bookingDTO.getBookingId())
                .emailId(bookingDTO.getEmailId())
                .paymentAmount(bookingDTO.getBookingAmount())
                .build();
        this.paymentRepo.save(paymentEntity);

        bookingDTO = this.stripeApiPaymentGateway.makePayment(bookingDTO);
        paymentEntity.setPaymentDateTime(LocalDateTime.now());


        if (bookingDTO.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            paymentEntity.setPaymentStatus(PaymentStatus.APPROVED);
        } else {
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
        }

        this.paymentServiceKafkaPublisher.pushBookingDetailsToPaymentResponseTopic(bookingDTO);
    }

}
