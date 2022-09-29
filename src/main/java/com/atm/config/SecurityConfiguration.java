package com.atm.config;

import com.atm.exception.NotValidUserException;
import com.atm.service.security.JpaAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public final static String ACCESS_TOKEN = "accessToken";

    @Autowired
    private JpaAuthenticationService jpaAuthenticationService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaAuthenticationService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers(
            "/h2-console/**",
                "/check-balance",
                "/withdrawal/*").permitAll().and()
            .formLogin().defaultSuccessUrl("/get-access-token").and().logout().logoutUrl("/logout");
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public static UserDetails getLoggedUser() throws NotValidUserException {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(o instanceof UserDetails userDetails) {
            return userDetails;
        } else {
            throw new NotValidUserException("User not valid: " + o);
        }
    }
}