package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Ambas anotaciones son importantes para que funcione la configuración
 * 
 * @author Saul Avila
 *
 */
@Configuration
@EnableResourceServer
//aqui se configuran las reglas de seguridad de los endpoint a quien se le va  dar permisos para hacer ciertas cosas
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/**
	 * Aqui ouedes indicar qué rutas están protegidas y cuales son publicas... Como
	 * para el ejemplo se estan usando anotaciones en cada metodo para definir su
	 * nivel de seguridad aqui sólo se definen las rutas que son publicas con
	 * permitAll() y se indica, también que todo lo que no este especificado ahi
	 * dentro debe llevar autenticación.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "/api/uploads/img/**",
						"/images/**")
				.permitAll()
				.antMatchers("/api/clientes/{id}").permitAll()
				.antMatchers("/api/facturas/**").permitAll()
				/*
				 * .antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER",
				 * "ADMIN") .antMatchers(HttpMethod.POST,
				 * "/api/clientes/upload").hasAnyRole("USER", "ADMIN")
				 * .antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
				 * .antMatchers("api/clientes/**").hasRole("ADMIN")
				 */
				.anyRequest().authenticated().and().cors().configurationSource(corsConfigurationSource());
	}

	/**
	 * Con esto se configura el CORS
	 * 
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// También puede poner un "*" para dar permiso a todos
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		// También puedes dar permiso a todos los verbos con "*"
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * Con esto se registra el bean de cors y se le da la prioridad más alta
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
