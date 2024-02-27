package ru.fefelov.summer.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fefelov.summer.market.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class OrderDto {
    private Long id;
    private String address;
    private String phone;
    private BigDecimal price;
    private List<OrderItemDto> items;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.address = order.getAddress();
        this.phone = order.getPhone();
        this.price = order.getPrice();
        this.items = order.getItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}
