package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;



@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DataSource dataSource;
	
	 @Autowired
	    private JpaUserDetailsService userDetailService;
		
	 
     @Autowired
     public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService)
        .passwordEncoder(passwordEncoder);
     }
   
     
	@Bean
    public UserDetailsService userDetailsService()throws Exception{
        
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
               .withUsername("user")
               .password(passwordEncoder.encode("12345"))
               .roles("USER")
               .build());
 
        manager.createUser(User
               .withUsername("admin")
               .password(passwordEncoder.encode("admin"))
               .roles("ADMIN","USER")
               .build());
            
        return manager;
    }
	
	@Autowired
	private LoginSuccesHandler successHandler;
	
	@Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> {
                try {
                    authz.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                        /*.requestMatchers("/uploads/**").hasAnyRole("USER")
                        .requestMatchers("/ver/**").hasRole("USER")
                        .requestMatchers("/factura/**").hasRole("ADMIN")
                        .requestMatchers("/form/**").hasRole("ADMIN")
                        .requestMatchers("/eliminar/**").hasRole("ADMIN")*/
                        .anyRequest().authenticated()
                        .and()
	                        .formLogin()
	                        	.successHandler(successHandler)
	                        	.loginPage("/login")
	                        .permitAll()
                        .and()
                        .logout().permitAll()
                        .and()
                        .exceptionHandling().accessDeniedPage("/error_403");
             
 
                } catch (Exception e) {
                        e.printStackTrace();
                }
            });
 
        return http.build();
            
    }
	
    /*@Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                Consulta de login por username
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                Consulta para obtener roles medainte el username
                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?")
                .and().build();
    }*/
	
}