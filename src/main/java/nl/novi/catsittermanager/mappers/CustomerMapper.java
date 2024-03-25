package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;

import java.util.ArrayList;

public class CustomerMapper {

    public static CustomerDto transferToDto(Customer customer) {
        return new CustomerDto(
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getCats().stream().map(CatMapper::transferToDto).toList(),
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
