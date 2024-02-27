package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.fefelov.summer.market.exceptions.InvalidInputDataException;
import ru.fefelov.summer.market.exceptions.ResourceNotFoundException;
import ru.fefelov.summer.market.dto.OrderDto;
import ru.fefelov.summer.market.model.User;
import ru.fefelov.summer.market.services.OrderService;
import ru.fefelov.summer.market.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public void createOrder(Principal principal, @RequestParam String address, @RequestParam String phone) {
        List<String> errors = new ArrayList<>();
        if (address.isBlank()) {
            errors.add("Field 'address' cannot be null");
        }
        if (phone.isBlank()) {
            errors.add("Field 'phone' cannot be null");
        }
        if (!errors.isEmpty()) {
            throw new InvalidInputDataException(errors);
        }
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("Unable to create order. User not found"));
        orderService.createOrder(user, address, phone);
    }

    @GetMapping
    public List<OrderDto> getAllOrders(Principal principal) {
        return orderService.findAllDtosByUsername(principal.getName());
    }
}
