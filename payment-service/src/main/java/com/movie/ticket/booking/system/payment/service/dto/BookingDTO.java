package com.movie.ticket.booking.system.payment.service.dto;

import com.movie.ticket.booking.system.payment.service.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    UUID bookingId;
    private String userId;
    private Integer movieId;
    private List<String> seatsSelected;
    private LocalDate showDate;
    private LocalTime showTime;
    BookingStatus bookingStatus;
    private Double bookingAmount;
}
