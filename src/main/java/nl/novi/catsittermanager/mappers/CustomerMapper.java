package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;

public class CustomerMapper {

    public static CustomerDto transferToDto(Customer customer) {
        return new CustomerDto(customer.getOrder(),
                               customer.getCat(),
                               customer.getCatsitter()
        );
    }

    public static Customer transferFromDto(CustomerInputDto customerInputDto) {
        return Customer.CustomerBuilder()
                .cat(customerInputDto.cat())
                .order(customerInputDto.order())
                .catsitter(customerInputDto.catsitter())
                .build();
    }
}
