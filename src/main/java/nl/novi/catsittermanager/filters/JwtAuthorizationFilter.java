package nl.novi.catsittermanager.filters;

//We need to add a jwt authorization filter for each request.
// This filter will block all requests that don’t have jwt token in the request header.

//We override the doFilterInternal() method. This method will be called for every request to out application.
// This method reads Bearer token from request headers and resolves claims. First, it checks if any access token is present in the request header.
// If the accessToken is null. It will pass the request to next filter chain.
// Any login request will not have jwt token in their header, therefore they will be passed to next filter chain.
// If any acessToken is present, then it will validate the token and then authenticate the request in SecurityContext.

import nl.novi.catsittermanager.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : "+accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims != null && jwtUtil.validateClaims(claims)){
                String username = claims.getSubject();
                System.out.println("username : "+ username);
                //This line will authenticate the request to the SecurityContext.
                // So, any request having a jwt token in their header will be authenticated & permited by spring security.
                //todo onderstaande nog aanpassen.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username,"",new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e){
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

        }
        filterChain.doFilter(request, response);
    }
}
