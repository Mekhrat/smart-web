package kz.kaznu.smart.config;

import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().antMatchers( "/css/**", "/js/**", "/images/**", "/image/**").permitAll()
                .antMatchers("/favorites", "/orders").authenticated()
                .anyRequest().permitAll();

       http.formLogin()
               .loginPage("/login").permitAll()
               .usernameParameter("email")
               .passwordParameter("password")
               .loginProcessingUrl("/auth").permitAll()
               .failureUrl("/login?error")
               .defaultSuccessUrl("/home");

       http.logout()
               .logoutUrl("/logout").permitAll()
               .logoutSuccessUrl("/login");

       http.csrf().disable();
    }

}
