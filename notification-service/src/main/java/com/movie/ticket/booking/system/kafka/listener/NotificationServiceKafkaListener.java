package com.movie.ticket.booking.system.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.booking.system.dto.BookingDTO;
import com.movie.ticket.booking.system.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceKafkaListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-response", groupId = "payment-response-group2")
    public void pullBookingDetailsFromPaymentResponseTopic(String bookingsJson) {
        log.info("Received booking details {} from payment-response topic", bookingsJson);
        try {

            //Deserialise JSON to Java DTO object
            BookingDTO bookingDTO = objectMapper.readValue(bookingsJson, BookingDTO.class);

            //send email logic
            String recipientEmail = bookingDTO.getEmailId();
            String subject = "Booking confirmation";
            String messageBody = String.format(
                    "<!DOCTYPE html>" +
                            "<html lang='en'>" +
                            "<head>" +
                            "<meta charset='UTF-8'>" +
                            "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                            "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'>" +
                            "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>" +
                            "<style>" +
                            "body { font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px; text-align: center; }" +
                            ".container { max-width: 600px; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); }" +
                            "h2 { color: #dc3545; }" +
                            ".details { background: #f0f0f0; padding: 15px; border-radius: 8px; border-left: 5px solid #dc3545; text-align: left; }" +
                            ".details p { margin: 8px 0; font-size: 16px; }" +
                            ".cta-button { display: inline-block; padding: 10px 20px; margin-top: 15px; color: white; background-color: #dc3545; text-decoration: none; border-radius: 5px; font-weight: bold; }" +
                            ".cta-button:hover { background-color: #c82333; }" +
                            ".footer { font-size: 14px; color: #666; margin-top: 20px; }" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='container'>" +
                            "<h2><i class='fas fa-ticket-alt'></i> Booking Confirmation</h2>" +
                            "<p>Dear <strong>%s</strong>,</p>" +
                            "<p>Your movie booking has been <strong>confirmed!</strong> 🎉</p>" +
                            "<div class='details'>" +
                            "<p><i class='fas fa-film'></i> <strong>Movie:</strong> %s</p>" +
                            "<p><i class='fas fa-chair'></i> <strong>Seats:</strong> %s</p>" +
                            "<p><i class='fas fa-calendar-alt'></i> <strong>Show Date:</strong> %s</p>" +
                            "<p><i class='fas fa-clock'></i> <strong>Show Time:</strong> %s</p>" +
                            "<p><i class='fas fa-money-bill-wave'></i> <strong>Total Paid:</strong> <span style='color: green;'>$%.2f</span></p>" +
                            "</div>" +
                            "<a href='https://your-movie-ticket-website.com/my-bookings' class='cta-button'><i class='fas fa-eye'></i> View My Booking</a>" +
                            "<p class='footer'>Best Regards,<br>🎟️ BookMyShow Team</p>" +
                            "</div>" +
                            "</body>" +
                            "</html>",
                    bookingDTO.getUserId(),
                    "Movie Name Here",  // Replace with actual movie name lookup if possible
                    String.join(", ", bookingDTO.getSeatsSelected()),
                    bookingDTO.getShowDate(),
                    bookingDTO.getShowTime(),
                    bookingDTO.getBookingAmount()
            );

            emailService.sendEmail(recipientEmail, subject, messageBody);
        } catch (Exception e) {
            log.error("Error while sending email for booking details {} ", bookingsJson, e);
        }
    }
}
