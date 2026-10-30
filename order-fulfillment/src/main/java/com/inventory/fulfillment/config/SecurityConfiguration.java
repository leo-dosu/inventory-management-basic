package com.inventory.fulfillment.config;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfiguration {
@Bean SecurityFilterChain security(HttpSecurity http) throws Exception {return http.authorizeHttpRequests(a->a.anyRequest().permitAll()).csrf(c->c.disable()).build();}
}
