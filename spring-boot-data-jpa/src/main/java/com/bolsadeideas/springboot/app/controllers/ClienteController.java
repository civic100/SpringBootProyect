package com.bolsadeideas.springboot.app.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IClienteService clienteService; // Para realizar la consulta

	@Autowired
	private IUploadFileService uploadFileService;

	/* Cargar imagenes en uploads */
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping(value = "/uploads/{fielname:.+}")
	public ResponseEntity<Resource> verfoto(@PathVariable String fielname) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(fielname);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.fetchByIdWithFacturas(id);  /*findOne(id);*/
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}

	// Método para lsitar los clientes
	@RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {
		
	
		if(authentication != null) {
			logger.info("Hola usuario autenticado, name: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			logger.info("Hola usuario autenticado por (	Authentication auth = SecurityContextHolder.getContext().getAuthentication()), name: ".concat(authentication.getName()));
		}
		
		/*
		 * Validacion de Roles 
		 */
		/* Froma manula mediante el metodo hasRole*/
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola usuario: ".concat(auth.getName()).concat("Tienes acceso!") );
		}
		else {
			logger.info("Hola usuario: ".concat(auth.getName()).concat("No tienes acceso!"));
		}
		
		/*Mediante el objeto "SecurityContextHolderAwareRequestWrapper" */
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("(usando:SecurityContextHolderAwareRequestWrapper ) Hola: ".concat(auth.getName()).concat("Tienes acceso!") );
		}else {
			logger.info("(usando:SecurityContextHolderAwareRequestWrapper ) Hola: ".concat(auth.getName()).concat("No tienes acceso!"));
		}
		
		/*De forma nativa mediante HttpServletRequest  */
		if(request.isUserInRole("ADMIN")) {
			logger.info("(usando:HttpServletRequest ) Hola: ".concat(auth.getName()).concat("Tienes acceso!") );
		}else {
			logger.info("(usando:HttpServletRequest ) Hola: ".concat(auth.getName()).concat("No tienes acceso!"));
		}

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	/*
	 * Método que muestra el formulario al usuario, pasando el cliente y un título
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Fromulario de cleinte");
		return "form";
	}

	/*
	 * Metodo para Editar un cliente
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El Id del cliente no existe en la bbdd");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id del cliente no puede ser 0");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}

	/*
	 * Método que recibe los datos del form, los guarda y recarga la apgina de
	 * listar clientes.
	 *
	 * Con @Valid se validan los argumentos recividos por el objeto del formulario
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Fromulario de cleinte");
			return "form";
		}

		/*
		 * Comprobar el requestparam de file y guardar :
		 */
		if (!foto.isEmpty()) {

			if (cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());
			}

			String uniqueFilname = null;
			try {
				uniqueFilname = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addAttribute("info", "Has subido correctamente: '" + uniqueFilname + "'");
			cliente.setFoto(uniqueFilname);

		}
		String mensajeFlash = (cliente.getId() != null) ? "Cliente Editado con existo" : "Cliente Creado con existo";

		clienteService.save(cliente);
		/* Eliminar el objeto cliente de la sesion */
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	/* Metodo Eliminar */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente Eliminado con existo");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto: " + cliente.getFoto() + " elimnada");
			}

		}
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		/*El método contains(GrantedAuthority) retorna un booleano, true o false, si contiene o no el elemento en la colección*/
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*for(GrantedAuthority authority:authorities ) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola usuario: ".concat(auth.getName()).concat("tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
		return false;*/
	}

}
