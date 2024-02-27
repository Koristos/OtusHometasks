package ru.fefelov.summer.market.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fefelov.summer.market.dto.OrderItemDto;
import ru.fefelov.summer.market.kafka.EventKafkaProducer;
import ru.fefelov.summer.market.model.Product;
import ru.fefelov.summer.market.model.User;
import ru.fefelov.summer.market.services.ProductService;
import ru.fefelov.summer.market.services.UserService;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventProducer {

    private final EventKafkaProducer producer;
    private final UserService userService;
    private final ProductService productService;

    public void sendAuthEvent(String userName) {
        User user = userService.findByUsername(userName).orElse(null);
        if (user != null) {
            Event event = new Event();
            event.setType(EventType.LOGIN);
            event.setUserId(user.getId().toString());
            event.setUserEmail(user.getEmail());
            producer.send(event);
        }
    }

    public void sendDealEvent(String userName, List<OrderItemDto> items, Double summ) {
        User user = userService.findByUsername(userName).orElse(null);
        if (user != null) {
            Event event = new Event();
            event.setType(EventType.DEAL);
            event.setUserId(user.getId().toString());
            event.setUserEmail(user.getEmail());
            event.setStatistic(new HashMap<>());
            items.forEach(item -> event.getStatistic().put(item.getProductTitle(), (double) item.getQuantity()));
            producer.send(event);
        }

    }

    public void sendCartEvent(String userName, Long productId) {
        User user = userService.findByUsername(userName).orElse(null);
        Product product = productService.findById(productId).orElse(null);
        if (user != null && product != null) {
            Event event = new Event();
            event.setType(EventType.CART);
            event.setUserId(user.getId().toString());
            event.setUserEmail(user.getEmail());
            event.setStatistic(new HashMap<>());
            event.getStatistic().put(product.getCategory().getTitle(), product.getPrice().doubleValue());
            producer.send(event);
        }
    }


}
