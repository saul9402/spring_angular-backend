package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descripcion;

	private String observacion;

	@Temporal(TemporalType.DATE)
	private Date createAt;

	@ManyToOne(fetch = FetchType.LAZY)
	/*
	 * es posible especificar el nombre de la llave foranea en la tabla factura,
	 * sólo habría que poner en esta clase la anotacion @JoinColumn y especificar el
	 * nombre que llevara dicha columna. Por defecto se toma el nombre de la
	 * propiedad en la entidad Factura y se concatena a "_id" quedando como
	 * "cliente_id". Se recomienda poner sólo cuando el nombre de la llave fóranea
	 * sea diferente de este estándar. Pare el caso no se pone puesto que la
	 * relación es bidireccional y la entidad Factura tiene conocimiento sobre la
	 * relación, así hibernate puede generar automaticamente la columna de la llave
	 * foránea.
	 */
	private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	/*
	 * En este caso si se debe poner el @JoinColumn puesto que es una relacion
	 * unidireccional y la entidad ItemFactura no tiene conocimiento de la
	 * existencia de la entidad Factura, pero la entidad Factura sabe acerca de esa
	 * relación y es el propietario de la misma, por ello se debe indicar aqui la
	 * llave foránea que ira en la otra tabla.
	 */
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;

	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public Double getTotal() {
		Double total = 0.0;
		for (ItemFactura itemFactura : items) {
			total += itemFactura.getImporte();
		}
		return total;
	}

}
