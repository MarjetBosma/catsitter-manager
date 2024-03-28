package nl.novi.catsittermanager.config;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings("removal")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, NoOpPasswordEncoder noOpPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(noOpPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/**").permitAll() // controleren of dit klopt, later meer paden invoeren
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            return http.build();
        }

        @SuppressWarnings("deprecation") // Deze moet later weer weg, want uiteindelijk wil ik wel een password encoder gebruiken.
        @Bean
        public NoOpPasswordEncoder passwordEncoder() {
            return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
        }
//
//    @Bean
//    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
//
//        //JWT token authentication
//        http
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(basic -> basic.disable())
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth ->
//                                auth
//                                        // Wanneer je deze uncomment, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                                        .requestMatchers("/**").permitAll()
////                                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
////                                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
////                                        .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
////                                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
////                                        .requestMatchers("/authenticated").authenticated()
////                                        .requestMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
////                                        .requestMatchers("/authenticated").authenticated()
////                                        .requestMatchers("/authenticate").permitAll()
////                                        .anyRequest().denyAll() /*Deze voeg je altijd als laatste toe, om een default beveiliging te hebben voor eventuele vergeten endpoints of endpoints die je later toevoegT. */
//                )
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
