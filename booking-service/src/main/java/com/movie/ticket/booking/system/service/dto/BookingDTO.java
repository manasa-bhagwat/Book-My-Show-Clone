package com.movie.ticket.booking.system.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie.ticket.booking.system.service.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO implements Serializable{

    UUID bookingId;

    @NotBlank(message = "Please provide user id.")
    private String userId;

    @NotNull(message = "Please provide a movie id.")
    @Positive(message = "Please provide a valid movie id.")
    private Integer movieId;

    @NotNull(message = "Please provide a email id.")
    private String emailId;

    @NotNull(message = "Please select at least 1 seat to make a booking.")
    @Size(min = 1, message = "Please select at least 1 seat to make a booking.")
    private List<@NotBlank(message = "Seat number cannot be blank.") String> seatsSelected;

    @NotNull(message = "Please select the show date.")
    private LocalDate showDate;

    @NotNull(message = "Please select the show time.")
    private LocalTime showTime;

    BookingStatus bookingStatus;

    @NotNull(message = "Please mention booking amount.")
    @Positive(message = "Booking amount must be a positive value.")
    private Double bookingAmount;
}
