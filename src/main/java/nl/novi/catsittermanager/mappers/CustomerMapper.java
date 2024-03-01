package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;

public class CustomerMapper {

    public static CustomerDto transferToDto(Customer customer) {
        return new CustomerDto(customer.getId(),
                               customer.getNumberOfCats(),
                               customer.getCatListByName(),
                               customer.getOrderList(),
                               customer.getCatsitterList()
        );
    }

    public static Customer transferFromDto(CustomerInputDto customerInputDto) {
        return new Customer(customerInputDto.numberOfCats(),
                            customerInputDto.catListByName(),
                            customerInputDto.orderList(),
                            customerInputDto.catsitterList()
        );
    }
}
