package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping(value = "/api")
@Slf4j
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	@GetMapping(value = "/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		PageRequest pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}

	/**
	 * Asi es como creo que es el deber ser un response entity que lleve información
	 * de lo que pasó en el backend pero sin limitar a un tipo especifico como
	 * Cliente, se deja abierto para que puedas enviar sólo mensajes o bien al
	 * Cliente en caso de que todo vaya bien.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	@PostMapping(value = "/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Cliente clienteNuevo = null;
		Map<String, Object> response = new HashMap<String, Object>();

		if (result.hasErrors()) {

			/*
			 * List<String> errors = new ArrayList<String>(); for (FieldError err :
			 * result.getFieldErrors()) { errors.add("El campo '" + err.getField() + "' " +
			 * err.getDefaultMessage()); }
			 */
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "el cliente ha sido creado con éxito");
		response.put("cliente", clienteNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdated = null;

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if (clienteActual == null) {
			response.put("mensaje", "Error, no se pudo editar, el cliente ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());
			clienteUpdated = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "el cliente ha sido actualizado con éxito");
		response.put("cliente", clienteUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Cliente cliente = clienteService.findById(id);
			String nombreFotoAnterior = cliente.getFoto();
			if (!StringUtils.isEmpty(nombreFotoAnterior)) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			clienteService.delete(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "el cliente ha sido eliminado con éxito");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<String, Object>();

		Cliente cliente = clienteService.findById(id);
		if (!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			log.info("ruta de archivo: " + rutaArchivo.toString());
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				e.printStackTrace();
				response.put("mensaje", "Error al subir a imagen del cliente: " + nombreArchivo);
				response.put("error", e.getMessage().concat(": ".concat(e.getCause().getMessage())));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String nombreFotoAnterior = cliente.getFoto();
			if (!StringUtils.isEmpty(nombreFotoAnterior)) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		log.info("ruta de archivo: " + rutaArchivo.toString());

		Resource recurso = null;
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no_user.png").toAbsolutePath();
			try {
				recurso = new UrlResource(rutaArchivo.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			log.error("Error no se pudo cargar la imagen: " + nombreFoto);
		}

		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);

	}

}
