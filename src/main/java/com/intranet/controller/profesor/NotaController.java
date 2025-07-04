package com.intranet.controller.profesor;

import java.util.List;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Curso;
import com.intranet.models.Notas;
import com.intranet.service.CarreraService;
import com.intranet.service.CicloService;
import com.intranet.service.CursoService;
import com.intranet.service.NotasService;
import com.intranet.service.UsuarioService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nota")
public class NotaController {

    @Autowired
    private NotasService notasService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private CicloService cicloService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/filtrado")
    public String getListado(@ModelAttribute CursoFilter filtro, @RequestParam(value = "idCurso", required = false) Integer idCurso, Model m, HttpSession session) {
        if (session.getAttribute("cuenta") == null)
            return "redirect:/login";
        
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        filtro.setIdUsuario(idUsuario);
        
        if (idCurso != null) {
            filtro.setIdCurso(idCurso); // aquí se aplica el filtro
        }
        
        
		m.addAttribute("filtro", filtro);
        m.addAttribute("carreras",carreraService.getAll());
        m.addAttribute("ciclos",cicloService.getAll());
        m.addAttribute("selectedCurso", filtro.getIdCurso());
        
        List<Curso> cFiltrado = cursoService.search(filtro);
        m.addAttribute("cursos",cFiltrado);
        
        m.addAttribute("lstNotas",notasService.getNotasPorCurso(filtro));

        return "/Profesor/Notas";
	}

	@GetMapping("/edicion")
	public String edicion(Model m) {
		return "Profesor/editarNota";
	}
	
	@GetMapping("/editarNota/{id}")
	public String editar(@PathVariable("id") Integer id, Model m, HttpSession session) {
	    if (session.getAttribute("cuenta") == null) {
	        return "redirect:/login";
	    }

	    Notas notas = notasService.getbyID(id);
	    

	    m.addAttribute("cuenta", session.getAttribute("cuenta"));
	    m.addAttribute("usuario", session.getAttribute("usuario"));
	    m.addAttribute("tipo", session.getAttribute("tipo"));
	    
	    m.addAttribute("notas", notas);
	    m.addAttribute("usuarios", usuarioService.getAlumnos());
	    m.addAttribute("cursos", cursoService.getAll()); 

	    return "Profesor/editarNota";
	}
	@PostMapping("/guardar")
	public String guardar(@Validated @ModelAttribute Notas notas, BindingResult br, Model m, RedirectAttributes flash) {
		if (br.hasErrors()) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
			return "Profesor/editarNota";
		}
		ResultadoResponse res = notasService.guardar(notas);
		if (!res.success) {
			m.addAttribute("ciclos", cicloService.getAll());
			m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
			m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
			return "Profesor/editarNota";
		}
		// Obtener datos relacionados al filtro
		Curso curso = cursoService.getbyID(notas.getCurso().getIdCurso());
		Integer idCurso = curso.getIdCurso();
		Integer idCiclo = curso.getCiclo().getIdCiclo();
		Integer idCarrera = curso.getCarrera().getIdCarrera();

	    
		flash.addFlashAttribute("toast", Alert.sweetToast("Nota actualizada correctamente", "success", 5000));
		return "redirect:/nota/filtrado?idCurso=" + idCurso +
		           "&idCiclo=" + idCiclo +
		           "&idCarrera=" + idCarrera;
		}
	



}
