package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUsuarioService;

/**
 * Aqui puede definir información adicional para el token, prácticamente lo que
 * quieras, nombre de usuario, email, numero de contacto, apellido, nombre, lo
 * que se te ocurra. Del objeto OAuth2Authentication puedes obtener datos del
 * usuario para realizar consultas a BD como su username o otros detalles.
 * 
 * @author Saul Avila
 *
 */
@Component
public class InfoAdicionalToken implements TokenEnhancer {

	@Autowired
	IUsuarioService usuarioService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		Usuario usuario = usuarioService.findByUsername(authentication.getName());

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("info_adicional", "Hola que tal " + authentication.getName());
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		info.put("email", usuario.getEmail());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		info.put("nombre_usuario", usuario.getUsername() + ": " + usuario.getId());

		return accessToken;
	}

}
