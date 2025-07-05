package com.intranet.controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.intranet.models.Auditoria;
import com.intranet.repo.IRepoAuditoria;
import com.intranet.service.AutenticationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auditoria")
public class AuditoriaController {

    @Autowired
    private IRepoAuditoria _auditoria;
    
    @Autowired
    private AutenticationService autenticationService;
    
    @GetMapping("listado")
    public String listado(Model m,HttpSession session) {
    	if (session.getAttribute("cuenta") == null || !autenticationService.Admin(session)) {
		    return "redirect:/login";
		}
        List<Auditoria> logs = _auditoria.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
        m.addAttribute("logs", logs);
        return "Admin/auditoria/Auditoria"; 
    }
}