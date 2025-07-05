package com.intranet.controller.alumno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.intranet.dtos.CursoFilter;
import com.intranet.models.Asistencia;
import com.intranet.models.Horario_alumno;
import com.intranet.models.Notas;
import com.intranet.service.AsistenciaService;
import com.intranet.service.AutenticationService;
import com.intranet.service.CarreraService;
import com.intranet.service.HorarioAlumnoService;
import com.intranet.service.NotasService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/alumno")
public class OperacionesController {

	@Autowired
	private NotasService notas;

	@Autowired
	private CarreraService carreraService;

	@Autowired
	private HorarioAlumnoService horarioService;
	
	@Autowired
	private AsistenciaService asistenciaService;
	@Autowired
	private AutenticationService autenticationService;
	

	@GetMapping("/notas")
	public String notas(Model m, HttpSession session, CursoFilter filtro) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Alumno(session)) {
		    return "redirect:/login";
		}

		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		Integer idCarrera = (Integer) session.getAttribute("carrera");

		filtro.setIdUsuario(idUsuario);
		filtro.setIdCarrera(idCarrera);

		m.addAttribute("carrera", carreraService.getOne(idCarrera).getDescripcion());

		List<Notas> filtrado = notas.getByAlumno(filtro);
		m.addAttribute("lstNotas", filtrado);
		return "Alumno/Notas";
	}

	@GetMapping("/horario")
	public String verHorario(Model m, HttpSession session) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Alumno(session)) {
		    return "redirect:/login";
		}
		
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		Integer idCarrera = (Integer) session.getAttribute("carrera");

		List<Horario_alumno> horarios = horarioService.getHorarioByAlumno(idUsuario);
		m.addAttribute("carrera", carreraService.getOne(idCarrera).getDescripcion());
		m.addAttribute("lstHorarios", horarios);
		return "Alumno/Horario";
	}
	
	@GetMapping("/asistencias")
	public String verAsistencias(Model m, HttpSession session, CursoFilter filtro) {
		if (session.getAttribute("cuenta") == null || !autenticationService.Alumno(session)) {
		    return "redirect:/login";
		}

		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		filtro.setIdUsuario(idUsuario);
		Integer idCarrera = (Integer) session.getAttribute("carrera");

		m.addAttribute("carrera", carreraService.getOne(idCarrera).getDescripcion());
		

		List<Asistencia> asistencias = asistenciaService.getAsistenciasByAlumno(filtro);
		m.addAttribute("lstAsistencias", asistencias);

		return "Alumno/Asistencia";
	}



}
