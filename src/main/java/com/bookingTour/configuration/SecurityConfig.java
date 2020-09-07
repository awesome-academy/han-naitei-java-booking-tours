package com.bookingTour.configuration;

import com.bookingTour.model.enu.Role;
import com.bookingTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("accessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	@Qualifier("authenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("userService")
    private UserService userService;

	@Override
	public void setContentNegotationStrategy(ContentNegotiationStrategy contentNegotiationStrategy) {
		// TODO Auto-generated method stub
		super.setContentNegotationStrategy(contentNegotiationStrategy);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
	        .antMatchers("/resources/**").permitAll()
	        .antMatchers("/img/**").permitAll()
			.antMatchers("/assets/**").permitAll()
			.antMatchers("/error").permitAll()
			.antMatchers("/access_denied").permitAll()
			.antMatchers("/home").permitAll()
	        .antMatchers("/login").permitAll()
	        .antMatchers("/users/signup").permitAll()
			.antMatchers(HttpMethod.GET, "/users").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
			.antMatchers("/tours**/**").permitAll()
			.antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
	        .anyRequest().hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
	        .and().formLogin().loginPage("/login").failureUrl("/login?error=true")
			    .usernameParameter("userName").passwordParameter("password")
			    .successHandler(authenticationSuccessHandler).loginProcessingUrl("/login").permitAll()
	        .and().logout().invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID")
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll()
	        .and().rememberMe().rememberMeParameter("remember-me").tokenValiditySeconds(60*60)
	            .tokenRepository(userService)
				.userDetailsService(userService)
	        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
	        .and().csrf();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/css/**", "/js/**", "/img/**", "/webjars/**", "/images/**");
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}


}