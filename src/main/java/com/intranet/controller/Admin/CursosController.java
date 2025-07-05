package com.intranet.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Curso;
import com.intranet.service.AutenticationService;
import com.intranet.service.CarreraService;
import com.intranet.service.CicloService;
import com.intranet.service.CursoService;
import com.intranet.service.UsuarioService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursosController {
	@Autowired
	private CursoService cursoService;
	@Autowired
	private CarreraService carreraService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private CicloService cicloService;
	@Autowired
	private AutenticationService autenticationService;

	@GetMapping("/filtrado")
	public String filtrado(@ModelAttribute CursoFilter filtro, Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
			return "redirect:/login";
		}
		m.addAttribute("ciclos", cicloService.getAll());
		m.addAttribute("carreras", carreraService.getAll());
		m.addAttribute("filtro", filtro);
		m.addAttribute("lstCurso", cursoService.search(filtro));
		return "Admin/cursos/filtrado";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
			return "redirect:/login";
		}
		m.addAttribute("curso", new Curso());
		m.addAttribute("ciclos", cicloService.getAll());
		m.addAttribute("carreras", carreraService.getAll());
		m.addAttribute("usuarios", usuarioService.getProfesores());
		return "Admin/cursos/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(@Valid @ModelAttribute Curso curso, BindingResult br, Model m,
			RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getProfesores());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "Admin/cursos/nuevo";
		}
		ResultadoResponse res = cursoService.create(curso);
		if (!res.success) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getProfesores());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "Admin/cursos/nuevo";
		}
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/cursos/filtrado";
	}

	@GetMapping("/edicion/{id}")
	public String edicion(@PathVariable Integer id, Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
			return "redirect:/login";
		}
		m.addAttribute("curso", cursoService.getOne(id));
		m.addAttribute("ciclos", cicloService.getAll());
		m.addAttribute("carreras", carreraService.getAll());
		m.addAttribute("usuarios", usuarioService.getProfesores());
		return "Admin/cursos/edicion";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute("curso") Curso curso, BindingResult br, Model m, RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "Admin/cursos/edicion";
		}
		ResultadoResponse res = cursoService.update(curso);
		if (!res.success) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "Admin/cursos/edicion";
		}
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/cursos/filtrado";
	}

	@PostMapping("/cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		ResultadoResponse res = cursoService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/cursos/filtrado";
	}
}
