package com.intranet.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Usuario;
import com.intranet.service.CarreraService;
import com.intranet.service.ProfesorService;
import com.intranet.service.UsuarioService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profesores")
public class ProfesorController {

	@Autowired
	private ProfesorService profesorService;
	@Autowired
	private CarreraService carreraService;
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/filtrado")
	public String filtrado(Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null)
			return "redirect:/login";

		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));

		m.addAttribute("lstProfesores", profesorService.getAll());
		return "Admin/profesores/filtrado";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model m, HttpSession session) {
		m.addAttribute("profesor", new Usuario());
		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));
		m.addAttribute("carreras", carreraService.getAll());
		return "Admin/profesores/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(@Validated @ModelAttribute("profesor") Usuario profesor, BindingResult br, Model m,
			RedirectAttributes flash, HttpSession session) {
		if (br.hasErrors()) {
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			m.addAttribute("cuenta", session.getAttribute("cuenta"));
			m.addAttribute("usuario", session.getAttribute("usuario"));
			m.addAttribute("tipo", session.getAttribute("tipo"));
			return "Admin/profesores/nuevo";
		}

		ResultadoResponse res = profesorService.create(profesor);
		if (!res.success) {
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			m.addAttribute("cuenta", session.getAttribute("cuenta"));
			m.addAttribute("usuario", session.getAttribute("usuario"));
			m.addAttribute("tipo", session.getAttribute("tipo"));
			return "Admin/profesores/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/profesores/filtrado";
	}

	@GetMapping("/edicion/{id}")
	public String edicion(@PathVariable Integer id, Model m, HttpSession session) {
		Usuario profesor = profesorService.getOne(id);
		m.addAttribute("profesor", profesor);
		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));
		m.addAttribute("carreras",carreraService.getAll());
		return "Admin/profesores/edicion";
	}

	@PostMapping("/guardar")
	public String guardar(@Validated @ModelAttribute("profesor") Usuario profesor, BindingResult br, Model m,
			RedirectAttributes flash, HttpSession session) {
		if (br.hasErrors()) {
			m.addAttribute("cuenta", session.getAttribute("cuenta"));
			m.addAttribute("usuario", session.getAttribute("usuario"));
			m.addAttribute("tipo", session.getAttribute("tipo"));
			
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "Admin/profesores/edicion";
		}

		ResultadoResponse res = profesorService.update(profesor);
		if (!res.success) {
			m.addAttribute("cuenta", session.getAttribute("cuenta"));
			m.addAttribute("usuario", session.getAttribute("usuario"));
			m.addAttribute("tipo", session.getAttribute("tipo"));
			
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "Admin/profesores/edicion";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/profesores/filtrado";
	}

	@PostMapping("/cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		ResultadoResponse res = profesorService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/profesores/filtrado";
	}
}
