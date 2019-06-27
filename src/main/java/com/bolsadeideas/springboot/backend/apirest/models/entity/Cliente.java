package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "No puede estar vacío")
	@Size(min = 4, max = 12, message = "El tamaño tiene que estar entre 4 y 12 caracteres")
	@Column(nullable = false)
	private String nombre;

	@NotEmpty(message = "No puede estar vacío")
	private String apellido;

	@NotEmpty(message = "No puede estar vacío")
	@Email(message = "No es una direccion de correo bien formada")
//	@Column(nullable = false, unique = true)
	@Column(nullable = false, unique = false)
	private String email;

	@Temporal(TemporalType.DATE)
	@NotNull(message = "No puede estar vacío")
	private Date createAt;
	
	private String foto;

//	@PrePersist
//	public void prePersist() {
//		createAt = new Date();
//	}

}
