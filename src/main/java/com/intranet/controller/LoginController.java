package com.intranet.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.intranet.models.Usuario;
import com.intranet.service.AutenticationService;
import com.intranet.service.CarreraService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;

import com.intranet.dtos.AutenticationFilter;
import com.intranet.dtos.HorarioFilter;
import com.intranet.dtos.RegistroForm;

@Controller
public class LoginController {

	@Autowired
	private AutenticationService autenticationService;
	@Autowired
	private CarreraService carreraService;


	@GetMapping({ "/", "/login" })
	public String login(Model model, HorarioFilter filtro) {
		model.addAttribute("RegistroForm", new RegistroForm());
		model.addAttribute("filter", new AutenticationFilter());
		model.addAttribute("carreras", carreraService.getAll());

		return "login";
	}

	@PostMapping("/iniciar-sesion")
	public String iniciarSesion(@ModelAttribute AutenticationFilter filter, HttpSession session, Model model,
			RedirectAttributes flash) {
		Usuario usuarioValidado = autenticationService.autenticar(filter);

		if (usuarioValidado == null) {
			model.addAttribute("filter", new AutenticationFilter());
			model.addAttribute("alert", Alert.sweetAlertError("Usuario y/o clave incorrecta"));
			return "login";
		}

		String nombreCompleto = String.format("%s %s", usuarioValidado.getNombres(), usuarioValidado.getApellidos());
		session.setAttribute("idUsuario", usuarioValidado.getIdUsuario());
		session.setAttribute("usuario", nombreCompleto);
		session.setAttribute("cuenta", usuarioValidado.getUsuario());
		session.setAttribute("tipo", usuarioValidado.getTipo().getId());
		if (usuarioValidado.getCarrera() != null) {
			session.setAttribute("carrera", usuarioValidado.getCarrera().getIdCarrera());
		}
		String alert = Alert.sweetAlertSuccess("Bienvenido a Intranet " + nombreCompleto);
		flash.addFlashAttribute("alert", alert);

		return "redirect:/inicio";
	}

	@GetMapping("/inicio")
	public String inicio(HttpSession session, Model model) {
		if (session.getAttribute("cuenta") == null)
			return "redirect:/login";

		model.addAttribute("cuenta", session.getAttribute("cuenta"));
		model.addAttribute("usuario", session.getAttribute("usuario"));
		model.addAttribute("tipo", session.getAttribute("tipo"));
		model.addAttribute("carrera", session.getAttribute("carrera"));
		return "Inicio";
	}

	@GetMapping("/cerrar-sesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}




}
