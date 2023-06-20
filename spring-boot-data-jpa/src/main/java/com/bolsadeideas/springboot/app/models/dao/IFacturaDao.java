package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IFacturaDao extends JpaRepository<Factura, Long>{
	
	@Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id = ?1")
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);

}
