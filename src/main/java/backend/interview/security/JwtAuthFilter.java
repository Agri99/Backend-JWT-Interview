package backend.interview.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import backend.interview.services.UserServiceImpl;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final String TOKEN_PREFIX = "Bearer ";
    private final UserServiceImpl userServiceImpl;
    private final String secret;

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl, String secret) {
        super(authenticationManager);
        this.userServiceImpl = userServiceImpl;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = getAuthentication(request);
        if(auth == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        String username = JWT.require(Algorithm.HMAC256(secret))
                            .build()
                            .verify(token.replace(TOKEN_PREFIX, ""))
                            .getSubject();
        if(username == null) return null;
        UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    
    
}
