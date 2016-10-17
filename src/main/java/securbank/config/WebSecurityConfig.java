package securbank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import securbank.authentication.CustomAuthenticationProvider;
import securbank.controller.CommonController;
import securbank.services.AuthenticationServiceImpl;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Ayush Gupta
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {


//        http
//        .authorizeRequests()
//	        .antMatchers("/", "/home").permitAll()
//	        .anyRequest().authenticated()
//	        .and()
//        .formLogin()
//            .loginPage("/login")
//            .failureForwardUrl("/login?error")
//            .permitAll()
//            .and()
//        .logout()
//            .permitAll();

        http
        .authorizeRequests()
//        	.antMatchers("/admin/**").access("hasRole('ADMIN')")
	        .antMatchers("/", "/home").permitAll()
//	        .anyRequest().authenticated()
	        .and()
        .formLogin()
            .loginPage("/login")
            //.failureForwardUrl("/login?error")
            .permitAll()
            
            .and()
        .logout()
            .permitAll();

    }

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(this.customAuthenticationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
