package com.owen.springsecurity.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	// add a reference to our security data source

	@Autowired
	private DataSource securityDataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// use jdbc authentication ... oh yeah!!!

		auth.jdbcAuthentication().dataSource(securityDataSource);

		// add our users for in memory authentication

//		auth.inMemoryAuthentication().withUser("john").password("secret123").roles("EMPLOYEE");
//		auth.inMemoryAuthentication().withUser("mary").password("secret123").roles("EMPLOYEE", "MANAGER");
//		auth.inMemoryAuthentication().withUser("susan").password("secret123").roles("EMPLOYEE", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()

				//.anyRequest().authenticated()
				.antMatchers("/").hasRole("EMPLOYEE")
				.antMatchers("/leaders/**").hasRole("MANAGER")
				.antMatchers("/systems/**").hasRole("ADMIN")

				.and()

				.formLogin()
					.loginPage("/showMyLoginPage")
					.loginProcessingUrl("/authenticateTheUser")
					.permitAll()

				.and()

				.logout().permitAll();
	}
}
