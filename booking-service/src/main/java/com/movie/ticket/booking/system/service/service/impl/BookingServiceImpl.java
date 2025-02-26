package com.movie.ticket.booking.system.service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.movie.ticket.booking.system.service.brokers.PaymentServiceBroker;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.entity.BookingEntity;
import com.movie.ticket.booking.system.service.enums.BookingStatus;
import com.movie.ticket.booking.system.service.exception.BookingsException;
import com.movie.ticket.booking.system.service.kafka.publisher.BookingServiceKafkaPublisher;
import com.movie.ticket.booking.system.service.repo.BookingRepo;
import com.movie.ticket.booking.system.service.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
//    private final PaymentServiceBroker paymentService;
    private final BookingServiceKafkaPublisher bookingServiceKafkaPublisher;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) throws JsonProcessingException {

        log.info("Entered into Service Layer: BookingServiceImpl createBooking method with request data : {}", bookingDTO);

        BookingEntity bookingEntity = BookingEntity.builder()
                .userId(bookingDTO.getUserId())
                .movieId(bookingDTO.getMovieId())
                .emailId(bookingDTO.getEmailId())
                .seatsSelected(bookingDTO.getSeatsSelected())
                .showDate(bookingDTO.getShowDate())
                .showTime(bookingDTO.getShowTime())
                .bookingAmount(bookingDTO.getBookingAmount())
                .build();

        this.bookingRepo.save(bookingEntity);

        bookingDTO.setBookingId(bookingEntity.getBookingId());
        bookingDTO.setBookingStatus(BookingStatus.PENDING);

        // publish booking details to kafka topic(payment-request)
        this.bookingServiceKafkaPublisher.pushBookingDetailsToPaymentRequestTopic(bookingDTO);

        return bookingDTO;
    }

    @Override
    public BookingDTO getBookingDetails(UUID bookingId) throws BookingsException {
        BookingEntity entity = this.bookingRepo
                .findById(bookingId)
                .orElseThrow(()-> new BookingsException("No booking details found for bookingId: " + bookingId));

        return BookingDTO.builder()
                .bookingId(entity.getBookingId())
                .emailId(entity.getEmailId())
                .bookingAmount(entity.getBookingAmount())
                .bookingStatus(entity.getBookingStatus())
                .movieId(entity.getMovieId())
                .showDate(entity.getShowDate())
                .showTime(entity.getShowTime())
                .userId(entity.getUserId())
                .seatsSelected(entity.getSeatsSelected())
                .build();
    }

    @Override
    @Transactional
    public void processFinalBooking(BookingDTO bookingDTO) throws BookingsException {
        BookingEntity bookingEntity = this.bookingRepo
                .findById(bookingDTO.getBookingId())
                .orElseThrow(()-> new BookingsException("No booking details found with id: "+ bookingDTO.getBookingId()));

        bookingEntity.setBookingStatus(bookingDTO.getBookingStatus());
    }
}
