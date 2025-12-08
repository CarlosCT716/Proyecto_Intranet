package com.cibertec.intranet.matricula.service;

import com.cibertec.intranet.academico.model.Carrera;
import com.cibertec.intranet.academico.model.Ciclo;
import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.academico.repository.CarreraRepository;
import com.cibertec.intranet.academico.repository.CicloRepository;
import com.cibertec.intranet.academico.repository.CursoRepository;
import com.cibertec.intranet.matricula.dto.MatriculaDTO;
import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.DetalleMatriculaRepository;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import com.cibertec.intranet.profesor.model.Nota;
import com.cibertec.intranet.profesor.repository.NotaRepository;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final PagoRepository pagoRepository;

    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final CicloRepository cicloRepository;
    private final CursoRepository cursoRepository;
    private final NotaRepository notaRepository;

    @Transactional
    public Matricula registrarMatricula(MatriculaDTO dto) {

        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + dto.getIdAlumno()));

        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + dto.getIdCarrera()));

        Ciclo ciclo = cicloRepository.findById(dto.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + dto.getIdCiclo()));

        if (dto.getIdCursos() == null || dto.getIdCursos().isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos un curso para matricularse.");
        }

        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setCarrera(carrera);
        matricula.setCiclo(ciclo);
        matricula.setPeriodo(dto.getPeriodo());
        matricula.setFechaMatricula(LocalDateTime.now());

        Matricula matriculaGuardada = matriculaRepository.save(matricula);

        // Variable para acumular créditos
        int totalCreditos = 0;

        // 3. Procesar Cursos
        for (Integer idCurso : dto.getIdCursos()) {
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + idCurso));
            totalCreditos += curso.getCreditos();

            // Guardar Detalle
            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matriculaGuardada);
            detalle.setCurso(curso);
            DetalleMatricula detalleGuardado = detalleMatriculaRepository.save(detalle);

            // Inicializar Notas
            Nota notaInicial = new Nota();
            notaInicial.setDetalleMatricula(detalleGuardado);
            notaInicial.setNota1(BigDecimal.ZERO);
            notaInicial.setNota2(BigDecimal.ZERO);
            notaInicial.setNota3(BigDecimal.ZERO);
            notaInicial.setExamenFinal(BigDecimal.ZERO);
            notaRepository.save(notaInicial);
        }

        generarPagosAutomaticos(matriculaGuardada, totalCreditos);

        return matriculaGuardada;
    }

    private void generarPagosAutomaticos(Matricula matricula, int totalCreditos) {
        LocalDate fechaBase = LocalDate.now();

        crearCuota(matricula, "Derecho de Matrícula", new BigDecimal("300.00"), fechaBase.plusDays(3));

        BigDecimal costoPorCredito = new BigDecimal("75.00");
        BigDecimal totalBoleta = costoPorCredito.multiply(new BigDecimal(totalCreditos));


        int numeroCuotas = 5;
        BigDecimal montoCuota = totalBoleta.divide(new BigDecimal(numeroCuotas), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= numeroCuotas; i++) {
            String concepto = "Mensualidad " + i + " (Costo Créditos)";
            LocalDate vencimiento = fechaBase.plusMonths(i).withDayOfMonth(1);

            if (i == numeroCuotas) {
                BigDecimal pagadoHastaAhora = montoCuota.multiply(new BigDecimal(numeroCuotas - 1));
                BigDecimal ultimaCuota = totalBoleta.subtract(pagadoHastaAhora);
                crearCuota(matricula, concepto, ultimaCuota, vencimiento);
            } else {
                crearCuota(matricula, concepto, montoCuota, vencimiento);
            }
        }
    }

    private void crearCuota(Matricula m, String concepto, BigDecimal monto, LocalDate vencimiento) {
        Pago pago = new Pago();
        pago.setMatricula(m);
        pago.setConcepto(concepto);
        pago.setMonto(monto);
        pago.setFechaVencimiento(vencimiento);
        pago.setIdEstadoPago(1);
        pago.setFechaPago(null);

        pagoRepository.save(pago);
    }

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }
}