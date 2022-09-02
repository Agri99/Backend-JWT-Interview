package backend.interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import backend.interview.services.UserServiceImpl;
@Configuration
public class UserSecurity {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final AuthSuccessHandler authSuccessHandler;
    private final UserServiceImpl userServiceImpl;
    private final String secret;

    public UserSecurity(AuthSuccessHandler authSuccessHandler, UserServiceImpl userServiceImpl, 
                        @Value("${jwt.secret}") String secret) {
        this.authSuccessHandler = authSuccessHandler;
        this.userServiceImpl = userServiceImpl;
        this.secret = secret; 
                        }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((auth -> {
                try {
                    auth
                        .antMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .addFilter(authFilter())
                        .addFilter(new JwtAuthFilter(authenticationManager, userServiceImpl, secret))
                        .exceptionHandling()
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }))
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthFilter authFilter() throws Exception {
        AuthFilter filter = new AuthFilter();
        filter.setAuthenticationSuccessHandler(authSuccessHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

}
