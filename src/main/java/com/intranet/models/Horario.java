package com.intranet.models;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Table(name = "tb_horarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Horario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario")
	private Integer idHorario;

	@NotNull(message = "El curso es obligatorio")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_curso")
	private Curso curso;

	@NotBlank(message = "El día es obligatorio")
	@Column(name = "dia_semana")
	private String Dia;

	@NotNull(message = "La hora de inicio es obligatoria")
	@Column(name = "hora_inicio")
	private LocalTime inicio;

	@NotNull(message = "La hora de fin es obligatoria")
	@Column(name = "hora_fin")
	private LocalTime fin;

	@NotNull(message = "El aula es obligatoria")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_aula")
	private Aula aula;

	@NotNull(message = "La modalidad es obligatoria")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modalidad")
	private Modalidad modalidad;

	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1")
	private Boolean estado = true;
	

    @AssertTrue(message = "La hora de inicio debe ser después de las 07:00 y antes de la hora de fin")
    public boolean isHoraInicioValida() {
        if (inicio == null || fin == null) return true; 
        return !inicio.isBefore(LocalTime.of(7, 0)) && inicio.isBefore(fin);
    }

    @AssertTrue(message = "La hora de fin debe ser antes de las 22:00 y después de la hora de inicio")
    public boolean isHoraFinValida() {
        if (inicio == null || fin == null) return true;
        return !fin.isAfter(LocalTime.of(22, 0)) && fin.isAfter(inicio);
    }

}