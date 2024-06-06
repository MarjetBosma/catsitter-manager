package nl.novi.catsittermanager.dtos.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.order.OrderResponse;
import java.util.List;

public record CustomerResponse(
        String username,
        String name,
        String address,
        String email,
        List<CatResponse> cats,
        @JsonIgnore
        List<OrderResponse> orders
) {
}