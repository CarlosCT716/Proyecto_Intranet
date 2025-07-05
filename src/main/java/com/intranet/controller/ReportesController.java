package com.intranet.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.intranet.service.ReportesService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/reportes")
public class ReportesController {
	
	@Autowired
	private ReportesService reporteService;


    @GetMapping("/horario")
    public void horarioReporte(HttpSession session, HttpServletResponse response) throws Exception {
    	if (session.getAttribute("cuenta") == null) {
    		response.sendRedirect("/login");
    		return;
    	}

    	Integer idUsuario = (Integer) session.getAttribute("idUsuario");

    	// Ruta del archivo .jrxml dentro de resources
    	String reportPath = "/reportes/horario.jrxml";

    	// Parámetros para el reporte
    	Map<String, Object> params = new HashMap<>();
    	params.put("id_alumno", idUsuario);


    	// Obtener el JasperPrint
    	JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

    	// Configuración de respuesta HTTP
    	response.setContentType("application/pdf");
    	response.setHeader("Content-Disposition", "inline; filename=horario-alumno.pdf");

    	// Enviar el PDF al navegador
    	OutputStream outputStream = response.getOutputStream();
    	JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    	outputStream.flush();
    	outputStream.close();
    }



}
