package nl.novi.catsittermanager.config;

import nl.novi.catsittermanager.repositories.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
public class TestConfig {

    @MockBean
    private CatRepository catRepository;

    @MockBean
    private CatsitterRepository catsitterRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private FileUploadRepository fileUploadRepository;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
