package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fefelov.summer.market.events.EventProducer;
import ru.fefelov.summer.market.exceptions.ResourceNotFoundException;
import ru.fefelov.summer.market.services.ProductService;
import ru.fefelov.summer.market.utils.Cart;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final ProductService productService;
    private final EventProducer eventProducer;

    @GetMapping
    public Cart getCart() {
        return cart;
    }

    @GetMapping("/add/{productId}")
    public void add(Principal principal, @PathVariable Long productId) {
        if (!cart.add(productId)) {
            cart.add(productService.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Unable add product to cart. Product not found id: " + productId)));
            new Thread(() -> eventProducer.sendCartEvent(principal.getName(), productId)).start();
        }
    }

    @GetMapping("/decrement/{productId}")
    public void decrement(@PathVariable Long productId) {
        cart.changeQuantity(productId, -1);
    }

    @GetMapping("/remove/{productId}")
    public void remove(@PathVariable Long productId) {
        cart.remove(productId);
    }

    @GetMapping("/clear")
    public void clear() {
        cart.clear();
    }
}
