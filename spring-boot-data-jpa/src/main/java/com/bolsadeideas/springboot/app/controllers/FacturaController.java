package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

import jakarta.validation.Valid;


@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/* Metodo para ver el detalle*/
	@GetMapping("/ver/{id}")
	public String ver (@PathVariable(value="id") Long id,
			Model model,
			RedirectAttributes flash) {
		Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);  /*findFacturaById(id);*/
		
		if(factura == null) {
			flash.addAttribute("error","La factura no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: " .concat(factura.getDescripcion()));
		
		return "factura/ver";
	}
	

	/* Metodo crear factura */
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(clienteId);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		/* Creacion de la i */
		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Crear Factura");

		return "factura/form";
	}

	/* Metodo para buscar el producto por su nombre */
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}

	/* Metodo para guardar la factura */
	@PostMapping("/form")
	public String guardar(@Valid Factura factura,  
			BindingResult result,
			Model model,
			@RequestParam(name="item_id[]", required = false)  Long[] itemId,
			@RequestParam(name="cantidad[]", required = false)  Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status) {
		
		//validar si el form no tiene errores:
		if(result.hasErrors()) {
			model.addAttribute("titulo","Crear Factura");
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo","Crear Factura");
			model.addAttribute("error","Error: la factura no estar sin productos");
			return "factura/form";
			
		}
		for(int i=0; i<itemId.length; i++ ) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("Id: " + itemId[i].toString()+", Cantidad: "+ cantidad[i].toString());
		}
		
		clienteService.saveFactura(factura);
		
		status.setComplete();
		flash.addAttribute("success", "Factura creada con éxito!");
		return "redirect:/ver/" + factura.getCliente().getId();
		 
	}
	
	/*Metodo para eliminar la factura*/
	@GetMapping("/eliminar/{id}")
	public String eliminar (@PathVariable(value="id") Long id,
			RedirectAttributes flash) {
		
		Factura factura = clienteService.findFacturaById(id);
		if(factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con éxito!");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error","La factura no existe en la base de datos, no se puedo elimnar!");
		return "redirect:/listar";
	}
	

}
