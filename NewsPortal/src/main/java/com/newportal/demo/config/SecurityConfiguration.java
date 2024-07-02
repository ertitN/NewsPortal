package com.newportal.demo.config;


import com.newportal.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf-> csrf.disable());
        http
                .authorizeHttpRequests((authorize) ->
                            authorize.requestMatchers("/api/register",
                                            "/api/login"
                                    ).permitAll()
                                    .requestMatchers(HttpMethod.GET,
                                            "/api/news/**","/api/news",
                                            "/api/user/**")
                                    .permitAll()

                                    .requestMatchers(HttpMethod.POST,"/api/news",
                                                                    "/api/user/**",
                                                                    "/api/user"

                                    ).hasAnyRole("AUTHOR","ADMIN")
                                    .requestMatchers(HttpMethod.PUT,"/api/news/**",
                                            "/api/user/**",
                                            "/api/user"
                                    ).hasAnyRole("AUTHOR","ADMIN")
                                    .requestMatchers("/api","/api/**").hasRole("ADMIN")

                );

       http.authenticationProvider(authProvider())
               .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }


}
