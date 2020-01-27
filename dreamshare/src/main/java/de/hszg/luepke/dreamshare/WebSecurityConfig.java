package de.hszg.luepke.dreamshare;

import de.hszg.luepke.dreamshare.user.DreamUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.audience}")
    private String jwtAudience;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtAudience, jwtIssuer, jwtSecret))
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtAudience, jwtIssuer, jwtSecret))
                .authorizeRequests().antMatchers("/user*", "/login").permitAll().anyRequest().authenticated().and()
                .csrf().disable().sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        DreamUserDetailsService service = new DreamUserDetailsService();
        return service;

    }

}