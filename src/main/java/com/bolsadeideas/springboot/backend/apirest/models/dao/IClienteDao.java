package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

@Repository
public interface IClienteDao extends CrudRepository<Cliente, Long>{

}
