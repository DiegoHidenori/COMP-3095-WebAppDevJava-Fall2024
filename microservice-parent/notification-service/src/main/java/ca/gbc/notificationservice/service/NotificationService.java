package ca.gbc.notificationservice.service;


import ca.gbc.orderservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 3000))
    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent) {

        log.info("Received message from booking-placed topic {}", orderPlacedEvent);

        if (orderPlacedEvent.getEmail() == null || orderPlacedEvent.getOrderNumber() == null) {
            log.error("Invalid OrderPlacedEvent received: {}", orderPlacedEvent);
            return;
        }

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("comp3095@georgebrown.ca");
            messageHelper.setTo(orderPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Booking (%s) was placed successfully",
                    orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    
                    Good Day %s %s,
                    
                    Your booking with booking number %s was successfully placed
                    
                    Thank you,
                    COMP3095
                    
                    """,
                    orderPlacedEvent.getFirstName(),
                    orderPlacedEvent.getLastName(),
                    orderPlacedEvent.getOrderNumber()

            ));
            log.info(messageHelper.toString());
        };

        try {

            log.info("Sending email...");
            javaMailSender.send(messagePreparator);
            log.info("Booking notification successfully sent");

        } catch (MailException e) {
            log.error("Error occurred when sending email", e);
            throw new RuntimeException("Exception occurred when trying to send email", e);
        }

    }
}
