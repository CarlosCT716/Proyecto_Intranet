package com.intranet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Asistencia;
import com.intranet.models.Boleta;
import com.intranet.models.Curso;
import com.intranet.models.DetalleBoleta;
import com.intranet.models.Horario;
import com.intranet.models.Horario_alumno;
import com.intranet.models.Notas;
import com.intranet.models.Usuario;
import com.intranet.repo.IRepoAsistencias;
import com.intranet.repo.IRepoBoleta;
import com.intranet.repo.IRepoHorario;
import com.intranet.repo.IRepoHorarioAlumno;
import com.intranet.repo.IRepoNotas;
import com.intranet.repo.IRepoUsuario;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MatriculaService {
	@Autowired
	private IRepoUsuario _usuario;

	@Autowired
	private IRepoHorario _horario;

	@Autowired
	private IRepoHorarioAlumno _hAlumno;

	@Autowired
	private IRepoNotas _notas;

	@Autowired
	private IRepoAsistencias _asistencia;

	@Autowired
	private IRepoBoleta _boleta;

	 public ResultadoResponse matricularAlumno(Integer idAlumno, List<Integer> idsHorarios) {
	        try {
	            Usuario alumno = _usuario.findById(idAlumno).orElseThrow();

	            int totalCreditos = 0;
	            double totalMonto = 0;
	            List<DetalleBoleta> detalles = new java.util.ArrayList<>();

	            for (Integer idHorario : idsHorarios) {
	                Horario horario = _horario.findById(idHorario).orElseThrow();

	                // 1. Insertar en tb_horarios_alumno
	                Horario_alumno ha = new Horario_alumno(alumno, horario);
	                _hAlumno.save(ha);

	                // 2. Insertar en tb_notas y tb_asistencias
	                Curso curso = horario.getCurso();
	                _notas.save(new Notas(alumno, curso));
	                _asistencia.save(new Asistencia(alumno, curso));

	                // 3. Calcular monto y detalle
	                int creditos = curso.getCreditos();
	                double monto = creditos * 100.0;
	                totalCreditos += creditos;
	                totalMonto += monto;

	                DetalleBoleta detalle = new DetalleBoleta();
	                detalle.setHorario(horario);
	                detalle.setConcepto(curso.getNombreCurso());
	                detalle.setCreditos(creditos);
	                detalle.setMonto(monto);
	                detalles.add(detalle);
	            }

	            // 4. Crear cabecera de boleta
	            Boleta boleta = new Boleta();
	            boleta.setUsuario(alumno);
	            boleta.setFecha(LocalDateTime.now());
	            boleta.setDetalles(detalles);
	            detalles.forEach(d -> d.setBoleta(boleta)); // enlazar cada detalle con la boleta

	            _boleta.save(boleta);

	            return new ResultadoResponse(true, "Matrícula realizada correctamente con " + totalCreditos + " créditos");

	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResultadoResponse(false, "Error al registrar matrícula: " + e.getMessage());
	        }
	    }
}
