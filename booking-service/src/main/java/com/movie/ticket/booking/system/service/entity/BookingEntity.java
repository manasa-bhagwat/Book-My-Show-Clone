package com.movie.ticket.booking.system.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie.ticket.booking.system.service.enums.BookingStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name="bookings")
@Builder
public class BookingEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    UUID bookingId;
    @Column(name = "user_id")
    String userId;
    @Column(name = "email_id")
    String emailId;
    @Column(name = "movie_id")
    Integer movieId;
    @ElementCollection
    List<String> seatsSelected;
    @Column(name = "show_date")
    LocalDate showDate;
    @Column(name = "show_time")
    LocalTime showTime;
    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;
    @Column(name = "booking_amount")
    Double bookingAmount;

}
