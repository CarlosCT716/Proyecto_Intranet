package com.intranet.controller.profesor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Asistencia;
import com.intranet.models.Curso;
import com.intranet.service.AsistenciaService;
import com.intranet.service.CarreraService;
import com.intranet.service.CicloService;
import com.intranet.service.CursoService;
import com.intranet.service.UsuarioService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {
	@Autowired
	private AsistenciaService asistenciaService;
	@Autowired
	private CursoService cursoService;
	@Autowired
	private CicloService cicloService;
	@Autowired
	private CarreraService carreraService;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/filtrado")
    public String getListado(@ModelAttribute CursoFilter filtro, Model m, HttpSession session) {
        if (session.getAttribute("cuenta") == null)
            return "redirect:/login";
        
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        filtro.setIdUsuario(idUsuario);
        
		m.addAttribute("filtro", filtro);
        m.addAttribute("carreras",carreraService.getAll());
        m.addAttribute("ciclos",cicloService.getAll());
        m.addAttribute("selectedCurso", filtro.getIdCurso());
        
        List<Curso> cFiltrado = cursoService.search(filtro);
        m.addAttribute("cursos",cFiltrado);
        
        m.addAttribute("lstAsistencias",asistenciaService.getAsistenciasPorCurso(filtro));

        return "/Profesor/Asistencias";
	}

	@GetMapping("/edicion")
	public String edicion(Model m) {
		return "Profesor/editar";
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Integer id, Model m, HttpSession session) {
	    if (session.getAttribute("cuenta") == null) {
	        return "redirect:/login";
	    }

	    Asistencia asistencia = asistenciaService.getbyID(id);
	    
	    m.addAttribute("asistencia", asistencia);
	    m.addAttribute("usuarios", usuarioService.getAlumnos());
	    m.addAttribute("cursos", cursoService.getAll()); 

	    return "Profesor/editar";
	}

	@PostMapping("/guardar")
	public String guardar(@Validated @ModelAttribute Asistencia asistencia, BindingResult br, Model m, RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "Profesor/editar";
		}
		ResultadoResponse res = asistenciaService.guardar(asistencia);
		if (!res.success) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "Profesor/editar";
		}
		flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
		return "redirect:/asistencia/filtrado";
	}

	
	
}
