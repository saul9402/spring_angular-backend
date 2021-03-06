package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Ambas anotaciones son importantes para que funcione la configuración
 * 
 * @author Saul Avila
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private InfoAdicionalToken infoAdicionalToken;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// con esta parte dices qué usuarios tienen permiso a qué cosas de tu aplicacion
		security
				// este es cuando generas el token (todos pueden generar uno)
				.tokenKeyAccess("permitAll()")
				// este es para verificar el token (solo los que este autenticados pueden)
				// /oauth/check_token
				.checkTokenAccess("isAuthenticated()");

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// con esto eplicas la seguridad en la app, pides las credenciales de la
		// aplicacion
		// que se va a logear y también las credenciales del usuario que va a entrar
		clients.inMemory()
				// usuario y contraseña de la aplicación
				.withClient("angularapp").secret(passwordEncoder.encode("12345"))
				// das los permisos que tendra la app, en este caso ambos
				.scopes("read", "write")
				/*
				 * el tipo de autorizacion; como se va a intercambiar el token, básicamente hay
				 * tres tipos: password, usuario y contraseña del cliente que se va a conectar y
				 * de la aplicacion misma; codigo de autorizacion con este se entrega un codigo
				 * de autorizacion por el backend y ese es el que recibe la aplicación para
				 * logear; implicit, usuario y contraseña es como el primero pero en este no
				 * pides el usuario y contraseña de la aplicacion sino solo del usuario que se
				 * va a logear.
				 * 
				 * El refresh_token permite obtener un token de acceso renovado para poder
				 * seguir en la aplicacion sin tener que iniciar sesion de nuevo.
				 */
				.authorizedGrantTypes("password", "refresh_token")
				// el tiempo en el que caduca el token, está en segundos (3600s = 1h)
				.accessTokenValiditySeconds(3600)
				// la duracion del token renovado (3600s = 1h)
				.refreshTokenValiditySeconds(3600);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		// con esto enlazas la información que trae el token por defecto y la
		// información adicional que quieres agregar
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));

		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
				// se encarga de traducir los datos del token y tambien de saber si es válido o
				// no
				.accessTokenConverter(accessTokenConverter()).tokenEnhancer(tokenEnhancerChain);
	}

	// esta parte es opcional, puedes o no ponerla
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		/*
		 * Se agrega una llave propia, por defecto si no se asigna el api asigna una
		 * llave random
		 */
		/* jwtAccessTokenConverter.setSigningKey(JwtConfig.LLAVE_SECRETA); */

		/**
		 * Comando para generar llave privada y llave publica con Openssl 1.- openssl
		 * genrsa -out jwt.pem Comando para ver la llave privada 1.- openssl rsa -in
		 * jwt.pem Comando para ver la llave publica 1.- openssl rsa -in jwt.pem -pubout
		 */

		/* El que firma es con el metodo signin key */
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVADA);

		/* El que verifica es con el metodo verfier */
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}

}
