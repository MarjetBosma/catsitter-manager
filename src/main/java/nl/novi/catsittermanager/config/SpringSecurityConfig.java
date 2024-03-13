//package nl.novi.catsittermanager.config;
//
//import nl.novi.catsittermanager.filter.JwtRequestFilter;
//import nl.novi.catsittermanager.services.CustomUserDetailsService;
//import nl.novi.catsittermanager.utils.JwtUtil;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig {
//
////    private final CustomUserDetailsService customUserDetailsService;
//
//    private final JwtRequestFilter jwtRequestFilter;
//
//    public SpringSecurityConfig(
////            CustomUserDetailsService userDetailsService,
//                                JwtRequestFilter jwtRequestFilter) {
////        this.customUserDetailsService = userDetailsService;
//        this.jwtRequestFilter = jwtRequestFilter;
//    }
//
//
//    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
//    // Je kunt dit ook in een aparte configuratie klasse zetten.
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    // Authenticatie met customUserDetailsService en passwordEncoder
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
//        var auth = new DaoAuthenticationProvider();
//        auth.setPasswordEncoder(passwordEncoder);
////        auth.setUserDetailsService(customUserDetailsService);
//        return new ProviderManager(auth);
//    }
//
//    // Authorisatie met jwt
//    @Bean
//    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
//
//        //JWT token authentication
//        http
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(basic -> basic.disable())
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth ->
//                                auth
//                                        // Wanneer je deze uncomment, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
////                .requestMatchers("/**").permitAll()
//                                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//                                        .requestMatchers("/authenticated").authenticated()
//                                        .requestMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
//                                        .requestMatchers("/authenticated").authenticated()
//                                        .requestMatchers("/authenticate").permitAll()
//                                        .anyRequest().denyAll() /*Deze voeg je altijd als laatste toe, om een default beveiliging te hebben voor eventuele vergeten endpoints of endpoints die je later toevoegt. */
//                )
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
