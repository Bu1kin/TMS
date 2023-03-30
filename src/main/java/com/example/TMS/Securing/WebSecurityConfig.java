package com.example.TMS.Securing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {return new BCryptPasswordEncoder(8);}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "login", "/user/registration").permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll().usernameParameter("username")
                .and().formLogin(formLogin -> formLogin.successHandler(new AuthSuccessHandler()))
                .logout().permitAll()
                .and().csrf().disable().cors().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(getPasswordEncoder()).
                usersByUsernameQuery("SELECT login, password, active FROM user_ WHERE login = ?").
                authoritiesByUsernameQuery("SELECT u.login, r.role FROM user_ u INNER JOIN role r ON u.id = r.id_user WHERE login = ?");
    }
}
