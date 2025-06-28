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

import com.intranet.dtos.HorarioFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Horario;
import com.intranet.service.AulaService;
import com.intranet.service.CursoService;
import com.intranet.service.HorarioService;
import com.intranet.service.ModalidadService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

	@Autowired
	private HorarioService horarioService;
	@Autowired
	private CursoService cursoService;
	@Autowired
	private ModalidadService modalidadService;
	@Autowired
	private AulaService aulaService;

	@GetMapping("/filtrado")
	public String filtrado(@ModelAttribute HorarioFilter filtro, Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null)
			return "redirect:/login";
		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));

		m.addAttribute("modalidades", modalidadService.getAll());
		m.addAttribute("aulas", aulaService.getAll());
		m.addAttribute("filtro", filtro);
		m.addAttribute("lstHorarios", horarioService.search(filtro));
		return "Admin/horarios/filtrado";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model m, HttpSession session) {
		m.addAttribute("horario", new Horario());

		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));

		m.addAttribute("cursos", cursoService.getAll());
		m.addAttribute("aulas", aulaService.getAll());
		m.addAttribute("modalidades", modalidadService.getAll());
		return "Admin/horarios/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(@Validated @ModelAttribute Horario horario, BindingResult br, Model m,
			RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("cursos", cursoService.getAll());
			m.addAttribute("aulas", aulaService.getAll());
			m.addAttribute("modalidades", modalidadService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "horarios/nuevo";
		}
		ResultadoResponse res = horarioService.create(horario);
		if (!res.success) {
			m.addAttribute("cursos", cursoService.getAll());
			m.addAttribute("aulas", aulaService.getAll());
			m.addAttribute("modalidades", modalidadService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "horarios/nuevo";
		}
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/horarios/filtrado";
	}

	@GetMapping("/edicion/{id}")
	public String edicion(@PathVariable Integer id, Model m, HttpSession session) {
		m.addAttribute("horario", horarioService.getOne(id));
		m.addAttribute("cuenta", session.getAttribute("cuenta"));
		m.addAttribute("usuario", session.getAttribute("usuario"));
		m.addAttribute("tipo", session.getAttribute("tipo"));
		m.addAttribute("cursos", cursoService.getAll());
		m.addAttribute("aulas", aulaService.getAll());
		m.addAttribute("modalidades", modalidadService.getAll());
		return "Admin/horarios/edicion";
	}

	@PostMapping("/guardar")
	public String guardar(@Validated @ModelAttribute Horario horario, BindingResult br, Model m,
			RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("cursos", cursoService.getAll());
			m.addAttribute("aulas", aulaService.getAll());
			m.addAttribute("modalidades", modalidadService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "horarios/edicion";
		}
		ResultadoResponse res = horarioService.update(horario);
		if (!res.success) {
			m.addAttribute("cursos", cursoService.getAll());
			m.addAttribute("aulas", aulaService.getAll());
			m.addAttribute("modalidades", modalidadService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "horarios/edicion";
		}
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/horarios/filtrado";
	}

	@PostMapping("/cambiar-estado/{id}")
	public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
		ResultadoResponse res = horarioService.cambiarEstado(id);
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/horarios/filtrado";
	}
}
