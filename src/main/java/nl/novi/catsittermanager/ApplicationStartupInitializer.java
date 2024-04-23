package nl.novi.catsittermanager;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.enumerations.Role;
import nl.novi.catsittermanager.models.User;
import nl.novi.catsittermanager.repositories.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartupInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createAdminUserIfNotExists();
    }

    private void createAdminUserIfNotExists() {
        if (!userRepository.existsById("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
            adminUser.setEnabled(true);
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);
        }
    }
}
