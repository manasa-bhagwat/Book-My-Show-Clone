package com.movie.ticket.booking.system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookingDTO {

    private UUID bookingId;
    private String userId;
    private Long movieId;
    private String emailId;
    private List<String> seatsSelected;
    private LocalDate showDate;
    private LocalTime showTime;
    private BookingStatus bookingStatus;
    private BigDecimal bookingAmount;
}
