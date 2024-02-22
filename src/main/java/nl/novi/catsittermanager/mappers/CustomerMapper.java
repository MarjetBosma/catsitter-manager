package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.dtos.customer.CustomerInputDto;
import nl.novi.catsittermanager.models.Customer;

public class CustomerMapper {
    public CustomerDto transferToDto(Customer customer) {

        CustomerDto customerDto = new CustomerDto();

        // catSitterDto.id = catSitter.getId(); // deze komt uit superklasse User
        customerDto.numberOfCats = customer.getNumberOfCats();
        customerDto.orderList = customer.getOrderList();
        customerDto.catListByName = customer.getCatListByName();
        customerDto.catsitters = customer.getCatSitters();

        return customerDto;
    }

    public Customer trasnferToCustomer(CustomerInputDto customerDto) {

        Customer customer = new Customer();

        customer.setNumberOfCats(customerDto.numberOfCats);
        customer.setOrderList(customerDto.orderList);
        customer.setCatListByName(customerDto.catListByName);
        customer.setCatSitters(customerDto.catsitters);

        return customer;
    }
}