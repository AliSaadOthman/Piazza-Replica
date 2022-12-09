package org.piazza.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.piazza.repo.CacheServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
//@NoArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    public CustomAuthorizationFilter(CacheServer cacheServer){
        this.cacheServer = cacheServer;
    }

    @Autowired
    CacheServer cacheServer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")||request.getServletPath().equals("/api/register")||request.getServletPath().equals("/api/refresh")||request.getServletPath().equals("/api/login")){
            request.setAttribute("url", request.getRequestURL().toString());
            filterChain.doFilter(request, response);
        }
        else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    request.setAttribute("access_token", token);
                    Algorithm algorithm = Algorithm.HMAC256("secretpiazza".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String roles = decodedJWT.getClaim("role").toString().replaceAll("\"", "");

                    request.setAttribute("userEmail", username);
                    request.setAttribute("userRole", roles);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(roles));
                    if (tokenCached(token)) {
                        filterChain.doFilter(request, response);
                    } else {
                        log.error("Error logging in: {}", "Incorrect Token Not in cache");
                        response.setHeader("error", "Incorrect Token not in cache");
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", "Incorrect Token");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
                }
                catch(Exception e){
//                    System.out.println("in Catch");
//                    System.out.println("tok: "+authorizationHeader);
                    log.error("Error logging in: {}",e.getMessage());
                    response.setHeader("error",e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }
            else{
                log.error("Error logging in: {}","No token sent");
                response.setHeader("error","No token sent");
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", "No token sent");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
    public boolean tokenCached(String token){
        boolean t = cacheServer.existsInCache("tokens", token);
        return t;
    }
}
