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
                                .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                                .requestMatchers("/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/**").permitAll()
//                                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//                                        .requestMatchers("/authenticated").authenticated()
//                                        .requestMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
//                                        .requestMatchers("/authenticated").authenticated()
//                                        .requestMatchers("/authenticate").permitAll()
                                .anyRequest().denyAll() /*Deze voeg je altijd als laatste toe, om een default beveiliging te hebben voor eventuele vergeten endpoints of endpoints die je later toevoegT. */

//Here we have added our jwt filter before the UsernamePasswordAuthenticationFilter.
//Because we want every request to be authenticated before going through spring security filter.
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
//        //JWT token authentication
//        http
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(basic -> basic.disable())
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth ->
//                                auth
//                                        // Wanneer je deze uncomment, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                                        .requestMatchers("/**").permitAll()
////                                        .requestMatchers(HttpMethod.POST, "/**").permitAll()
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
////        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf().disable()
//                .authorizeRequests()
//                .requestMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//            return http.build();
//        }

    // Deze moet later weer weg, want uiteindelijk wil ik wel een password encoder gebruiken.
//        @Bean
//        public NoOpPasswordEncoder passwordEncoder() {
//            return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//        }


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
