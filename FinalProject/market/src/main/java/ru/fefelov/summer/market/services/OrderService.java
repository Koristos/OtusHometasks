package ru.fefelov.summer.market.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fefelov.summer.market.events.EventProducer;
import ru.fefelov.summer.market.exceptions.ResourceNotFoundException;
import ru.fefelov.summer.market.repositories.OrderRepository;
import ru.fefelov.summer.market.dto.OrderDto;
import ru.fefelov.summer.market.dto.OrderItemDto;
import ru.fefelov.summer.market.model.Order;
import ru.fefelov.summer.market.model.OrderItem;
import ru.fefelov.summer.market.model.Product;
import ru.fefelov.summer.market.model.User;
import ru.fefelov.summer.market.utils.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final Cart cart;
    private final EventProducer eventProducer;

    @Transactional
    public void createOrder(User user, String address, String phone) {
        Order order = new Order();
        order.setPrice(cart.getPrice());
        order.setItems(new ArrayList<>());
        order.setUser(user);
        order.setPhone(phone);
        order.setAddress(address);
        for (OrderItemDto o : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(o.getQuantity());
            Product product = productService.findById(o.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())));
            orderItem.setPricePerProduct(product.getPrice());
            orderItem.setProduct(product);
            order.getItems().add(orderItem);
        }
        orderRepository.save(order);
        List<OrderItemDto> cartClone = new ArrayList(cart.getItems());
        Double summ = cart.getPrice().doubleValue();
        new Thread(() -> eventProducer.sendDealEvent(user.getUsername(), cartClone, summ)).start();
        cart.clear();
    }

    @Transactional
    public List<OrderDto> findAllDtosByUsername(String username) {
        return orderRepository.findAllByUsername(username).stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
