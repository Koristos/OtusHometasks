package ru.fefelov.eventsorter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.fefelov.eventsorter.dto.ActivityEvent;
import ru.fefelov.eventsorter.dto.BonusEvent;
import ru.fefelov.eventsorter.dto.Event;
import ru.fefelov.eventsorter.dto.NotificationEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "dateToLocal")
    ActivityEvent toActivityEvent(Event event);

    BonusEvent toBonusEvent(Event event);

    @Mapping(target = "notificationType", ignore = true)
    NotificationEvent toNotificationEvent(Event event);

    @Named("dateToLocal")
    default LocalDateTime dateToLocal(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
