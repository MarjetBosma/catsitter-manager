package nl.novi.catsittermanager.config;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.filters.JwtAuthorizationFilter;
import nl.novi.catsittermanager.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                                .requestMatchers("/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/cats").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/cat/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/api/cat").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/cat/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/cat/{id}").hasAnyRole("ADMIN", "CUSTOMER")

                                .requestMatchers(HttpMethod.GET, "/api/customers").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/customer/{username}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/api/customer").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/customer/{username}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/customer/{username}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/customer/{username}/cats").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")
                                .requestMatchers(HttpMethod.GET, "/api/customer/{username}/orders").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")

                                .requestMatchers(HttpMethod.GET, "/api/catsitters").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/catsitter/{username}").hasAnyRole("ADMIN", "CATSITTER")
                                .requestMatchers(HttpMethod.POST, "/api/catsitter").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/catsitter/{username}").hasAnyRole("ADMIN", "CATSITTER")
                                .requestMatchers(HttpMethod.DELETE, "/api/catsitter/{username}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/catsitter/{username}/orders").hasAnyRole("ADMIN", "CATSITTER")

                                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/user/{id}").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")
                                .requestMatchers(HttpMethod.POST, "/api/user").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/user/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/user/{id}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/order/{id}").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")
                                .requestMatchers(HttpMethod.POST, "/api/order").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/order/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/order/{id}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/order/{id}/tasks").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")
                                .requestMatchers(HttpMethod.GET, "/api/order/{id}/invoice").hasAnyRole("ADMIN", "CUSTOMER")

                                .requestMatchers(HttpMethod.GET, "/api/tasks").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/task/{id}").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")
                                .requestMatchers(HttpMethod.POST, "/api/task").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/task/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/task/{id}").hasAnyRole("ADMIN", "CUSTOMER")

                                .requestMatchers(HttpMethod.GET, "/api/invoices").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/invoice/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/api/invoice").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/invoice/{id}").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/invoice/{id}").hasAnyRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/cat/{id}/images").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/api/catsitter/{id}/images").hasAnyRole("ADMIN", "CATSITTER")
                                .requestMatchers(HttpMethod.GET, "/api/{type}/{id}/images/{filename}").hasAnyRole("ADMIN", "CUSTOMER", "CATSITTER")

                                .anyRequest().denyAll()

                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
