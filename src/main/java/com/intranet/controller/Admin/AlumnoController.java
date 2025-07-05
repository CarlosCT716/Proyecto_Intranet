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

import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Usuario;
import com.intranet.service.AlumnoService;
import com.intranet.service.AutenticationService;
import com.intranet.service.CarreraService;
import com.intranet.service.UsuarioService;
import com.intranet.utils.Alert;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;
	@Autowired
	private CarreraService carreraService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private AutenticationService autenticationService;
	

    @GetMapping("/filtrado")
    public String filtrado(Model m, HttpSession session) {
    	if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
		    return "redirect:/login";
		}
        m.addAttribute("lstAlumnos", alumnoService.getAll());
        return "Admin/alumnos/filtrado";
    }


    @GetMapping("/edicion/{id}")
    public String edicion(@PathVariable Integer id, Model m, HttpSession session) {
    	if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
		    return "redirect:/login";
		}
        Usuario alumno = alumnoService.getOne(id);
        m.addAttribute("alumno", alumno);
        m.addAttribute("carreras", carreraService.getAll());
        return "Admin/alumnos/edicion";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("alumno") Usuario alumno,
                          BindingResult br,
                          Model m,
                          RedirectAttributes flash) {
        if (br.hasErrors()) {
        	m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
            m.addAttribute("alert", Alert.sweetAlertInfo("Falta completar información"));
            return "Admin/alumnos/edicion";
        }

        ResultadoResponse res = alumnoService.update(alumno);
        if (!res.success) {
        	m.addAttribute("carreras", carreraService.getAll());
			m.addAttribute("usuarios", usuarioService.getAll());
            m.addAttribute("alert", Alert.sweetAlertError(res.mensaje));
            return "Admin/profesores/edicion";
        }

        flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
        return "redirect:/alumnos/filtrado";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
        ResultadoResponse res = alumnoService.cambiarEstado(id);
        flash.addFlashAttribute("toast", Alert.sweetToast(res.mensaje, "success", 5000));
        return "redirect:/alumnos/filtrado";
    }
}
