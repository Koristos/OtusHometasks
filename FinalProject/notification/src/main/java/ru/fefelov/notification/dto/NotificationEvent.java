package ru.fefelov.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationEvent {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("userEmail")
    private String userEmail;
    @JsonProperty("notificationType")
    private NotificationType notificationType;
}
