package com.intranet.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.intranet.dtos.RegistroForm;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalAttributes {

    @ModelAttribute
    public void setGlobalAttributes(Model model, HttpSession session) {
    	 Object usuario = session.getAttribute("usuario");
         Object cuenta = session.getAttribute("cuenta");
         Object tipo = session.getAttribute("tipo");
         Object carrera = session.getAttribute("carrera");
         Object ciclo = session.getAttribute("ciclo");
         Object registroDatos = session.getAttribute("registroDatos");
         if (usuario != null) {
             model.addAttribute("usuario", usuario);
         }
         if (cuenta != null) {
             model.addAttribute("cuenta", cuenta);
         }
         if (tipo != null) {
             model.addAttribute("tipo", tipo);
         }
         if (carrera != null) {
             model.addAttribute("carrera", carrera);
         }
         if (ciclo != null) {
             model.addAttribute("ciclo", ciclo);
         }
         if (registroDatos != null) {
             RegistroForm registro = (RegistroForm) registroDatos;
             model.addAttribute("registroForm", registro);
             model.addAttribute("Nombres", registro.getNombres() + " " + registro.getApellidos());
         }
    }
}

