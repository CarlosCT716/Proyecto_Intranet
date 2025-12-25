package com.cibertec.intranet.matricula.repository;

import com.cibertec.intranet.matricula.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByMatriculaIdMatricula(Integer idMatricula);

    @Query("SELECT p FROM Pago p WHERE p.matricula.idMatricula = :idMatricula AND p.estadoPago.idEstadoPago = :idEstado")
    List<Pago> buscarPagosPorMatriculaYEstado(@Param("idMatricula") Integer idMatricula, @Param("idEstado") Integer idEstado);

    @Query("SELECT p FROM Pago p WHERE p.matricula.idMatricula = :idMatricula " +
            "AND p.estadoPago.idEstadoPago = :idEstado " +
            "AND p.fechaVencimiento <= :fechaLimite " +
            "ORDER BY p.fechaVencimiento ASC")
    List<Pago> buscarPendientesVisibles(
            @Param("idMatricula") Integer idMatricula,
            @Param("idEstado") Integer idEstado,
            @Param("fechaLimite") LocalDate fechaLimite
    );
    @Query("SELECT p FROM Pago p WHERE p.matricula.idMatricula = :idMatricula " +
            "AND p.fechaVencimiento <= :fechaLimite " +
            "ORDER BY p.fechaVencimiento ASC")
    List<Pago> buscarHistorialVisible(
            @Param("idMatricula") Integer idMatricula,
            @Param("fechaLimite") LocalDate fechaLimite
    );
    List<Pago> findByEstadoPago_IdEstadoPago(Integer idEstadoPago);
}