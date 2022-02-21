package com.qpf.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.qpf.service.UsuariService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuariService usuariService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                	// rutes del backoffice, és a dir, has d'estar loguejat per a accedir-hi i l'accés és segons el rol de l'usuari
                	.antMatchers("/back/index").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROMOTOR")
                	.antMatchers("/back/admin/**").hasAnyAuthority("ROLE_ADMIN")
                	.antMatchers("/back/promotor/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PROMOTOR")                
                	// permet totes aquestes rutes, és a dir, són públiques
                    .antMatchers(
                    		"/","/home","/index","/welcome",
                    		"/registration",
                    		"/contact",
                    		"/front/**",   
                    		"/activitats","/activitats-dema","/activitats-setmana",
                    		"/quefem","/anunciat","/politica","/avis",
                    		"/error",
                            "/js/**","/css/**","/assets/img/**").permitAll()	
                    .anyRequest().authenticated()
                .and()
                	// on és el formulari de login
                    .formLogin()
                        .loginPage("/login")
                            .permitAll()
                .and()
                    .logout()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                	// pàgina d'error per quan l'usuari no té permisos
                	.exceptionHandling().accessDeniedPage("/access-denied")              	
                .and()
                	// perquè funcionin els iframes al mateix domini
                	// https://stackoverflow.com/questions/28647136/how-to-disable-x-frame-options-response-header-in-spring-security                
                	.headers().frameOptions().sameOrigin()              
            	.and()
            		// csrf disable code.
            		// https://www.yawintutor.com/how-to-enable-and-disable-csrf/            	
            		.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        // match autenticació Spring <> model d'usuari definit a l'aplicació
        auth.setUserDetailsService(usuariService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

	// rol de l'usuari loguejat
	// https://stackoverflow.com/questions/10092882/how-can-i-get-the-current-user-roles-from-spring-security-3-1
    public String getLoggedUserRole() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = authentication.getAuthorities().stream()
			     .map(r -> r.getAuthority()).collect(Collectors.toSet());
		String role = roles.iterator().next();
		return role;
    }
}
