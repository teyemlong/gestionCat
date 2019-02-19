/**
 * 
 */
package org.sid.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Robert
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder bCryptPasswordEncoder=getBCPE();
		auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("1234")).roles("ADMIN","USER");
		auth.inMemoryAuthentication().withUser("user1").password(bCryptPasswordEncoder.encode("1234")).roles("USER");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		// http.formLogin();
		//On utilise une authentification de type Stateless en utilisant un Jeton a savoir JWT
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// on dit a Spring Security quil n'est plus necessaire d'utiliser les sessions
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/categories/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/products/**").permitAll();
		http.authorizeRequests().antMatchers("/categories/**").hasAuthority("ADMIN");
		http.authorizeRequests().antMatchers("/products/**").hasAuthority("USER");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	@Bean
	public BCryptPasswordEncoder getBCPE(){
		return new BCryptPasswordEncoder();
	}

}
