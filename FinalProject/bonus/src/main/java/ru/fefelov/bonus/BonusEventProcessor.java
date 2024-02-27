package ru.fefelov.bonus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BonusEventProcessor {

    public void process(BonusEvent event) {
        log.info(event.toString());
    }
}
