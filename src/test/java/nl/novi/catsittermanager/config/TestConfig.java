package nl.novi.catsittermanager.config;

import nl.novi.catsittermanager.repositories.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public CatRepository catRepository() {
        return Mockito.mock(CatRepository.class);
    }

    @Bean
    @Primary
    public CatsitterRepository catsitterRepository() {
        return Mockito.mock(CatsitterRepository.class);
    }

    @Bean
    @Primary
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    @Primary
    public FileUploadRepository fileUploadRepository() {
        return Mockito.mock(FileUploadRepository.class);
    }

    @Bean
    @Primary
    public InvoiceRepository invoiceRepository() {
        return Mockito.mock(InvoiceRepository.class);
    }

    @Bean
    @Primary
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    @Primary
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
