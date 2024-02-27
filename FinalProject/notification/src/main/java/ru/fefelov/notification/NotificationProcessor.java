package ru.fefelov.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fefelov.notification.dto.NotificationEvent;
import ru.fefelov.notification.dto.NotificationType;
import ru.fefelov.notification.mail.NotificationEmailService;

@Service
@Slf4j
public class NotificationProcessor {

    @Autowired
    private NotificationEmailService emailService;

    private static final String DEAL_MESSAGE = "Поздравляем! Ваш заказ оформлен.";
    private static final String DEAL_THEME = "Успешная покупка";
    private static final String REMINDER_MESSAGE = "Напоминание";
    private static final String REMINDER_THEME = "Не забудте завершить заказ";

    public void process(NotificationEvent event) {
        log.info(event.toString());
        if (NotificationType.DEAL.equals(event.getNotificationType())){
            emailService.sendSimpleEmail(event.getUserEmail(), DEAL_THEME, DEAL_MESSAGE);
        } else if (NotificationType.INTEREST.equals(event.getNotificationType())) {
            emailService.sendSimpleEmail(event.getUserEmail(), REMINDER_THEME, REMINDER_MESSAGE);
        }
    }
}
