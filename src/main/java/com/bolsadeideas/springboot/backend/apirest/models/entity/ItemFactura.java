package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;

	@ManyToOne(fetch = FetchType.LAZY)
	/*
	 * Por defecto se creara la columna de la llave foránea con el nombre de la
	 * propiedad producto puesto que ItemFactura es el dueño de la relación pero
	 * igua se puede poner explicitamente en caso de que el nombre de la columna no
	 * siga el estándar. Esta relación también es unidireccional ya que Producto no
	 * sabe sobre dicha relación.
	 */
	@JoinColumn(name = "producto_id")
	private Producto producto;

	public Double getImporte() {
		return cantidad.doubleValue() * producto.getPrecio();
	}

}
