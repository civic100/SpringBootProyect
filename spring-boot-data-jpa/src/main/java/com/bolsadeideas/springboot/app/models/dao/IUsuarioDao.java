package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);

}
