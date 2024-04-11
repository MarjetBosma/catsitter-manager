package nl.novi.catsittermanager.mappers;

import lombok.AllArgsConstructor;
import nl.novi.catsittermanager.dtos.customer.CustomerRequest;
import nl.novi.catsittermanager.dtos.customer.CustomerResponse;
import nl.novi.catsittermanager.models.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

@AllArgsConstructor
@Component
public class CustomerMapper {

    private final CatMapper catMapper;

    public static CustomerResponse CustomerToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getUsername(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                customer.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList()
        );
    }

    public static Customer CustomerRequestToCustomer(CustomerRequest customerRequest) {
        return Customer.CustomerBuilder()
                .username(customerRequest.username())
                .password(customerRequest.password())
                .name(customerRequest.name())
                .address(customerRequest.address())
                .email(customerRequest.email())
                .authorities(new HashSet<>())
                .enabled(true)
                .orders(new ArrayList<>())
                .cats(new ArrayList<>())
                .build();
    }
}
