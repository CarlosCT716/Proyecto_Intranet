package com.intranet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.intranet.dtos.HorarioFilter;
import com.intranet.dtos.RegistroForm;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Carrera;
import com.intranet.models.Ciclo;
import com.intranet.models.Horario;
import com.intranet.models.Usuario;
import com.intranet.service.AlumnoService;
import com.intranet.service.CarreraService;
import com.intranet.service.CicloService;
import com.intranet.service.HorarioService;
import com.intranet.service.MatriculaService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/matricula")
@SessionAttributes("seleccionados")
public class MatriculaController {

	@Autowired
	private HorarioService horarioService;
	@Autowired
	private CarreraService carreraService;
	@Autowired
	private CicloService cicloService;
	@Autowired
	private MatriculaService matriculaService;
	@Autowired
	private AlumnoService alumnoService;

	@PostMapping("seleccion")
	public String seleccion(Model m, RegistroForm registroForm, HorarioFilter filtro, HttpSession session) {

	    session.setAttribute("registroForm", registroForm);

	    List<Horario> horariosFiltrados = horarioService.getbyFilters(filtro);
	    int totalCreditos = horariosFiltrados.stream().mapToInt(h -> h.getCurso().getCreditos()).sum();
	    double totalMatricula = totalCreditos * 100.0;


	    m.addAttribute("totalCreditos", totalCreditos);
	    m.addAttribute("totalMatricula", totalMatricula);
	    m.addAttribute("carreras", carreraService.getAll());
	    m.addAttribute("ciclos", cicloService.getAll());
	    m.addAttribute("filtro", filtro);
	    m.addAttribute("lstHorarios", horariosFiltrados);
	    m.addAttribute("modalAbierto", true);
	    session.setAttribute("filtroActual", filtro);

	    return "registro/matricula";
	}


	@GetMapping("/seleccionFiltro")
	public String getSeleccion(Model m, HorarioFilter filtro, HttpSession session) {
	    RegistroForm registroForm = (RegistroForm) session.getAttribute("registroForm");

	    // Si no hay datos en sesión, redirigir al login o a la página de registro
	    if (registroForm == null) {
	        return "redirect:/login";
	    }

	    // ✅ GUARDAR ciclo y carrera seleccionados en el registroForm
	    registroForm.setIdCarrera(filtro.getIdCarrera());
	    registroForm.setIdCiclo(filtro.getIdCiclo());
	    session.setAttribute("registroForm", registroForm);
	    List<Horario> horariosFiltrados = horarioService.getbyFilters(filtro);
	    int totalCreditos = horariosFiltrados.stream().mapToInt(h -> h.getCurso().getCreditos()).sum();
	    double totalMatricula = totalCreditos * 100.0;

	    m.addAttribute("Nombres", registroForm.getNombres() + " " + registroForm.getApellidos());
	    m.addAttribute("Correo", registroForm.getCorreo());
	    m.addAttribute("totalCreditos", totalCreditos);
	    m.addAttribute("totalMatricula", totalMatricula);
	    m.addAttribute("carreras", carreraService.getAll());
	    m.addAttribute("ciclos", cicloService.getAll());
	    m.addAttribute("filtro", filtro);
	    m.addAttribute("lstHorarios", horariosFiltrados);
	    m.addAttribute("modalAbierto", true);

	    return "registro/matricula";
	}
	
	@PostMapping("/confirmar")
	public String confirmarMatricula(
	        @RequestParam("idsHorarios") List<Integer> idsHorarios,
	        HttpSession session,HorarioFilter filtro,
	        RedirectAttributes redirectAttributes) {

	    RegistroForm r = (RegistroForm) session.getAttribute("registroForm");

	    // Validar que el usuario esté en sesión
	    if (r == null) {
	        return "redirect:/login";
	    }

	    try {
	    	Usuario nuevo = new Usuario();
	    	nuevo.setNombres(r.getNombres());
	    	nuevo.setApellidos(r.getApellidos());
	    	nuevo.setUsuario(r.getUsuario());
	    	nuevo.setCorreo(r.getCorreo());
	    	nuevo.setPassword(r.getPassword());
	    	Ciclo ciclo = cicloService.getOne(r.getIdCiclo()); 
	    	nuevo.setCiclo(ciclo);
	    	Carrera carrera = carreraService.getOne(r.getIdCarrera()); 
	    	nuevo.setCarrera(carrera);
	    	alumnoService.create(nuevo);
	    	
	        Integer idAlumno = nuevo.getIdUsuario(); // Asegúrate de que RegistroForm tenga el idUsuario

	        ResultadoResponse resultado = matriculaService.matricularAlumno(idAlumno, idsHorarios);

	        if (resultado.success) {
	            redirectAttributes.addFlashAttribute("alert", Alert.sweetAlertSuccess(resultado.mensaje));
	        } else {
	        	 redirectAttributes.addFlashAttribute("alert", Alert.sweetAlertError(resultado.mensaje));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("alert", Alert.sweetAlertError(e.getMessage()));
	    }

	    return "redirect:/login";
	}

	

}
