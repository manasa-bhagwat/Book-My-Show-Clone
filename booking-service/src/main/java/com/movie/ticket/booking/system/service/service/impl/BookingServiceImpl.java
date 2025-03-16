package com.movie.ticket.booking.system.service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.entity.BookingEntity;
import com.movie.ticket.booking.system.service.enums.BookingStatus;
import com.movie.ticket.booking.system.service.exception.BookingsException;
import com.movie.ticket.booking.system.service.kafka.publisher.BookingServiceKafkaPublisher;
import com.movie.ticket.booking.system.service.repo.BookingRepo;
import com.movie.ticket.booking.system.service.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final BookingServiceKafkaPublisher bookingServiceKafkaPublisher;


    public BookingServiceImpl(BookingRepo bookingRepo, BookingServiceKafkaPublisher bookingServiceKafkaPublisher) {
        this.bookingRepo = bookingRepo;
        this.bookingServiceKafkaPublisher = bookingServiceKafkaPublisher;
    }


    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) throws JsonProcessingException {
        log.info("Service Layer: BookingServiceImpl createBooking method with request data : {}", bookingDTO);

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
    @Cacheable(value = "bookings", key = "#bookingId")
    public BookingDTO getBookingDetails(@PathVariable UUID bookingId) throws BookingsException {
        log.info("Service Layer: Fetching booking details from DB  for bookingId: {}", bookingId);

        BookingEntity entity = this.bookingRepo
                .findById(bookingId)
                .orElseThrow(() -> new BookingsException("No booking details found for bookingId: " + bookingId));

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
                .orElseThrow(() -> new BookingsException("No booking details found with id: " + bookingDTO.getBookingId()));

        bookingEntity.setBookingStatus(bookingDTO.getBookingStatus());
    }

    @Override
    @Cacheable(value = "bookings")
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        log.info("Service Layer: Fetching all bookings from DB with page: {} and size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<BookingEntity> bookingEntities = bookingRepo.findAll(pageable);

        if (bookingEntities.isEmpty()) {
            log.warn("No bookings found in the database for the given page and size.");
            return Page.empty();
        }

        Page<BookingDTO> bookingDTOPage = bookingEntities.map(entity -> {
            try {
                BookingDTO bookingDTO = BookingDTO.builder()
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

                log.debug("Mapped BookingEntity to BookingDTO: {}", bookingDTO);
                return bookingDTO;

            } catch (Exception e) {
                log.error("Error while mapping BookingEntity to BookingDTO for bookingId: {}",
                        entity.getBookingId(), e);
                throw new RuntimeException("Failed to map BookingEntity to BookingDTO", e);
            }
        });

        log.info("Successfully fetched {} bookings from DB.", bookingDTOPage.getTotalElements());
        return bookingDTOPage;
    }



}
