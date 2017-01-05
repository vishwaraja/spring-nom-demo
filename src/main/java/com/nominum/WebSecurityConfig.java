package com.nominum;

/**
 * Created by vpathi on 12/28/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/create").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .defaultSuccessUrl("/list")
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/")
                .logoutUrl("/logout")
                .permitAll()
            .and()
                .csrf()
                .disable();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inMemoryUserDetailsManager());
    }


    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        users.put("user", "pass,ADMIN,enabled");
        users.put("jack", "yoyo,USER,enabled");
        users.put("phil", "pass,ADMIN,enabled");
        users.put("eric", "pass,ADMIN,enabled");
        return new InMemoryUserDetailsManager(users);
    }
}