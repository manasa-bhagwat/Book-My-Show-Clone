package com.movie.ticket.booking.system.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponseDTO {
    private Integer errorCode;  // Numeric HTTP status code
    private String errorStatusCodeDescription;  // Readable description
    private List<String> errorMessages;
    private BookingDTO bookingDTO;
}
