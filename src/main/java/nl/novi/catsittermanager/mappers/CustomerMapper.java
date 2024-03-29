package nl.novi.catsittermanager.mappers;

import lombok.AllArgsConstructor;
import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@AllArgsConstructor
@Component
public class CustomerMapper {

    private final CatMapper catMapper;

    public CustomerDto transferToDto(Customer customer) {
        return new CustomerDto(
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getCats().stream().map(CatMapper::CatToCatResponse).toList(),
                customer.getOrders().stream().map(OrderMapper::transferToDto).toList()
        );
    }

    public static Customer transferFromInputDto(CustomerInputDto customerInputDto) {
        return Customer.CustomerBuilder()
                .username(customerInputDto.username())
                .password(customerInputDto.password())
                .name(customerInputDto.name())
                .address(customerInputDto.address())
                .email(customerInputDto.email())
                .orders(new ArrayList<>())
                .cats(new ArrayList<>())
                .build();
    }
}
