package backend.interview.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final int expTime;
    private final String secret;

    public AuthSuccessHandler(@Value("${jwt.expiration}") int expTime,
                                @Value("${jwt.secret}") String secret) {
        this.expTime = expTime;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = JWT.create()
                                    .withSubject(principal.getUsername())
                                    .withExpiresAt(new Date(System.currentTimeMillis() + expTime))
                                    .sign(Algorithm.HMAC256(secret));
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    
    }
}
