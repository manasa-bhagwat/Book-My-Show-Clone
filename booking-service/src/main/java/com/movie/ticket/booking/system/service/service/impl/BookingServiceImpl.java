package com.movie.ticket.booking.system.service.service.impl;

import com.movie.ticket.booking.system.service.brokers.PaymentServiceBroker;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.entity.BookingEntity;
import com.movie.ticket.booking.system.service.enums.BookingStatus;
import com.movie.ticket.booking.system.service.repo.BookingRepo;
import com.movie.ticket.booking.system.service.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    PaymentServiceBroker paymentService;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {

        log.info("Entered into Service Layer: BookingServiceImpl createBooking method with request data : {}", bookingDTO);

        BookingEntity bookingEntity = BookingEntity.builder()
                .userId(bookingDTO.getUserId())
                .movieId(bookingDTO.getMovieId())
                .seatsSelected(bookingDTO.getSeatsSelected())
                .showDate(bookingDTO.getShowDate())
                .showTime(bookingDTO.getShowTime())
                .bookingAmount(bookingDTO.getBookingAmount())
                .build();

        this.bookingRepo.save(bookingEntity);

        bookingDTO.setBookingId(bookingEntity.getBookingId());
        bookingDTO.setBookingStatus(BookingStatus.PENDING);

        //call payment-service
        BookingDTO bookingDTOResponse = this.paymentService.makePayment(bookingDTO);
        bookingEntity.setBookingStatus(bookingDTOResponse.getBookingStatus());

        return BookingDTO.builder()
                .bookingId(bookingEntity.getBookingId())
                .userId(bookingEntity.getUserId())
                .movieId(bookingEntity.getMovieId())
                .seatsSelected(bookingEntity.getSeatsSelected())
                .showDate(bookingEntity.getShowDate())
                .showTime(bookingEntity.getShowTime())
                .bookingStatus(bookingDTOResponse.getBookingStatus())
                .bookingAmount(bookingEntity.getBookingAmount())
                .build();
    }
}
