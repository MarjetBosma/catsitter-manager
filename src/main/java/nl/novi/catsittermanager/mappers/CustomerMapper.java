package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;

public class CustomerMapper {

    public static CustomerDto transferToDto(Customer customer) {
        return new CustomerDto(
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getCats().stream().map(CatMapper::transferToDto).toList()
        );
    }

    public static Customer transferFromDto(CustomerInputDto customerInputDto) {
        return Customer.builder()
                .username(customerInputDto.username())
                .password(customerInputDto.password())
                .name(customerInputDto.name())
                .address(customerInputDto.address())
                .email(customerInputDto.email())
                .build();
    }
}
